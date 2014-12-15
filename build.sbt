// basic project info

name := "algebra"
organization := "org.spire-math"
homepage := Some(url("http://spire-math.org"))
licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))


// build information

scalaVersion := "2.11.4"
crossScalaVersions := Seq("2.9.3", "2.10.4", "2.11.4")

scalacOptions ++= (
  "-deprecation" ::
  "-unchecked" ::
  "-optimize" ::
  Nil
)


// publishing/release details follow

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
<scm>
  <url>git@github.com:non/algebra.git</url>
  <connection>scm:git:git@github.com:non/algebra.git</connection>
</scm>
<developers>
  <developer>
    <id>d_m</id>
    <name>Erik Osheim</name>
    <url>http://github.com/non/</url>
  </developer>
</developers>
)
