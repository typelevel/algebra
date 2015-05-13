package algebra

import lattice.{ BoundedMeetSemilattice, BoundedJoinSemilattice }

import scala.{specialized => sp}

import simulacrum._

@typeclass trait BoundedSemilattice[@sp(Int, Long, Float, Double) A] extends Any with Semilattice[A] with CommutativeMonoid[A] { self =>

  @noop override def asMeetSemilattice: BoundedMeetSemilattice[A] =
    new BoundedMeetSemilattice[A] {
      def one: A = self.empty
      def meet(x: A, y: A): A = self.combine(x, y)
    }

  @noop override def asJoinSemilattice: BoundedJoinSemilattice[A] =
    new BoundedJoinSemilattice[A] {
      def zero: A = self.empty
      def join(x: A, y: A): A = self.combine(x, y)
    }
}
