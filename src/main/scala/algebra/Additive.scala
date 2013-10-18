package algebra

import scala.{ specialized => sp }

object Additive {
  def apply[A](s: Semigroup[A]): AdditiveSemigroup[A] = new AdditiveSemigroup[A] {
    def plus(x: A, y: A): A = s.op(x, y)
  }

  def apply[A](m: Monoid[A]): AdditiveMonoid[A] = new AdditiveMonoid[A] {
    def plus(x: A, y: A): A = m.op(x, y)
    def zero = m.id
  }

  def apply[A](g: Group[A]): AdditiveGroup[A] = new AdditiveGroup[A] {
    def plus(x: A, y: A): A = g.op(x, y)
    override def minus(x: A, y: A): A = g.op(x, g.inverse(y))
    def zero: A = g.id
    def negate(x: A): A = g.inverse(x)
  }

  def apply[A](g: AbGroup[A]): AdditiveAbGroup[A] = new AdditiveAbGroup[A] {
    def plus(x: A, y: A): A = g.op(x, y)
    override def minus(x: A, y: A): A = g.op(x, g.inverse(y))
    def zero: A = g.id
    def negate(x: A): A = g.inverse(x)
  }
}

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

trait AdditiveCMonoid[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveMonoid[A] {
  override def additive: CMonoid[A] = new CMonoid[A] {
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

trait AdditiveAbGroup[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveGroup[A] with AdditiveCMonoid[A] {
  override def additive: AbGroup[A] = new AbGroup[A] {
    def id = zero
    def op(x: A, y: A): A = plus(x, y)
    def inverse(x: A): A = negate(x)
  }
}
