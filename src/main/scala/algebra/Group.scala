package algebra

import scala.{ specialized => sp }

/**
 * A group is a monoid where each element has an inverse.
 */
trait Group[@sp(Byte, Short, Int, Long, Float, Double) A] extends Monoid[A] {
  def inverse(a: A): A
  def opInverse(a: A, b: A): A = op(a, inverse(b))

  /**
   * Return `a` appended to itself `n` times. If `n` is negative, then
   * this returns `-a` appended to itself `n` times.
   */
  override def sumn(a: A, n: Int): A =
    if (n < 0) positiveSumn(inverse(a), -n)
    else if (n == 0) id
    else positiveSumn(a, n)
}

object Group {
  @inline final def apply[A](implicit ev: Group[A]): Group[A] = ev
  @inline final def additive[A](implicit A: AdditiveGroup[A]): Group[A] =  A.additive
  @inline final def multiplicative[A](implicit A: MultiplicativeGroup[A]): Group[A] = A.multiplicative

}

/**
 * An abelian group is a group whose operation is commutative.
 */
trait AbGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends Group[A] with CMonoid[A]

object AbGroup {
  @inline final def apply[A](implicit ev: AbGroup[A]): AbGroup[A] = ev
  @inline final def additive[A](implicit A: AdditiveAbGroup[A]): AbGroup[A] =  A.additive
  @inline final def multiplicative[A](implicit A: MultiplicativeAbGroup[A]): AbGroup[A] = A.multiplicative
}
