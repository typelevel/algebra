package algebra
package ring

import scala.{ specialized => sp }
import scala.annotation.tailrec

import simulacrum._

@typeclass trait AdditiveSemigroup[@sp(Int, Long, Float, Double) A] extends Any with Serializable {
  @noop def additive: Semigroup[A] = new Semigroup[A] {
    def combine(x: A, y: A): A = plus(x, y)
  }

  def plus(x: A, y: A): A

  @noop def sumN(a: A, n: Int): A =
    if (n > 0) positiveSumN(a, n)
    else throw new IllegalArgumentException("Illegal non-positive exponent to sumN: %s" format n)

  protected[this] def positiveSumN(a: A, n: Int): A = {
    @tailrec def loop(b: A, k: Int, extra: A): A =
      if (k == 1) plus(b, extra) else {
        val x = if ((k & 1) == 1) plus(b, extra) else extra
        loop(plus(b, b), k >>> 1, x)
      }
    if (n == 1) a else loop(a, n - 1, a)
  }

  /**
   * Given a sequence of `as`, combine them and return the total.
   *
   * If the sequence is empty, returns None. Otherwise, returns Some(total).
   */
  def trySum(as: TraversableOnce[A]): Option[A] =
    as.reduceOption(plus)
}

@typeclass trait AdditiveCommutativeSemigroup[@sp(Int, Long, Float, Double) A] extends Any with AdditiveSemigroup[A] {
  @noop override def additive: CommutativeSemigroup[A] = new CommutativeSemigroup[A] {
    def combine(x: A, y: A): A = plus(x, y)
  }
}

@typeclass trait AdditiveMonoid[@sp(Int, Long, Float, Double) A] extends Any with AdditiveSemigroup[A] {
  @noop override def additive: Monoid[A] = new Monoid[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
  }

  @noop def zero: A

  /**
    * Tests if `a` is zero.
    */
  def isZero(a: A)(implicit ev: Eq[A]): Boolean = ev.eqv(a, zero)

  @noop override def sumN(a: A, n: Int): A =
    if (n > 0) positiveSumN(a, n)
    else if (n == 0) zero
    else throw new IllegalArgumentException("Illegal negative exponent to sumN: %s" format n)

  /**
   * Given a sequence of `as`, compute the sum.
   */
  def sum(as: TraversableOnce[A]): A =
    as.foldLeft(zero)(plus)
}

@typeclass trait AdditiveCommutativeMonoid[@sp(Int, Long, Float, Double) A] extends Any with AdditiveMonoid[A] with AdditiveCommutativeSemigroup[A] {
  @noop override def additive: CommutativeMonoid[A] = new CommutativeMonoid[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
  }
}

@typeclass trait AdditiveGroup[@sp(Int, Long, Float, Double) A] extends Any with AdditiveMonoid[A] {
  @noop override def additive: Group[A] = new Group[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
    override def remove(x: A, y: A): A = minus(x, y)
    def inverse(x: A): A = negate(x)
  }

  def negate(x: A): A
  def minus(x: A, y: A): A = plus(x, negate(y))

  @noop override def sumN(a: A, n: Int): A =
    if (n > 0) positiveSumN(a, n)
    else if (n == 0) zero
    else if (n == Int.MinValue) positiveSumN(negate(plus(a, a)), 1073741824)
    else positiveSumN(negate(a), -n)
}

@typeclass trait AdditiveCommutativeGroup[@sp(Int, Long, Float, Double) A] extends Any with AdditiveGroup[A] with AdditiveCommutativeMonoid[A] {
  @noop override def additive: CommutativeGroup[A] = new CommutativeGroup[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
    override def remove(x: A, y: A): A = minus(x, y)
    def inverse(x: A): A = negate(x)
  }
}

trait AdditiveSemigroupFunctions {
  def isCommutative[A](implicit ev: AdditiveSemigroup[A]): Boolean =
    ev.isInstanceOf[AdditiveCommutativeSemigroup[_]]

  def plus[@sp(Int, Long, Float, Double) A](x: A, y: A)(implicit ev: AdditiveSemigroup[A]): A =
    ev.plus(x, y)

  def sumN[@sp(Int, Long, Float, Double) A](a: A, n: Int)(implicit ev: AdditiveSemigroup[A]): A =
    ev.sumN(a, n)

  def trySum[@sp(Int, Long, Float, Double) A](as: TraversableOnce[A])(implicit ev: AdditiveSemigroup[A]): Option[A] =
    ev.trySum(as)
}

trait AdditiveMonoidFunctions extends AdditiveSemigroupFunctions {
  def zero[@sp(Int, Long, Float, Double) A](implicit ev: AdditiveMonoid[A]): A =
    ev.zero

  def isZero[@sp(Int, Long, Float, Double) A](a: A)(implicit ev0: AdditiveMonoid[A], ev1: Eq[A]): Boolean =
    ev0.isZero(a)

  def sum[@sp(Int, Long, Float, Double) A](as: TraversableOnce[A])(implicit ev: AdditiveMonoid[A]): A =
    ev.sum(as)
}

trait AdditiveGroupFunctions extends AdditiveMonoidFunctions {
  def negate[@sp(Int, Long, Float, Double) A](x: A)(implicit ev: AdditiveGroup[A]): A =
    ev.negate(x)
  def minus[@sp(Int, Long, Float, Double) A](x: A, y: A)(implicit ev: AdditiveGroup[A]): A =
    ev.minus(x, y)
}

object AdditiveSemigroup extends AdditiveSemigroupFunctions

object AdditiveCommutativeSemigroup extends AdditiveSemigroupFunctions

object AdditiveMonoid extends AdditiveMonoidFunctions

object AdditiveCommutativeMonoid extends AdditiveMonoidFunctions

object AdditiveGroup extends AdditiveGroupFunctions

object AdditiveCommutativeGroup extends AdditiveGroupFunctions
