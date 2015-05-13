package algebra
package lattice

import scala.{specialized => sp}

import simulacrum._

@typeclass trait BoundedMeetSemilattice[@sp(Int, Long, Float, Double) A] extends Any with MeetSemilattice[A] { self =>
  @noop def one: A
  def isOne(a: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, one)

  @noop override def meetSemilattice: BoundedSemilattice[A] =
    new BoundedSemilattice[A] {
      def empty: A = self.one
      def combine(x: A, y: A): A = meet(x, y)
    }
}