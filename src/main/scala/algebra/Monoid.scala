package algebra

import scala.{ specialized => sp }

/**
 * A monoid is a semigroup with an identity. A monoid is a specialization of a
 * semigroup, so its operation must be associative. Additionally,
 * `op(x, id) == op(id, x) == x`. For example, if we have `Monoid[String]`,
 * with `op` as string concatenation, then `id = ""`.
 */
trait Monoid[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Semigroup[A] {
  def id: A

  /**
   * Return `a` appended to itself `n` times.
   */
  override def sumn(a: A, n: Int): A =
    if (n < 0) throw new IllegalArgumentException("Repeated summation for monoids must have reptitions >= 0")
    else if (n == 0) id
    else positiveSumn(a, n)

  /**
   * Given a sequence of `as`, sum them using the monoid and return the total.
   */
  def sum(as: TraversableOnce[A]): A = as.foldLeft(id)(op)
}

trait MonoidFunctions extends SemigroupFunctions {
  def id[A](implicit ev: Monoid[A]): A = ev.id

  def sum[A](as: TraversableOnce[A])(implicit ev: Monoid[A]): A = ev.sum(as)
}

object Monoid extends MonoidFunctions {
  @inline final def apply[A](implicit m: Monoid[A]): Monoid[A] = m
}
