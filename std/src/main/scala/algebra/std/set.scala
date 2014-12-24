package algebra
package std

import algebra.ring.Semiring

object set extends SetInstances

trait SetInstances {
  implicit def setEq[A]: Eq[Set[A]] = new SetEq[A]
  implicit def setSemiring[A] = new SetSemiring[A]
}

class SetEq[A] extends Eq[Set[A]] {
  def eqv(x: Set[A], y: Set[A]): Boolean = x == y
}

class SetSemiring[A] extends Semiring[Set[A]] {
  def zero: Set[A] = Set.empty
  def plus(x: Set[A], y: Set[A]): Set[A] = x | y
  def times(x: Set[A], y: Set[A]): Set[A] = x & y
}
