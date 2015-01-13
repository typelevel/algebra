import sbt._
import sbt.Keys._

import com.typesafe.sbt.pgp.PgpKeys._
 
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._
 
object AlgebraBuild extends Build {

  lazy val core =
    Project("core", file("core")).settings(algebraSettings: _*)

  lazy val std =
    Project("std", file("std")).settings(algebraSettings: _*).dependsOn(core)

  lazy val laws =
    Project("laws", file("laws")).settings(algebraSettings: _*).dependsOn(core, std)

  lazy val aggregate =
    Project("aggregate", file(".")).settings(aggregateSettings: _*).aggregate(core, std, laws)

  lazy val aggregateSettings = algebraSettings ++ noPublishSettings

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
}
