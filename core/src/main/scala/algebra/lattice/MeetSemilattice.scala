package algebra
package lattice

import scala.{specialized => sp}

import simulacrum._

/**
 * A meet-semilattice (or lower semilattice) is a semilattice whose
 * operation is called "meet", and which can be thought of as a
 * greatest lower bound.
 */
@typeclass trait MeetSemilattice[@sp(Int, Long, Float, Double) A] extends Any with Serializable { self =>
  def meet(lhs: A, rhs: A): A

  @noop def meetSemilattice: Semilattice[A] =
    new Semilattice[A] {
      def combine(x: A, y: A): A = self.meet(x, y)
    }

  @noop def meetPartialOrder(implicit ev: Eq[A]): PartialOrder[A] =
    meetSemilattice.asMeetPartialOrder
}
