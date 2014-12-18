package algebra
package lattice

import scala.{specialized => sp}

trait BoundedLattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with Lattice[A] with BoundedMeetSemilattice[A] with BoundedJoinSemilattice[A]

trait BoundedLatticeFunctions {
  def zero[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedJoinSemilattice[A]): A =
    ev.zero

  def one[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedMeetSemilattice[A]): A =
    ev.one
}

object BoundedLattice extends LatticeFunctions with BoundedLatticeFunctions {

  /**
   * Access an implicit `BoundedLattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedLattice[A]): BoundedLattice[A] = ev
}
