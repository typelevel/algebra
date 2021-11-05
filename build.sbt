import sbtrelease.Utilities._
import ReleaseTransformations._
import microsites.ExtraMdFileConfig
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import com.typesafe.tools.mima.core._

lazy val catsVersion     = "2.6.1"
lazy val mUnit           = "0.7.26"
lazy val disciplineMUnit = "1.0.9"

val Scala212 = "2.12.13"
val Scala213 = "2.13.6"
val Scala300 = "3.0.0"

ThisBuild / crossScalaVersions := Seq(Scala212, Scala213, Scala300)
ThisBuild / scalaVersion := Scala213

ThisBuild / githubWorkflowBuildMatrixAdditions +=
  "platform" -> List("jvm", "js", "native")

ThisBuild / githubWorkflowBuildMatrixExclusions +=
  MatrixExclude(Map("platform" -> "native", "scala" -> Scala300))

val JvmCond = s"matrix.platform == 'jvm'"
val JsCond = s"matrix.platform == 'js'"
val NativeCond = s"matrix.platform == 'native'"

ThisBuild / githubWorkflowBuildMatrixFailFast := Some(false)
ThisBuild / githubWorkflowArtifactUpload := false

ThisBuild / githubWorkflowPublishTargetBranches := Seq()

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(List("coreJS/test", "lawsJS/test"), name = Some("Validate Scala JS"), cond = Some(JsCond)),
  WorkflowStep.Sbt(List("coreNative/test", "lawsNative/test"), name = Some("Validate Scala Native"), cond = Some(NativeCond)),
  WorkflowStep.Sbt(List("coreJVM/test", "lawsJVM/test", "benchmark/test"), name = Some("Validate Scala JVM"), cond = Some(JvmCond))
)

ThisBuild / githubWorkflowAddedJobs ++= Seq(
  WorkflowJob(
    "microsite",
    "Microsite",
    githubWorkflowJobSetup.value.toList ::: List(
      WorkflowStep.Use(UseRef.Public("ruby", "setup-ruby", "v1"), Map("ruby-version" -> "2.7"), name = Some("Setup Ruby")),
      WorkflowStep.Run(List("gem install jekyll -v 4.0.0"), name = Some("Setup Jekyll")),
      WorkflowStep.Sbt(List("docs/makeMicrosite"), name = Some("Build the microsite"))
    ),
    scalas = List(Scala212)
  ),
  WorkflowJob(
    "mima",
    "MiMa",
    githubWorkflowJobSetup.value.toList ::: List(
      WorkflowStep.Sbt(List("mimaReportBinaryIssues"), name = Some("Run MiMa"))
    ),
    scalas = List(Scala212)
  )
)


lazy val buildSettings = Seq(
  organization := "org.typelevel"
)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    //"-Ywarn-value-discard", // fails with @sp on Unit
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, v)) if v <= 12 => Seq("-Ywarn-unused-import")
    case _ => Seq.empty
  }),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        Seq(
          "-Xfuture",
          "-Xfatal-warnings",
          "-Yno-adapted-args"
        )
      case _ =>
        Nil
    }
  },
  resolvers += Resolver.sonatypeRepo("public"),
  Compile / console / scalacOptions ~= (_ filterNot (_ == "-Ywarn-unused-import")),
  Test / console / scalacOptions := (Compile / console / scalacOptions).value,
  Global / scalaJSStage := FastOptStage,
  jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
  fork := false,
  Test / parallelExecution := false
)

lazy val algebraSettings = buildSettings ++ commonSettings ++ publishSettings

lazy val nativeSettings = Seq(
  crossScalaVersions ~= (_.filterNot(Scala300.contains)),
  publishConfiguration := publishConfiguration.value.withOverwrite(true) // needed since we double-publish on release
)

lazy val docsMappingsAPIDir =
  settingKey[String]("Name of subdirectory in site target directory for api docs")

lazy val docSettings = Seq(
  micrositeName := "Algebra",
  micrositeDescription := "Algebraic Typeclasses for Scala.",
  micrositeAuthor := "Algebra's contributors",
  micrositeHighlightTheme := "atom-one-light",
  micrositeHomepage := "https://typelevel.org/algebra/",
  micrositeBaseUrl := "algebra",
  micrositeDocumentationUrl := "api/",
  micrositeGithubOwner := "typelevel",
  micrositeExtraMdFiles := Map(file("CONTRIBUTING.md") -> ExtraMdFileConfig("contributing.md", "page")), // TODO check layout
  micrositeGithubRepo := "algebra",
  autoAPIMappings := true,
  ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(coreJVM, lawsJVM),
  docsMappingsAPIDir := "api",
  addMappingsToSiteDir(ScalaUnidoc / packageDoc / mappings, docsMappingsAPIDir),
  git.remoteRepo := "git@github.com:typelevel/algebra.git",
  ghpagesNoJekyll := false,
  mdoc / fork := true,
  ScalaUnidoc / unidoc / fork := true,
  ScalaUnidoc / unidoc / scalacOptions ++= Seq(
      "-doc-source-url", "https://github.com/typelevel/algebra/tree/master€{FILE_PATH}.scala",
      "-sourcepath", (LocalRootProject / baseDirectory).value.getAbsolutePath,
      "-diagrams"
    ),
  makeSite / includeFilter := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md"
)

