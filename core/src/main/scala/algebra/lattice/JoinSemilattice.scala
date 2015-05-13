package algebra
package lattice

import scala.{specialized => sp}

import simulacrum._

/**
 * A join-semilattice (or upper semilattice) is a semilattice whose
 * operation is called "join", and which can be thought of as a least
 * upper bound.
 */
@typeclass trait JoinSemilattice[@sp(Int, Long, Float, Double) A] extends Any with Serializable { self =>
  def join(lhs: A, rhs: A): A

  @noop def joinSemilattice: Semilattice[A] =
    new Semilattice[A] {
      def combine(x: A, y: A): A = self.join(x, y)
    }

  @noop def joinPartialOrder(implicit ev: Eq[A]): PartialOrder[A] =
    joinSemilattice.asJoinPartialOrder
}
