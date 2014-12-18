package algebra
package lattice

import scala.{specialized => sp}

trait BoundedMeetSemilattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with MeetSemilattice[A] {
  def one: A
  def isOne(a: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, one)
}

object BoundedMeetSemilattice {

  /**
   * Access an implicit `BoundedMeetSemilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedMeetSemilattice[A]): BoundedMeetSemilattice[A] = ev
}
