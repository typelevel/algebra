package algebra

import scala.{ specialized => sp }

/**
 * CommutativeSemigroup represents a commutative semigroup.
 * 
 * A semigroup is commutative if for all x and y, x |+| y === y |+| x.
 */
trait CommutativeSemigroup[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Semigroup[A] {
  override def isCommutative: Boolean = true
}

object CommutativeSemigroup extends SemigroupFunctions {
  @inline final def apply[A](implicit ev: CommutativeSemigroup[A]): CommutativeSemigroup[A] = ev
}
