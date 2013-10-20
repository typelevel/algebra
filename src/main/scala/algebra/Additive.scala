package algebra

import scala.{ specialized => sp }

trait AdditiveSemigroup[@sp(Byte, Short, Int, Long, Float, Double) A] {
  def additive: Semigroup[A] = new Semigroup[A] {
    def op(x: A, y: A): A = plus(x, y)
  }
  def plus(x: A, y: A): A
}

trait AdditiveMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveSemigroup[A] {
  override def additive: Monoid[A] = new Monoid[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
  }
  def zero: A
}

trait AdditiveCommutativeMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveMonoid[A] {
  override def additive: CommutativeMonoid[A] = new CommutativeMonoid[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
  }
}

trait AdditiveGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveMonoid[A] {
  override def additive: Group[A] = new Group[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
    def inverse(x: A): A = negate(x)
  }

  def negate(x: A): A
  def minus(x: A, y: A): A = plus(x, negate(y))
}

trait AdditiveCommutativeGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveGroup[A] with AdditiveCommutativeMonoid[A] {
  override def additive: CommutativeGroup[A] = new CommutativeGroup[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
    def inverse(x: A): A = negate(x)
  }
}

trait AdditiveSemigroupFunctions {
  def plus[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: AdditiveSemigroup[A]): A =
    ev.plus(x, y)
}

trait AdditiveMonoidFunctions extends AdditiveSemigroupFunctions {
  def zero[@sp(Byte, Short, Int, Long, Float, Double) A](implicit ev: AdditiveMonoid[A]): A =
    ev.zero
}

trait AdditiveGroupFunctions extends AdditiveMonoidFunctions {
  def negate[@sp(Byte, Short, Int, Long, Float, Double) A](x: A)(implicit ev: AdditiveGroup[A]): A =
    ev.negate(x)
  def minus[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: AdditiveGroup[A]): A =
    ev.minus(x, y)
}
