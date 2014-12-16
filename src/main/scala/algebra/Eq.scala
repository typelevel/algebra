package algebra

import scala.{specialized => sp}

/**
 * A type class used to determine equality between 2 instances of the same
 * type. Any 2 instances `x` and `y` are equal if `eqv(x, y)` is `true`.
 * Moreover, `eqv` should form an equivalence relation.
 */
trait Eq[@sp A] extends Any { self =>

  /** Returns `true` if `x` and `y` are equivalent, `false` otherwise. */
  def eqv(x: A, y: A): Boolean

  /** Returns `false` if `x` and `y` are equivalent, `true` otherwise. */
  def neqv(x: A, y: A): Boolean = !eqv(x, y)

  /**
   * Constructs a new `Eq` instance for type `B` where 2 elements are
   * equivalent iff `eqv(f(x), f(y))`.
   */
  def on[@sp B](f: B => A): Eq[B] =
    new Eq[B] {
      def eqv(x: B, y: B): Boolean = self.eqv(f(x), f(x))
    }
}

object Eq extends EqFunctions {
  def apply[A](implicit ev: Eq[A]): Eq[A] = ev

  def by[@sp A, @sp B](f: A => B)(implicit ev: Eq[B]): Eq[A] =
    new Eq[A] {
      def eqv(x: A, y: A): Boolean = ev.eqv(f(x), f(y))
    }
}

trait EqFunctions {
  def eqv[A](x: A, y: A)(implicit ev: Eq[A]): Boolean =
    ev.eqv(x, y)

  def neqv[A](x: A, y: A)(implicit ev: Eq[A]): Boolean =
    ev.neqv(x, y)
}
