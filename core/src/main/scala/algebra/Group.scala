package algebra

import scala.{ specialized => sp }

import simulacrum._

/**
 * A group is a monoid where each element has an inverse.
 */
@typeclass trait Group[@sp(Int, Long, Float, Double) A] extends Any with Monoid[A] {

  /**
   * Find the inverse of `a`.
   *
   * `combine(a, inverse(a))` = `combine(inverse(a), a)` = `empty`.
   */
  def inverse(a: A): A

  /**
   * Remove the element `b` from `a`.
   *
   * Equivalent to `combine(a, inverse(a))`
   */
  def remove(a: A, b: A): A = combine(a, inverse(b))

  /**
   * Return `a` appended to itself `n` times. If `n` is negative, then
   * this returns `inverse(a)` appended to itself `n` times.
   */
  @noop override def combineN(a: A, n: Int): A =
    if (n > 0) repeatedCombineN(a, n)
    else if (n == 0) empty
    else if (n == Int.MinValue) combineN(inverse(combine(a, a)), 1073741824)
    else repeatedCombineN(inverse(a), -n)
}

trait GroupFunctions extends MonoidFunctions {
  def inverse[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: Group[A]): A =
    ev.inverse(a)
  def remove[@sp(Int, Long, Float, Double) A](x: A, y: A)(implicit ev: Group[A]): A =
    ev.remove(x, y)
}

object Group extends GroupFunctions {

  /**
   * This method converts an additive instance into a generic
   * instance.
   * 
   * Given an implicit `AdditiveGroup[A]`, this method returns a
   * `Group[A]`.
   */
  @inline final def additive[A](implicit ev: ring.AdditiveGroup[A]): Group[A] =  ev.additive

  /**
   * This method converts an multiplicative instance into a generic
   * instance.
   * 
   * Given an implicit `MultiplicativeGroup[A]`, this method returns
   * a `Group[A]`.
   */
  @inline final def multiplicative[A](implicit ev: ring.MultiplicativeGroup[A]): Group[A] = ev.multiplicative
}
