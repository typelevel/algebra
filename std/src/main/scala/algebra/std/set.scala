package algebra
package std

import algebra.ring.Semiring

package object set extends SetInstances

trait SetInstances {
  implicit def setPartialOrder[A]: PartialOrder[Set[A]] = new SetPartialOrder[A]
  implicit def setSemiring[A] = new SetSemiring[A]
}

class SetPartialOrder[A] extends PartialOrder[Set[A]] {
  def partialCompare(x: Set[A], y: Set[A]): Double =
    if (x == y) 0.0
    else if (x.subsetOf(y)) -1.0
    else 1.0
}

class SetSemiring[A] extends Semiring[Set[A]] {
  def zero: Set[A] = Set.empty
  def plus(x: Set[A], y: Set[A]): Set[A] = x | y
  def times(x: Set[A], y: Set[A]): Set[A] = x & y
}
