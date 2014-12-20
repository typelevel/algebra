package algebra
package std

object set {

  implicit def setEq[A]: Eq[Set[A]] = new SetEq[A]

  class SetEq[A] extends Eq[Set[A]] {
    def eqv(x: Set[A], y: Set[A]): Boolean = x == y
  }

  implicit def setMonoid[A] = new SetMonoid[A]

  class SetMonoid[A] extends CommutativeMonoid[Set[A]] {
    def empty: Set[A] = Set.empty
    def combine(x: Set[A], y: Set[A]): Set[A] = x | y
  }
}