lazy val docs = project.in(file("docs"))
  .enablePlugins(MdocPlugin)
  .enablePlugins(GhpagesPlugin)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(moduleName := "algebra-docs")
  .settings(algebraSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(docSettings)
  .dependsOn(coreJVM, lawsJVM)

lazy val aggregate = project.in(file("."))
  .enablePlugins(GhpagesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .disablePlugins(MimaPlugin)
  .settings(algebraSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(docSettings: _*)
  .aggregate(coreJVM, lawsJVM, benchmark)
  .dependsOn(coreJVM, lawsJVM)
  .aggregate(coreJS, lawsJS)
  .dependsOn(coreJS, lawsJS)
  .aggregate(coreNative, lawsNative)
  .dependsOn(coreNative, lawsNative)

val binaryCompatibleVersions = Set(
  "1.0.0",
  "1.0.1",
  "2.0.0",
  "2.0.1",
  "2.1.0",
  "2.1.1"
)

/**
  * Empty this each time we publish a new version (and bump the minor number)
  */
val ignoredABIProblems = {
  import com.typesafe.tools.mima.core._
  import com.typesafe.tools.mima.core.ProblemFilters._
  Seq(
    exclude[ReversedMissingMethodProblem]("algebra.ring.RingFunctions.defaultFromDouble"),
    exclude[IncompatibleSignatureProblem]("algebra.instances.all.package.catsKernelStdOrderForChar"),
    exclude[IncompatibleSignatureProblem]("algebra.instances.char.package.catsKernelStdOrderForChar")
  )
}

lazy val core = crossProject(JSPlatform, NativePlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(MimaPlugin)
  .settings(moduleName := "algebra")
  .settings(
    mimaPreviousArtifacts := binaryCompatibleVersions.map(v => "org.typelevel" %% "algebra" % v),
    mimaBinaryIssueFilters ++= ignoredABIProblems,
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.5.0",
      "org.typelevel" %%% "cats-kernel" % catsVersion,
      "org.typelevel" %%% "discipline-munit" % disciplineMUnit % Test,
      "org.scalameta" %%% "munit" % mUnit % Test
    )
  )
  .settings(algebraSettings: _*)
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .settings(Compile / sourceGenerators += (Compile / sourceManaged).map(Boilerplate.gen).taskValue)
  .nativeSettings(nativeSettings)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
lazy val coreNative = core.native

lazy val laws = crossProject(JSPlatform, NativePlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .enablePlugins(MimaPlugin)
  .dependsOn(core)
  .settings(
    moduleName := "algebra-laws",
    mimaPreviousArtifacts := binaryCompatibleVersions.map(v => "org.typelevel" %% "algebra-laws" % v),
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-kernel-laws" % catsVersion,
      "org.typelevel" %%% "discipline-munit" % disciplineMUnit % Test,
      "org.scalameta" %%% "munit" % mUnit % Test
    )
  )
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .nativeSettings(nativeSettings)
  .settings(algebraSettings: _*)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js
lazy val lawsNative = laws.native

lazy val benchmark = project.in(file("benchmark"))
  .settings(
    moduleName := "algebra-benchmark"
  )
  .enablePlugins(JmhPlugin)
  .disablePlugins(MimaPlugin)
  .settings(JmhPlugin.projectSettings:_*)
  .settings(noPublishSettings: _*)
  .settings(algebraSettings: _*)
  .dependsOn(coreJVM)

lazy val publishSettings = Seq(
  homepage := Some(url("http://typelevel.org/algebra")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  autoAPIMappings := true,
  apiURL := Some(url("https://typelevel.org/algebra/api/")),

  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <id>johnynek</id>
        <name>P. Oscar Boykin</name>
        <url>https://github.com/johnynek/</url>
      </developer>
      <developer>
        <id>avibryant</id>
        <name>Avi Bryant</name>
        <url>https://github.com/avibryant/</url>
      </developer>
      <developer>
        <id>non</id>
        <name>Erik Osheim</name>
        <url>http://github.com/non/</url>
      </developer>
      <developer>
        <id>tixxit</id>
        <name>Tom Switzer</name>
        <url>http://github.com/tixxit/</url>
      </developer>
    </developers>
  ),
  releaseProcess := Seq[ReleaseStep](
    inquireVersions,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeReleaseAll"),
    pushChanges
  )
)

addCommandAlias("validate", ";compile;test;unidoc")

// Base Build Settings

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

def crossVersionSharedSources() =
  Seq(Compile, Test).map { sc =>
    (sc / unmanagedSourceDirectories) ++= {
      (sc / unmanagedSourceDirectories).value.map {
        dir:File => new File(dir.getPath + "_" + scalaBinaryVersion.value)
      }
    }
  }

addCommandAlias("gitSnapshots", ";set version in ThisBuild := git.gitDescribedVersion.value.get + \"-SNAPSHOT\"")

// For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
credentials ++= (for {
  username <- Option(System.getenv().get("SONATYPE_USERNAME"))
  password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq

credentials += Credentials(
  Option(System.getProperty("build.publish.credentials")) map (new File(_)) getOrElse (Path.userHome / ".ivy2" / ".credentials")
)
