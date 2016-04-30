package algebra
package std

import algebra.lattice.GenBool
import algebra.ring.{BoolRng, Semiring}

package object set extends SetInstances

trait SetInstances extends cats.kernel.std.SetInstances {
  implicit def setLattice[A]: GenBool[Set[A]] = new SetLattice[A]
  implicit def setSemiring[A] = new SetSemiring[A]
  def setBoolRng[A] = new SetBoolRng[A]
}

class SetLattice[A] extends GenBool[Set[A]] {
  def zero: Set[A] = Set.empty[A]
  def or(lhs: Set[A], rhs: Set[A]): Set[A] = lhs.union(rhs)
  def and(lhs: Set[A], rhs: Set[A]): Set[A] = lhs.intersect(rhs)
  def without(lhs: Set[A], rhs: Set[A]): Set[A] = lhs.diff(rhs)
}

class SetSemiring[A] extends Semiring[Set[A]] {
  def zero: Set[A] = Set.empty
  def plus(x: Set[A], y: Set[A]): Set[A] = x | y
  def times(x: Set[A], y: Set[A]): Set[A] = x & y
}

class SetBoolRng[A] extends BoolRng[Set[A]] {
  def zero: Set[A] = Set.empty
  def plus(x: Set[A], y: Set[A]): Set[A] = (x diff y) | (y diff x)
  def times(x: Set[A], y: Set[A]): Set[A] = x & y
}
