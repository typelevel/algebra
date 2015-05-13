import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import sbtrelease.ReleaseStep
import sbtrelease.ReleasePlugin.ReleaseKeys.releaseProcess
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._
import sbtunidoc.Plugin.UnidocKeys._

lazy val buildSettings = Seq(
  organization := "org.spire-math",
  scalaVersion := "2.11.5",
  crossScalaVersions := Seq("2.10.4", "2.11.5")
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
  )
)

lazy val algebraSettings = buildSettings ++ commonSettings ++ publishSettings ++ releaseSettings

lazy val docSettings = Seq(
  autoAPIMappings := true,
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(core, std, laws),
  site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
  git.remoteRepo := "git@github.com:non/algebra.git"
)

lazy val aggregate = project.in(file("."))
  .settings(algebraSettings: _*)
  .settings(noPublishSettings: _*)
  .settings(unidocSettings: _*)
  .settings(site.settings: _*)
  .settings(ghpages.settings: _*)
  .settings(docSettings: _*)
  .aggregate(core, laws, std)
  .dependsOn(core, laws, std)

lazy val core = project
  .settings(moduleName := "algebra")
  .settings(algebraSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.mpilquist" %% "simulacrum" % "0.3.0",
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)
    )
  )

lazy val std = project.dependsOn(core)
  .settings(moduleName := "algebra-std")
  .settings(algebraSettings: _*)

lazy val laws = project.dependsOn(core, std)
  .settings(moduleName := "algebra-laws")
  .settings(algebraSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.11.3",
      "org.typelevel" %% "discipline" % "0.2.1",
      "org.scalatest" %% "scalatest" % "2.1.3" % "test"
    )
  )

lazy val publishSettings = Seq(
  homepage := Some(url("http://spire-math.org")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  autoAPIMappings := true,
  apiURL := Some(url("https://non.github.io/algebra/api/")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"

    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <scm>
      <url>git@github.com:non/algebra.git</url>
      <connection>scm:git:git@github.com:non/algebra.git</connection>
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
    publishSignedArtifacts,
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)

lazy val publishSignedArtifacts = ReleaseStep(
  action = { st =>
    val extracted = st.extract
    val ref = extracted.get(thisProjectRef)
    extracted.runAggregated(publishSigned in Global in ref, st)
  },
  check = { st =>
    // getPublishTo fails if no publish repository is set up.
    val ex = st.extract
    val ref = ex.get(thisProjectRef)
    Classpaths.getPublishTo(ex.get(publishTo in Global in ref))
    st
  },
  enableCrossBuild = true
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

addCommandAlias("validate", ";compile;test;unidoc")
