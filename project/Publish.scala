import sbt._
import sbt.Keys._

import com.typesafe.sbt.pgp.PgpKeys._

import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._

import scala.xml.{ Node, NodeSeq }
import scala.xml.transform.{ RewriteRule, RuleTransformer }

object Publish {
  lazy val settings =
    releaseSettings ++
    Seq(
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
      ),
      pomDependencyExclusions <<= pomDependencyExclusions ?? Seq(),
      pomPostProcess := { (node: Node) =>
        val exclusions = pomDependencyExclusions.value.toSet
        val rule = new RewriteRule {
          override def transform(n: Node): NodeSeq = {
            if (n.label == "dependency") {
              val groupId = (n \ "groupId").text
              val artifactId = (n \ "artifactId").text
              if (exclusions.contains(groupId -> artifactId)) {
                NodeSeq.Empty
              } else {
                n
              }
            } else {
              n
            }
          }
        }
        new RuleTransformer(rule).transform(node)(0)
      }
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

  val pomDependencyExclusions =
    SettingKey[Seq[(String, String)]](
      "pom-dependency-exclusions",
      "Group ID / Artifact ID dependencies to exclude from the maven POM."
    )
}
