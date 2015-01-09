package algebra

/**
 * CommutativeSemigroup represents a commutative semigroup.
 * 
 * A semigroup is commutative if for all x and y, x |+| y === y |+| x.
 */
trait CommutativeSemigroup[@mb @sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with Semigroup[A] {

  /**
   * CommutativeSemigroup is guaranteed to be commutative.
   */
  override def isCommutative: Boolean = true
}

object CommutativeSemigroup extends SemigroupFunctions {

  /**
   * Access an implicit `CommutativeSemigroup[A]`.
   */
  @inline final def apply[A](implicit ev: CommutativeSemigroup[A]): CommutativeSemigroup[A] = ev

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
