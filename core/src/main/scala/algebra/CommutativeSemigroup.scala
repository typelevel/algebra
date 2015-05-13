package algebra

import scala.{ specialized => sp }

import simulacrum._

/**
 * CommutativeSemigroup represents a commutative semigroup.
 *
 * A semigroup is commutative if for all x and y, x |+| y === y |+| x.
 */
@typeclass trait CommutativeSemigroup[@sp(Int, Long, Float, Double) A] extends Any with Semigroup[A]

object CommutativeSemigroup extends SemigroupFunctions {

  /**
   * This method converts an additive instance into a generic
   * instance.
   *
   * Given an implicit `AdditiveCommutativeSemigroup[A]`, this method
   * returns a `CommutativeSemigroup[A]`.
   */
  @inline final def additive[A](implicit ev: ring.AdditiveCommutativeSemigroup[A]): CommutativeSemigroup[A] =
    ev.additive

  /**
   * This method converts a multiplicative instance into a generic
   * instance.
   *
   * Given an implicit `MultiplicativeCommutativeSemigroup[A]`, this
   * method returns a `CommutativeSemigroup[A]`.
   */
  @inline final def multiplicative[A](implicit ev: ring.MultiplicativeCommutativeSemigroup[A]): CommutativeSemigroup[A] =
    ev.multiplicative
}
