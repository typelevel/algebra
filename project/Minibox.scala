import sbt._
import sbt.Keys._

object Minibox {
  val version = "0.4-SNAPSHOT"
  val miniboxing = "org.scala-miniboxing.plugins" %% "miniboxing-runtime" % version

  def miniboxed(proj: Project) = Seq(
    libraryDependencies += miniboxing,
    addCompilerPlugin("org.scala-miniboxing.plugins" %% "miniboxing-plugin" % version),
    unmanagedSourceDirectories in Compile ++= {
      val projSourceDir = (sourceDirectory in (proj, Compile)).value
      val projSpecDir = projSourceDir / "scala_spec"
      val projUnmanagedSourceDirectories =
        (unmanagedSourceDirectories in (proj, Compile)).value
      (projUnmanagedSourceDirectories --- projSpecDir).get
    },
    unmanagedSourceDirectories in Compile +=
      (sourceDirectory in Compile).value / s"scala_spec"
  )

  def specDirectory =
    unmanagedSourceDirectories in Compile +=
      (sourceDirectory in Compile).value / s"scala_spec"
}
