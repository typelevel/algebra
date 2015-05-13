package algebra
package lattice

import scala.{specialized => sp}

import simulacrum._

@typeclass trait BoundedJoinSemilattice[@sp(Int, Long, Float, Double) A] extends Any with JoinSemilattice[A] { self =>
  @noop def zero: A
  def isZero(a: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, zero)

  @noop override def joinSemilattice: BoundedSemilattice[A] =
    new BoundedSemilattice[A] {
      def empty: A = self.zero
      def combine(x: A, y: A): A = join(x, y)
    }
}
