package algebra

import scala.{ specialized => sp }

/**
 * A monoid is a semigroup with an identity. A monoid is a specialization of a
 * semigroup, so its operation must be associative. Additionally,
 * `combine(x, empty) == combine(empty, x) == x`. For example, if we have `Monoid[String]`,
 * with `combine` as string concatenation, then `empty = ""`.
 */
trait Monoid[@sp(Int, Long, Float, Double) A] extends Any with Semigroup[A] {

  /**
   * Return the identity element for this monoid.
   */
  def empty: A

  /**
   * Tests if `a` is the identity.
   */
  def isEmpty(a: A)(implicit ev: Eq[A]) = ev.eqv(a, empty)

  /**
   * Return `a` appended to itself `n` times.
   */
  override def combineN(a: A, n: Int): A =
    if (n < 0) throw new IllegalArgumentException("Repeated combining for monoids must have n >= 0")
    else if (n == 0) empty
    else repeatedCombineN(a, n)

  /**
   * Given a sequence of `as`, sum them using the monoid and return the total.
   */
  def combineAll(as: TraversableOnce[A]): A =
    as.foldLeft(empty)(combine)
}

trait MonoidFunctions extends SemigroupFunctions {
  def empty[@sp(Int, Long, Float, Double) A](implicit ev: Monoid[A]): A =
    ev.empty

  def combineAll[@sp(Int, Long, Float, Double) A](as: TraversableOnce[A])(implicit ev: Monoid[A]): A =
    ev.combineAll(as)
}

object Monoid extends MonoidFunctions {

  /**
   * Access an implicit `Monoid[A]`.
   */
  @inline final def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev

  /**
   * This method converts an additive instance into a generic
   * instance.
   * 
   * Given an implicit `AdditiveMonoid[A]`, this method returns a
   * `Monoid[A]`.
   */
  @inline final def additive[A](implicit ev: ring.AdditiveMonoid[A]): Monoid[A] =
    ev.additive

  /**
   * This method converts an multiplicative instance into a generic
   * instance.
   * 
   * Given an implicit `MultiplicativeMonoid[A]`, this method returns
   * a `Monoid[A]`.
   */
  @inline final def multiplicative[A](implicit ev: ring.MultiplicativeMonoid[A]): Monoid[A] =
    ev.multiplicative
}
