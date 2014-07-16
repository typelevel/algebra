package algebra
package ring

import scala.{ specialized => sp }

trait AdditiveSemigroup[@sp(Byte, Short, Int, Long, Float, Double) A] {
  def additive: Semigroup[A] = new Semigroup[A] {
    def combine(x: A, y: A): A = plus(x, y)
  }
  def plus(x: A, y: A): A
  def hasCommutativeAddition: Boolean = false
}

trait AdditiveCommutativeSemigroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveSemigroup[A] {
  override def additive: CommutativeSemigroup[A] = new CommutativeSemigroup[A] {
    def combine(x: A, y: A): A = plus(x, y)
  }
  override def hasCommutativeAddition: Boolean = true
}

trait AdditiveMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveSemigroup[A] {
  override def additive: Monoid[A] = new Monoid[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
  }
  def zero: A
}

trait AdditiveCommutativeMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveMonoid[A] with AdditiveCommutativeSemigroup[A] {
  override def additive: CommutativeMonoid[A] = new CommutativeMonoid[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
  }
}

trait AdditiveGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveMonoid[A] {
  override def additive: Group[A] = new Group[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
    override def uncombine(x: A, y: A): A = minus(x, y)
    def inverse(x: A): A = negate(x)
  }

  def negate(x: A): A
  def minus(x: A, y: A): A = plus(x, negate(y))
}

trait AdditiveCommutativeGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveGroup[A] with AdditiveCommutativeMonoid[A] {
  override def additive: CommutativeGroup[A] = new CommutativeGroup[A] {
    def empty = zero
    def combine(x: A, y: A): A = plus(x, y)
    override def uncombine(x: A, y: A): A = minus(x, y)
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

object AdditiveSemigroup extends AdditiveSemigroupFunctions {
  @inline final def apply[A](implicit ev: AdditiveSemigroup[A]): AdditiveSemigroup[A] = ev
}
  
object AdditiveCommutativeSemigroup extends AdditiveSemigroupFunctions {
  @inline final def apply[A](implicit ev: AdditiveCommutativeSemigroup[A]): AdditiveCommutativeSemigroup[A] = ev
}
  
object AdditiveMonoid extends AdditiveMonoidFunctions {
  @inline final def apply[A](implicit ev: AdditiveMonoid[A]): AdditiveMonoid[A] = ev
}
  
object AdditiveCommutativeMonoid extends AdditiveMonoidFunctions {
  @inline final def apply[A](implicit ev: AdditiveCommutativeMonoid[A]): AdditiveCommutativeMonoid[A] = ev
}
  
object AdditiveGroup extends AdditiveGroupFunctions {
  @inline final def apply[A](implicit ev: AdditiveGroup[A]): AdditiveGroup[A] = ev
}
  
object AdditiveCommutativeGroup extends AdditiveGroupFunctions {
  @inline final def apply[A](implicit ev: AdditiveCommutativeGroup[A]): AdditiveCommutativeGroup[A] = ev
}
