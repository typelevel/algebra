package algebra
package ring

import scala.{specialized => sp}
import scala.annotation.tailrec

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

  /**
   * Convert the given BigInt to an instance of A.
   *
   * This is equivalent to `n` repeated summations of this ring's `one`, or
   * `-n` summations of `-one` if `n` is negative.
   *
   * Most type class instances should consider overriding this method for
   * performance reasons.
   */
  def fromBigInt(n: BigInt): A =
    if (n.isValidInt) {
      fromInt(n.toInt)
    } else {
      val d = fromInt(1 << 30)
      val mask = (1L << 30) - 1
      @tailrec def loop(k: A, x: BigInt, acc: A): A =
        if (x.isValidInt) {
          plus(times(k, fromInt(x.toInt)), acc)
        } else {
          val y = x >> 30
          val r = fromInt((x & mask).toInt)
          loop(times(d, k), y, plus(times(k, r), acc))
        }

      val absValue = loop(one, n.abs, zero)
      if (n.signum < 0) negate(absValue) else absValue
    }
}

trait RingFunctions[R[T] <: Ring[T]] extends AdditiveGroupFunctions[R] with MultiplicativeMonoidFunctions[R] {
  def fromInt[@sp(Int, Long, Float, Double) A](n: Int)(implicit ev: R[A]): A =
    ev.fromInt(n)

  def fromBigInt[@sp(Int, Long, Float, Double) A](n: Int)(implicit ev: R[A]): A =
    ev.fromBigInt(n)
}

object Ring extends RingFunctions[Ring] {
  @inline final def apply[A](implicit ev: Ring[A]): Ring[A] = ev
}
