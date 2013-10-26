package algebra

import scala.{ specialized => sp }

/**
 * A group is a monoid where each element has an inverse.
 */
trait Group[@sp(Byte, Short, Int, Long, Float, Double) A] extends Monoid[A] {
  def inverse(a: A): A
  def uncombine(a: A, b: A): A = combine(a, inverse(b))

  /**
   * Return `a` appended to itself `n` times. If `n` is negative, then
   * this returns `-a` appended to itself `n` times.
   */
  override def sumn(a: A, n: Int): A =
    if (n < 0) positiveSumn(inverse(a), -n)
    else if (n == 0) empty
    else positiveSumn(a, n)
}

trait GroupFunctions extends MonoidFunctions {
  def inverse[A](a: A)(implicit ev: Group[A]): A = ev.inverse(a)
  def uncombine[A](x: A, y: A)(implicit ev: Group[A]): A = ev.uncombine(x, y)
}

object Group extends GroupFunctions {
  @inline final def apply[A](implicit ev: Group[A]): Group[A] = ev
}
