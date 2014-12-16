package algebra

import scala.{ specialized => sp }
import scala.annotation.{ switch, tailrec }

/**
 * A semigroup is any set `A` with an associative operation (`combine`).
 */
trait Semigroup[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] {

  /**
   * Associative operation taking which combines two values.
   */
  def combine(x: A, y: A): A

  /**
   * Whether or not this particular Semigroup is commutative.
   *
   * For guaranteed commutativity, see CommutativeSemigroup.
   */
  def isCommutative: Boolean = false

  /**
   * Return `a` combined with itself `n` times.
   */
  def combineN(a: A, n: Int): A =
    if (n <= 0) throw new IllegalArgumentException("Repeated combining for semigroups must have n > 0")
    else repeatedCombineN(a, n)

  /**
   * Return `a` combined with itself more than once.
   */
  protected[this] def repeatedCombineN(a: A, n: Int): A = {
    @tailrec def loop(b: A, k: Int, extra: A): A =
      if (k == 1) combine(b, extra) else {
        val x = if ((k & 1) == 1) combine(b, extra) else extra
        loop(combine(b, b), k >>> 1, x)
      }
    if (n == 1) a else loop(a, n - 1, a)
  }

  /**
   * Given a sequence of `as`, combine them and return the total.
   * 
   * If the sequence is empty, returns None. Otherwise, returns Some(total).
   */
  def tryCombineAll(as: TraversableOnce[A]): Option[A] =
    as.reduceOption(combine)
}

trait SemigroupFunctions {
  def combine[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: Semigroup[A]): A =
    ev.combine(x, y)

  def maybeCombine[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](ox: Option[A], y: A)(implicit ev: Semigroup[A]): A =
    ox match {
      case Some(x) => ev.combine(x, y)
      case None => y
    }

  def combineN[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](a: A, n: Int)(implicit ev: Semigroup[A]): A =
    ev.combineN(a, n)

  def tryCombineAll[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](as: TraversableOnce[A])(implicit ev: Semigroup[A]): Option[A] =
    ev.tryCombineAll(as)
}

object Semigroup extends SemigroupFunctions {
  @inline final def apply[A](implicit s: Semigroup[A]) = s
}
