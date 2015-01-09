package algebra
package lattice

trait BoundedMeetSemilattice[@mb @sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with MeetSemilattice[A] { self =>
  def one: A
  def isOne(a: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, one)

  override def meetSemilattice: BoundedSemilattice[A] =
    new BoundedSemilattice[A] {
      def empty: A = self.one
      def combine(x: A, y: A): A = meet(x, y)
    }
}

object BoundedMeetSemilattice {

  /**
   * Access an implicit `BoundedMeetSemilattice[A]`.
   */
  @inline final def apply[@mb @sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedMeetSemilattice[A]): BoundedMeetSemilattice[A] = ev
}
