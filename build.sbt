import sbtrelease.Utilities._
import sbtunidoc.Plugin.UnidocKeys._
import ReleaseTransformations._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import com.typesafe.tools.mima.plugin.MimaPlugin.mimaDefaultSettings

lazy val scalaCheckVersion = "1.13.4"
lazy val scalaTestVersion = "3.0.1"
lazy val disciplineVersion = "0.7.2"
lazy val catsVersion = "0.9.0"
lazy val catalystsVersion = "0.0.5"

lazy val buildSettings = Seq(
  organization := "org.typelevel",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1")
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
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    //"-Ywarn-value-discard", // fails with @sp on Unit
    "-Xfuture"
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) => Seq.empty
    case _ => Seq("-Ywarn-unused-import")
  }),
  resolvers += Resolver.sonatypeRepo("public"),
  scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import")),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  scalaJSStage in Global := FastOptStage,
  requiresDOM := false,
  jsEnv := NodeJSEnv().value,
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
  micrositeHomepage := "https://typelevel.org/algebra",
  micrositeBaseUrl := "algebra",
  micrositeDocumentationUrl := "api",
  micrositeGithubOwner := "typelevel",
  micrositeExtraMdFiles := Map(file("CONTRIBUTING.md") -> "contributing.md"),
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
  .enablePlugins(MicrositesPlugin)
  .settings(moduleName := "algebra-docs")
  .settings(algebraSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(unidocSettings)
  .settings(ghpages.settings)
  .settings(docSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .dependsOn(coreJVM, lawsJVM)

lazy val aggregate = project.in(file("."))
  .settings(algebraSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(unidocSettings: _*)
  .settings(ghpages.settings: _*)
  .settings(docSettings: _*)
  .aggregate(coreJVM, lawsJVM, benchmark)
  .dependsOn(coreJVM, lawsJVM)
  .aggregate(coreJS, lawsJS)
  .dependsOn(coreJS, lawsJS)

val binaryCompatibleVersion = "0.6.0"

/**
  * Empty this each time we publish a new version (and bump the minor number)
  */
val ignoredABIProblems = {
  import com.typesafe.tools.mima.core._
  import com.typesafe.tools.mima.core.ProblemFilters._
  Seq(
    exclude[ReversedMissingMethodProblem]("algebra.ring.RingFunctions.defaultFromDouble")
  )
}

lazy val core = crossProject
  .crossType(CrossType.Pure)
  .settings(moduleName := "algebra")
  .settings(mimaDefaultSettings: _*)
  .settings(
    mimaPreviousArtifacts := Set("org.typelevel" %% "algebra" % binaryCompatibleVersion),
    mimaBinaryIssueFilters ++= ignoredABIProblems
  )
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-kernel" % catsVersion,
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion % "test",
    "org.scalatest" %%% "scalatest" % scalaTestVersion % "test"))
  .settings(algebraSettings: _*)
  .settings(sourceGenerators in Compile += (sourceManaged in Compile).map(Boilerplate.gen).taskValue)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val laws = crossProject
  .crossType(CrossType.Pure)
  .dependsOn(core)
  .settings(moduleName := "algebra-laws")
  .settings(mimaDefaultSettings: _*)
  .settings(mimaPreviousArtifacts :=
    Set("org.typelevel" %% "algebra-laws" % binaryCompatibleVersion))
  .settings(algebraSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-kernel-laws" % catsVersion,
    "org.scalacheck" %%% "scalacheck" % scalaCheckVersion,
    "org.typelevel" %%% "discipline" % disciplineVersion,
    "org.typelevel" %%% "catalysts-platform" % catalystsVersion % "test",
    "org.typelevel" %%% "catalysts-macros" % catalystsVersion % "test",
    "org.scalatest" %%% "scalatest" % scalaTestVersion % "test"))

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

lazy val benchmark = project.in(file("benchmark"))
  .settings(
    moduleName := "algebra-benchmark",
    coverageExcludedPackages := "com\\.twitter\\.algebird\\.benchmark.*")
  .enablePlugins(JmhPlugin)
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
    <scm>
      <url>git@github.com:typelevel/algebra.git</url>
      <connection>scm:git:git@github.com:typelevel/algebra.git</connection>
    </scm>
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
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges
  )
)

addCommandAlias("validate", ";compile;test;unidoc")

// Base Build Settings

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
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

lazy val scalaMacroDependencies: Seq[Setting[_]] = Seq(
  libraryDependencies += scalaOrganization.value % "scala-reflect" % scalaVersion.value % "provided",
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      // if scala 2.11+ is used, quasiquotes are merged into scala-reflect
      case Some((2, scalaMajor)) if scalaMajor >= 11 => Seq()
      // in Scala 2.10, quasiquotes are provided by macro paradise
      case Some((2, 10)) =>
        Seq(
          compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.patch),
              "org.scalamacros" %% "quasiquotes" % "2.1.0-M5" cross CrossVersion.binary
        )
    }
  }
)

addCommandAlias("gitSnapshots", ";set version in ThisBuild := git.gitDescribedVersion.value.get + \"-SNAPSHOT\"")

// For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
credentials ++= (for {
  username <- Option(System.getenv().get("SONATYPE_USERNAME"))
  password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
