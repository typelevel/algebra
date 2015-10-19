package algebra
package lattice

import scala.{specialized => sp}
import algebra.ring.CommutativeRig

/**
 * A bounded distributive lattice is a lattice that both bounded and distributive
 */
trait BoundedDistributiveLattice[@sp(Int, Long, Float, Double) A] extends Any with BoundedLattice[A] with DistributiveLattice[A] { self =>
  /**
   * Return a CommutativeRig using join and meet. Note this must obey the commutative rig laws since
   * meet(a, one) = a, and meet and join are associative, commutative and distributive.
   */
  def asCommutativeRig: CommutativeRig[A] =
    new CommutativeRig[A] {
      def zero: A = self.zero
      def one: A = self.one
      def plus(x: A, y: A): A = self.join(x, y)
      def times(x: A, y: A): A = self.meet(x, y)
    }
}

object BoundedDistributiveLattice extends LatticeFunctions with BoundedLatticeFunctions {

  /**
   * Access an implicit `BoundedDistributiveLattice[A]`.
   */
  @inline final def apply[@sp(Int, Long, Float, Double) A](implicit ev: BoundedDistributiveLattice[A]): BoundedDistributiveLattice[A] = ev

  def minMax[@sp(Int, Long, Float, Double)A](min: A, max: A)(implicit ord: Order[A]): BoundedDistributiveLattice[A] =
    new BoundedDistributiveLattice[A] {
      def zero = min
      def one = max
      def join(a: A, b: A) = ord.max(a, b)
      def meet(a: A, b: A) = ord.min(a, b)
    }
}
