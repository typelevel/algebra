package algebra
package std

import algebra.lattice.Lattice
import algebra.ring.Semiring

package object set extends SetInstances

trait SetInstances {
  implicit def setLattice[A]: Lattice[Set[A]] = new SetLattice[A]
  implicit def setPartialOrder[A]: PartialOrder[Set[A]] = new SetPartialOrder[A]
  implicit def setSemiring[A] = new SetSemiring[A]
}

class SetLattice[A] extends Lattice[Set[A]] {
  def join(lhs: Set[A], rhs: Set[A]): Set[A] = lhs.union(rhs)

  def meet(lhs: Set[A], rhs: Set[A]): Set[A] = lhs.intersect(rhs)
}

class SetPartialOrder[A] extends PartialOrder[Set[A]] {
  def partialCompare(x: Set[A], y: Set[A]): Double =
    if (x.size < y.size) if (x.subsetOf(y)) -1.0 else Double.NaN
    else if (y.size < x.size) -partialCompare(y, x)
    else if (x == y) 0.0
    else Double.NaN

  override def eqv(x: Set[A], y: Set[A]): Boolean = x == y
}

class SetSemiring[A] extends Semiring[Set[A]] {
  def zero: Set[A] = Set.empty
  def plus(x: Set[A], y: Set[A]): Set[A] = x | y
  def times(x: Set[A], y: Set[A]): Set[A] = x & y
}
