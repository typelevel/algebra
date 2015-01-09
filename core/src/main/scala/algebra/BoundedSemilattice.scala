package algebra

import lattice.{ BoundedMeetSemilattice, BoundedJoinSemilattice }

trait BoundedSemilattice[@mb @sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with Semilattice[A] with CommutativeMonoid[A] { self =>

  override def asMeetSemilattice: BoundedMeetSemilattice[A] =
    new BoundedMeetSemilattice[A] {
      def one: A = self.empty
      def meet(x: A, y: A): A = self.combine(x, y)
    }

  override def asJoinSemilattice: BoundedJoinSemilattice[A] =
    new BoundedJoinSemilattice[A] {
      def zero: A = self.empty
      def join(x: A, y: A): A = self.combine(x, y)
    }

}

object BoundedSemilattice {

  /**
   * Access an implicit `BoundedSemilattice[A]`.
   */
  @inline final def apply[@mb @sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedSemilattice[A]): BoundedSemilattice[A] = ev
}
