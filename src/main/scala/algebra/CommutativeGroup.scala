package algebra

import scala.{ specialized => sp }

/**
 * An abelian group is a group whose operation is commutative.
 */
trait CommutativeGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends Any with Group[A] with CommutativeMonoid[A]

object CommutativeGroup extends GroupFunctions {

  /**
   * Access an implicit `CommutativeGroup[A]`.
   */
  @inline final def apply[A](implicit ev: CommutativeGroup[A]): CommutativeGroup[A] = ev

  /**
   * This method converts an additive instance into a generic
   * instance.
   * 
   * Given an implicit `AdditiveCommutativeGroup[A]`, this method
   * returns a `CommutativeGroup[A]`.
   */
  @inline final def additive[A](implicit ev: ring.AdditiveCommutativeGroup[A]): CommutativeGroup[A] =
    ev.additive

  /**
   * This method converts a multiplicative instance into a generic
   * instance.
   * 
   * Given an implicit `MultiplicativeCommutativeGroup[A]`, this
   * method returns a `CommutativeGroup[A]`.
   */
  @inline final def multiplicative[A](implicit ev: ring.MultiplicativeCommutativeGroup[A]): CommutativeGroup[A] =
    ev.multiplicative
}
