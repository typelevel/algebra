package algebra

import scala.{specialized => sp}

/**
 * The `Order` type class is used to define a total ordering on some type `A`.
 * An order is defined by a relation <=, which obeys the following laws:
 *
 * - either x <= y or y <= x (totality)
 * - if x <= y and y <= x, then x == y (antisymmetry)
 * - if x <= y and y <= z, then x <= z (transitivity)
 *
 * The truth table for compare is defined as follows:
 *
 * x <= y    x >= y      Int
 * true      true        = 0     (corresponds to x == y)
 * true      false       < 0     (corresponds to x < y)
 * false     true        > 0     (corresponds to x > y)
 *
 * By the totality law, x <= y and y <= x cannot be both false.
 */
trait Order[@sp A] extends Any with PartialOrder[A] { self =>

  /**
   * Result of comparing `x` with `y`. Returns an Int whose sign is:
   * - negative iff `x < y`
   * - zero     iff `x = y`
   * - positive iff `x > y`
   */
  def compare(x: A, y: A): Int

  def partialCompare(x: A, y: A): Double = compare(x, y).toDouble

  /**
   * If x <= y, return x, else return y.
   */
  def min(x: A, y: A): A = if (lt(x, y)) x else y

  /**
   * If x >= y, return x, else return y.
   */
  def max(x: A, y: A): A = if (gt(x, y)) x else y

  /**
   * Defines an order on `B` by mapping `B` to `A` using `f` and using `A`s
   * order to order `B`.
   */
  override def on[@sp B](f: B => A): Order[B] =
    new Order[B] {
      def compare(x: B, y: B): Int = self.compare(f(x), f(y))
    }

  /**
   * Defines an ordering on `A` where all arrows switch direction.
   */
  override def reverse: Order[A] =
    new Order[A] {
      def compare(x: A, y: A): Int = self.compare(y, x)
    }

  // The following may be overridden for performance:

  /**
   * Returns true if `x` = `y`, false otherwise.
   */
  override def eqv(x: A, y: A): Boolean =
    compare(x, y) == 0

  /**
   * Returns true if `x` != `y`, false otherwise.
   */
  override def neqv(x: A, y: A): Boolean =
    compare(x, y) != 0

  /**
   * Returns true if `x` <= `y`, false otherwise.
   */
  override def lteqv(x: A, y: A): Boolean =
    compare(x, y) <= 0

  /**
   * Returns true if `x` < `y`, false otherwise.
   */
  override def lt(x: A, y: A): Boolean =
    compare(x, y) < 0

  /**
   * Returns true if `x` >= `y`, false otherwise.
   */
  override def gteqv(x: A, y: A): Boolean =
    compare(x, y) >= 0

  /**
   * Returns true if `x` > `y`, false otherwise.
   */
  override def gt(x: A, y: A): Boolean =
    compare(x, y) > 0
}

trait OrderFunctions {
  def compare[@sp A](x: A, y: A)(implicit ev: Order[A]): Int =
    ev.compare(x, y)

  def eqv[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.eqv(x, y)
  def neqv[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.neqv(x, y)
  def gt[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.gt(x, y)
  def gteqv[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.gteqv(x, y)
  def lt[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.lt(x, y)
  def lteqv[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.lteqv(x, y)

  def min[@sp A](x: A, y: A)(implicit ev: Order[A]): A =
    ev.min(x, y)
  def max[@sp A](x: A, y: A)(implicit ev: Order[A]): A =
    ev.max(x, y)
}

object Order extends OrderFunctions {

  /**
   * Access an implicit `Eq[A]`.
   */
  @inline final def apply[A](implicit ev: Order[A]) = ev

  /**
   * Convert an implicit `Order[A]` to an `Order[B]` using the given
   * function `f`.
   */
  def by[@sp A, @sp B](f: A => B)(implicit ev: Order[B]): Order[A] =
    ev.on(f)

  /**
   * Define an `Order[A]` using the given function `f`.
   */
  def from[@sp A](f: (A, A) => Int): Order[A] =
    new Order[A] {
      def compare(x: A, y: A) = f(x, y)
    }

  /**
   * Define an `Order[A]` using the given functions `fs` until
   * the first one returns a non-0 result.
   */
  def fromAll[@sp A](fs: ((A, A) => Int)*): Order[A] =
    new Order[A] {
      def compare(x: A, y: A) =
        fs.foldLeft(0) {
          case (0, f) => f(x,y)
          case (r, _) => r
        }
    }

  /**
   * Implicitly convert a `Order[A]` to a `scala.math.Ordering[A]`
   * instance.
   */
  implicit def ordering[A](implicit ev: Order[A]): Ordering[A] =
    new Ordering[A] {
      def compare(x: A, y: A) = ev.compare(x, y)
    }
}
