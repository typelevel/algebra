// basic project info

organization := "org.spire-math"
homepage := Some(url("http://spire-math.org"))
licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

// build information

scalaVersion := "2.11.4"
crossScalaVersions := Seq("2.10.4", "2.11.4")

scalacOptions ++= (
  "-deprecation" ::           
  "-encoding" :: "UTF-8" ::
  "-feature" ::                
  "-language:existentials" ::
  "-language:higherKinds" ::
  "-language:implicitConversions" ::
  "-unchecked" ::
  "-Xfatal-warnings" ::       
  "-Xlint" ::
  "-Yno-adapted-args" ::       
  "-Ywarn-dead-code" ::
  "-Ywarn-numeric-widen" ::   
  //"-Ywarn-value-discard" :: // fails with @sp on Unit
  "-Xfuture" ::
  Nil
)

// extra repositories (for miniboxing)

resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")

// publish pre-amble

Publish.preamble

// projects

lazy val aggregate = project.
  in(file(".")).
  aggregate(core, std, laws, coreMinibox, stdMinibox).
  settings(Publish.noPublishSettings: _*)

lazy val core = project.
  in(file("core")).
  settings(Minibox.specDirectory)

lazy val std = project.
  in(file("std")).
  dependsOn(core)

lazy val laws = project.
  in(file("laws")).
  dependsOn(core, std)

lazy val coreMinibox = project.
  in(file("core-minibox")).
  settings(Minibox.miniboxed(core): _*)

lazy val stdMinibox = project.
  in(file("std-minibox")).
  dependsOn(coreMinibox).
  settings(Minibox.miniboxed(core): _*)
