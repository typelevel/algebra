package algebra

import scala.{ specialized => sp }

/**
 * A group is a monoid where each element has an inverse.
 */
trait Group[@sp(Byte, Short, Int, Long, Float, Double) A] extends Monoid[A] {

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
  override def combineN(a: A, n: Int): A =
    if (n > 0) repeatedCombineN(a, n)
    else if (n == 0) empty
    else if (n == Int.MinValue) combineN(inverse(combine(a, a)), 1073741824)
    else repeatedCombineN(inverse(a), -n)
}

trait GroupFunctions extends MonoidFunctions {
  def inverse[@sp(Byte, Short, Int, Long, Float, Double) A](a: A)(implicit ev: Group[A]): A =
    ev.inverse(a)
  def remove[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: Group[A]): A =
    ev.remove(x, y)
}

object Group extends GroupFunctions {
  @inline final def apply[A](implicit ev: Group[A]): Group[A] = ev

  @inline final def additive[A](implicit ev: ring.AdditiveGroup[A]): Group[A] =  ev.additive
  @inline final def multiplicative[A](implicit ev: ring.MultiplicativeGroup[A]): Group[A] = ev.multiplicative
}
