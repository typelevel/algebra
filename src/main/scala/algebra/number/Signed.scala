package algebra
package number

import algebra.ring._

import scala.{ specialized => sp }

/**
 * A trait for things that have some notion of sign and the ability to ensure
 * something has a positive sign.
 */
trait Signed[@sp(Double, Float, Int, Long) A] {
  /** Returns Zero if `a` is 0, Positive if `a` is positive, and Negative is `a` is negative. */
  def sign(a: A): Sign = Sign(signum(a))

  /** Returns 0 if `a` is 0, > 0 if `a` is positive, and < 0 is `a` is negative. */
  def signum(a: A): Int

  /** An idempotent function that ensures an object has a non-negative sign. */
  def abs(a: A): A

  def isZero(a: A): Boolean = signum(a) == 0
}

trait SignedFunctions {
  def sign[@sp(Double, Float, Int, Long) A](a: A)(implicit ev: Signed[A]): Sign =
    ev.sign(a)
  def signum[@sp(Double, Float, Int, Long) A](a: A)(implicit ev: Signed[A]): Int =
    ev.signum(a)
  def abs[@sp(Double, Float, Int, Long) A](a: A)(implicit ev: Signed[A]): A =
    ev.abs(a)
  def isZero[@sp(Double, Float, Int, Long) A](a: A)(implicit ev: Signed[A]): Boolean =
    ev.isZero(a)
}

object Signed extends SignedFunctions {
  implicit def orderedRingIsSigned[A: Order: Ring]: Signed[A] = new OrderedRingIsSigned[A]
  def apply[A](implicit s: Signed[A]): Signed[A] = s
}

private[algebra] class OrderedRingIsSigned[A](implicit o: Order[A], r: Ring[A]) extends Signed[A] {
  def signum(a: A) = o.compare(a, r.zero)
  def abs(a: A) = if (signum(a) < 0) r.negate(a) else a
}
