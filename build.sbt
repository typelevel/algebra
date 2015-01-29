// basic project info

organization in ThisBuild := "org.spire-math"
homepage in ThisBuild := Some(url("http://spire-math.org"))
licenses in ThisBuild := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

// build information

scalaVersion in ThisBuild := "2.11.4"
crossScalaVersions in ThisBuild := Seq("2.10.4", "2.11.4")

scalacOptions in ThisBuild ++= (
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

// publishing/release details follow

publishMavenStyle in ThisBuild := true
publishArtifact in ThisBuild in Test := false
pomIncludeRepository in ThisBuild := { _ => false }

publishTo in ThisBuild <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra in ThisBuild := (
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
)
