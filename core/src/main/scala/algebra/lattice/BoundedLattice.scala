package algebra
package lattice

import scala.{specialized => sp}

import simulacrum._

/**
 * A bounded lattice is a lattice that additionally has one element
 * that is the bottom (zero, also written as ⊥), and one element that
 * is the top (one, also written as ⊤).
 * 
 * This means that for any a in A:
 * 
 *   join(zero, a) = a = meet(one, a)
 * 
 * Or written using traditional notation:
 * 
 *   (0 ∨ a) = a = (1 ∧ a)
 */
@typeclass trait BoundedLattice[@sp(Int, Long, Float, Double) A] extends Any with Lattice[A] with BoundedMeetSemilattice[A] with BoundedJoinSemilattice[A]

trait BoundedLatticeFunctions {
  def zero[@sp(Int, Long, Float, Double) A](implicit ev: BoundedJoinSemilattice[A]): A =
    ev.zero

  def one[@sp(Int, Long, Float, Double) A](implicit ev: BoundedMeetSemilattice[A]): A =
    ev.one
}

object BoundedLattice extends LatticeFunctions with BoundedLatticeFunctions
