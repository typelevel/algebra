import sbt._, Keys._

object SbtAdditions {
  def scalaPartV = Def setting (CrossVersion partialVersion scalaVersion.value)

  implicit final class AnyWithIfScala11Plus[A](val _o: A) {
    def ifScala210 = Def setting (scalaPartV.value collect { case (2, y) if y == 10 => _o })
  }

  implicit final class ModuleIdWithCompilerPlugin(val __x: ModuleID) {
    def compilerPlugin = sbt.compilerPlugin(__x)
  }
}
