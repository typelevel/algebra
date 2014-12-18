package algebra
package lattice

import scala.{specialized => sp}

trait MeetSemilattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any {
  def meet(lhs: A, rhs: A): A
}

object MeetSemilattice {

  /**
   * Access an implicit `MeetSemilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: MeetSemilattice[A]): MeetSemilattice[A] = ev
}
