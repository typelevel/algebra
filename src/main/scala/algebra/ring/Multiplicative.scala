package algebra
package ring

import scala.{ specialized => sp }

trait MultiplicativeSemigroup[@sp(Byte, Short, Int, Long, Float, Double) A] {
  def multiplicative: Semigroup[A] = new Semigroup[A] {
    def combine(x: A, y: A): A = times(x, y)
  }

  def times(x: A, y: A): A

  def hasCommutativeMultiplication: Boolean = false

  def pow(a: A, n: Int): A =
    if (n > 0) multiplicative.sumn(a, n)
    else throw new IllegalArgumentException("Illegal non-positive exponent %s to Semiring#pow" format n)
}

trait MultiplicativeCommutativeSemigroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends MultiplicativeSemigroup[A] {
  override def hasCommutativeMultiplication: Boolean = false
  override def multiplicative: CommutativeSemigroup[A] = new CommutativeSemigroup[A] {
    def combine(x: A, y: A): A = times(x, y)
  }
}

trait MultiplicativeMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends MultiplicativeSemigroup[A] {
  override def multiplicative: Monoid[A] = new Monoid[A] {
    def empty = one
    def combine(x: A, y: A): A = times(x, y)
  }

  def one: A

  override def pow(a: A, n: Int): A =
    if (n >= 0) multiplicative.sumn(a, n)
    else throw new IllegalArgumentException("Illegal negative exponent %s to Monoid#pow" format n)
}

trait MultiplicativeCommutativeMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends MultiplicativeMonoid[A] with MultiplicativeCommutativeSemigroup[A] {
  override def multiplicative: CommutativeMonoid[A] = new CommutativeMonoid[A] {
    def empty = one
    def combine(x: A, y: A): A = times(x, y)
  }
}

trait MultiplicativeGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends MultiplicativeMonoid[A] {
  override def multiplicative: Group[A] = new Group[A] {
    def empty = one
    def combine(x: A, y: A): A = times(x, y)
    override def uncombine(x: A, y: A): A = div(x, y)
    def inverse(x: A): A = reciprocal(x)
  }

  def reciprocal(x: A): A = div(one, x)
  def div(x: A, y: A): A

  override def pow(a: A, n: Int): A =
    if (n >= 0) multiplicative.sumn(a, n) else multiplicative.sumn(reciprocal(a), -n)
}

trait MultiplicativeCommutativeGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends MultiplicativeGroup[A] with MultiplicativeCommutativeMonoid[A] {
  override def multiplicative: CommutativeGroup[A] = new CommutativeGroup[A] {
    def empty = one
    def combine(x: A, y: A): A = times(x, y)
    override def uncombine(x: A, y: A): A = div(x, y)
    def inverse(x: A): A = reciprocal(x)
  }
}

trait MultiplicativeSemigroupFunctions {
  def times[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: MultiplicativeSemigroup[A]): A =
    ev.times(x, y)
  def pow[@sp(Byte, Short, Int, Long, Float, Double) A](a: A, n: Int)(implicit ev: MultiplicativeSemigroup[A]): A =
    ev.pow(a, n)
}

trait MultiplicativeMonoidFunctions extends MultiplicativeSemigroupFunctions {
  def one[@sp(Byte, Short, Int, Long, Float, Double) A](implicit ev: MultiplicativeMonoid[A]): A =
    ev.one
}

trait MultiplicativeGroupFunctions extends MultiplicativeMonoidFunctions {
  def reciprocal[@sp(Byte, Short, Int, Long, Float, Double) A](x: A)(implicit ev: MultiplicativeGroup[A]): A =
    ev.reciprocal(x)
  def div[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: MultiplicativeGroup[A]): A =
    ev.div(x, y)
}

object MultiplicativeSemigroup extends MultiplicativeSemigroupFunctions {
  @inline final def apply[A](implicit ev: MultiplicativeSemigroup[A]): MultiplicativeSemigroup[A] = ev
}
  
object MultiplicativeCommutativeSemigroup extends MultiplicativeSemigroupFunctions {
  @inline final def apply[A](implicit ev: MultiplicativeCommutativeSemigroup[A]): MultiplicativeCommutativeSemigroup[A] = ev
}
  
object MultiplicativeMonoid extends MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit ev: MultiplicativeMonoid[A]): MultiplicativeMonoid[A] = ev
}
  
object MultiplicativeCommutativeMonoid extends MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit ev: MultiplicativeCommutativeMonoid[A]): MultiplicativeCommutativeMonoid[A] = ev
}
  
object MultiplicativeGroup extends MultiplicativeGroupFunctions {
  @inline final def apply[A](implicit ev: MultiplicativeGroup[A]): MultiplicativeGroup[A] = ev
}
  
object MultiplicativeCommutativeGroup extends MultiplicativeGroupFunctions {
  @inline final def apply[A](implicit ev: MultiplicativeCommutativeGroup[A]): MultiplicativeCommutativeGroup[A] = ev
}
