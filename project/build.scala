import sbt._
import sbt.Keys._

import com.typesafe.sbt.pgp.PgpKeys._
 
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._
 
object AlgebraBuild extends Build {

  override lazy val settings = super.settings ++ Seq(
    name := "algebra",
    organization := "org.spire-math",
    scalaVersion := "2.10.2",
    crossScalaVersions := Seq("2.9.3", "2.10.2"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-optimize"
    )
  )

  lazy val spire = Project("algebra", file(".")).
    settings(algebraSettings: _*)

  lazy val algebraSettings = Defaults.defaultSettings ++ releaseSettings ++ Seq(
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
    action = st => {
      val extracted = st.extract
      val ref = extracted.get(thisProjectRef)
      extracted.runAggregated(publishSigned in Global in ref, st)
    },
    check = st => {
      // getPublishTo fails if no publish repository is set up.
      val ex = st.extract
      val ref = ex.get(thisProjectRef)
      Classpaths.getPublishTo(ex.get(publishTo in Global in ref))
      st
    },
    enableCrossBuild = true
  )
}
