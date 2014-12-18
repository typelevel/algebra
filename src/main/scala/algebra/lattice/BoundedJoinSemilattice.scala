package algebra
package lattice

import scala.{specialized => sp}

trait BoundedJoinSemilattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with JoinSemilattice[A] {
  def zero: A
  def isZero(a: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, zero)
}

object BoundedJoinSemilattice {

  /**
   * Access an implicit `BoundedJoinSemilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedJoinSemilattice[A]): BoundedJoinSemilattice[A] = ev
}
