package algebra
package number

import algebra.ring._

import scala.{ specialized => sp }

/**
 * A trait for things that have some notion of sign and the ability to
 * ensure something has a non-negative sign.
 */
trait Signed[@sp(Int, Long, Float, Double) A] extends Any {

  /**
   * Return a's sign:
   * - Zero if `a` is 0,
   * - Positive if `a` is positive
   * - Negative is `a` is negative.
   */
  def sign(a: A): Sign = Sign(signum(a))

  /**
   * Return a's sign encoded as an Int (n) where:
   * - n = 0 if `a` is 0
   * - n > 0 if `a` is positive
   * - n < 0 is `a` is negative.
   */
  def signum(a: A): Int

  /** An idempotent function that ensures an object has a non-negative sign. */
  def abs(a: A): A

  def isSignPositive(a: A): Boolean = signum(a) > 0
  def isSignZero(a: A): Boolean = signum(a) == 0
  def isSignNegative(a: A): Boolean = signum(a) < 0

  def isSignNonPositive(a: A): Boolean = signum(a) <= 0
  def isSignNonZero(a: A): Boolean = signum(a) != 0
  def isSignNonNegative(a: A): Boolean = signum(a) >= 0
}

trait SignedFunctions {
  def sign[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Sign =
    ev.sign(a)
  def signum[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Int =
    ev.signum(a)
  def abs[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): A =
    ev.abs(a)

  def isSignPositive[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isSignPositive(a)
  def isSignZero[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isSignZero(a)
  def isSignNegative[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isSignNegative(a)

  def isSignNonPositive[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isSignNonPositive(a)
  def isSignNonZero[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isSignNonZero(a)
  def isSignNonNegative[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isSignNonNegative(a)
}

object Signed extends SignedFunctions {
  def apply[A](implicit ev: Signed[A]): Signed[A] = ev

  implicit def orderedAdditiveGroupIsSigned[A](implicit o: Order[A], g: AdditiveGroup[A]): Signed[A] =
    new Signed[A] {
      def signum(a: A) = o.compare(a, g.zero)
      def abs(a: A) = if (o.lt(a, g.zero)) g.negate(a) else a
    }
}
