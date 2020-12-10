import sbtrelease.Utilities._
import ReleaseTransformations._
import microsites.ExtraMdFileConfig
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import com.typesafe.tools.mima.core._

lazy val catsVersion     = "2.3.0"
lazy val mUnit           = "0.7.19"
lazy val disciplineMUnit = "1.0.3"

val Scala212 = "2.12.12"
val Scala213 = "2.13.4"
val Scala300 = Seq("3.0.0-M1", "3.0.0-M2")

ThisBuild / crossScalaVersions := Seq(Scala212, Scala213) ++ Scala300
ThisBuild / scalaVersion := Scala213

ThisBuild / githubWorkflowArtifactUpload := false

ThisBuild / githubWorkflowPublishTargetBranches := Seq()

ThisBuild / githubWorkflowAddedJobs ++= Seq(
  WorkflowJob(
    "microsite",
    "Microsite",
    githubWorkflowJobSetup.value.toList ::: List(
      WorkflowStep.Use("actions", "setup-ruby", "v1", name = Some("Setup Ruby")),
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
  scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import")),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  scalaJSStage in Global := FastOptStage,
  jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
  fork := false,
  parallelExecution in Test := false
)

lazy val algebraSettings = buildSettings ++ commonSettings ++ publishSettings

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
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(coreJVM, lawsJVM),
  docsMappingsAPIDir := "api",
  addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), docsMappingsAPIDir),
  git.remoteRepo := "git@github.com:typelevel/algebra.git",
  ghpagesNoJekyll := false,
  fork in tut := true,
  fork in (ScalaUnidoc, unidoc) := true,
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
      "-doc-source-url", "https://github.com/typelevel/algebra/tree/master€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
      "-diagrams"
    ),
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md"
)

lazy val docs = project.in(file("docs"))
  .enablePlugins(TutPlugin)
  .enablePlugins(GhpagesPlugin)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(ScalaUnidocPlugin)
  .settings(moduleName := "algebra-docs")
  .settings(algebraSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(docSettings)
  .settings((scalacOptions in Tut) ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
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

val binaryCompatibleVersion = "1.0.1"

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

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .enablePlugins(MimaPlugin)
  .settings(moduleName := "algebra")
  .settings(
    mimaPreviousArtifacts := Set("org.typelevel" %% "algebra" % binaryCompatibleVersion),
    mimaBinaryIssueFilters ++= ignoredABIProblems,
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-kernel" % catsVersion,
    "org.typelevel" %%% "discipline-munit" % disciplineMUnit % Test,
    "org.scalameta" %%% "munit" % mUnit % Test
  ))
  .settings(algebraSettings: _*)
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .settings(sourceGenerators in Compile += (sourceManaged in Compile).map(Boilerplate.gen).taskValue)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val laws = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .enablePlugins(MimaPlugin)
  .dependsOn(core)
  .settings(
    moduleName := "algebra-laws",
    mimaPreviousArtifacts :=
    Set("org.typelevel" %% "algebra-laws" % binaryCompatibleVersion),
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-kernel-laws" % catsVersion,
      "org.typelevel" %%% "discipline-munit" % disciplineMUnit % Test,
      "org.scalameta" %%% "munit" % mUnit % Test
  ))
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))
  .settings(algebraSettings: _*)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

lazy val benchmark = project.in(file("benchmark"))
  .settings(
    moduleName := "algebra-benchmark",
    coverageExcludedPackages := "com\\.twitter\\.algebird\\.benchmark.*")
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
  publishArtifact in Test := false,
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
    checkSnapshotDependencies,
    inquireVersions,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
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
    (unmanagedSourceDirectories in sc) ++= {
      (unmanagedSourceDirectories in sc ).value.map {
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
