package algebra
package std

import algebra.ring.Semiring

object set {

  implicit def setEq[A]: Eq[Set[A]] = new SetEq[A]

  class SetEq[A] extends Eq[Set[A]] {
    def eqv(x: Set[A], y: Set[A]): Boolean = x == y
  }

  implicit def setSemiring[A] = new SetSemiring[A]

  class SetSemiring[A] extends Semiring[Set[A]] {
    def zero: Set[A] = Set.empty
    def plus(x: Set[A], y: Set[A]): Set[A] = x | y
    def times(x: Set[A], y: Set[A]): Set[A] = x & y
  }
}
