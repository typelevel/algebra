package algebra

import scala.Specializable._

package object spec {
  type _miniboxed = miniboxed
}

package spec {
  class _specialized(group: SpecializedGroup) extends scala.annotation.StaticAnnotation {
    def this(types: Specializable*) = this(new scala.Specializable.Group(types.toList))
    def this() = this(Primitives)
  }
}
