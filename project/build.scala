import sbt._
import sbt.Keys._

// import sbtunidoc.Plugin._
// import sbtunidoc.Plugin.UnidocKeys._
// 
// import com.typesafe.sbt.pgp.PgpKeys._
// 
// import sbtrelease._
import sbtrelease.ReleasePlugin._
// import sbtrelease.ReleasePlugin.ReleaseKeys._
// import sbtrelease.ReleaseStateTransformations._
// import sbtrelease.Utilities._
// 
// import sbtbuildinfo.Plugin._

object AlgebraBuild extends Build {

  override lazy val settings = super.settings ++ Seq(
    organization := "org.spire-math",
    scalaVersion := "2.10.2",
    crossScalaVersions := Seq("2.9.3", "2.10.2"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-optimize"
    )
  )


  lazy val algebraSettings = Defaults.defaultSettings ++ releaseSettings

  lazy val spire = Project("algebra", file(".")).
    settings(algebraSettings: _*)
}
