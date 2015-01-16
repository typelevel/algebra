package algebra
package ring

import scala.{specialized => sp}

/**
 * Ring consists of:
 * 
 *  - a commutative group for addition (+)
 *  - a monoid for multiplication (*)
 * 
 * Additionally, multiplication must distribute over addition.
 * 
 * Ring implements some methods (for example fromInt) in terms of
 * other more fundamental methods (zero, one and plus). Where
 * possible, these methods should be overridden by more efficient
 * implementations.
 */
trait Ring[@sp(Int, Long, Float, Double) A] extends Any with Rig[A] with Rng[A] {

  /**
   * Convert the given integer to an instance of A.
   * 
   * Defined to be equivalent to `sumN(one, n)`.
   * 
   * That is, `n` repeated summations of this ring's `one`, or `-n`
   * summations of `-one` if `n` is negative.
   * 
   * Most type class instances should consider overriding this method
   * for performance reasons.
   */
  def fromInt(n: Int): A = sumN(one, n)
}

trait RingFunctions {
  def fromInt[@sp(Int, Long, Float, Double) A](n: Int)(implicit ev: Ring[A]): A =
    ev.fromInt(n)
}

object Ring extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit ev: Ring[A]): Ring[A] = ev
}
