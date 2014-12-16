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

  def partialCompare(x: A, y: A): Double = compare(x, y).toDouble

  override def eqv(x: A, y: A): Boolean = compare(x, y) == 0
  override def gt(x: A, y: A): Boolean = compare(x, y) > 0
  override def lt(x: A, y: A): Boolean = compare(x, y) < 0
  override def gteqv(x: A, y: A): Boolean = compare(x, y) >= 0
  override def lteqv(x: A, y: A): Boolean = compare(x, y) <= 0

  def min(x: A, y: A): A = if (lt(x, y)) x else y
  def max(x: A, y: A): A = if (gt(x, y)) x else y
  def compare(x: A, y: A): Int

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
}

object Order extends OrderFunctions {
  @inline final def apply[A](implicit ev: Order[A]) = ev

  def by[@sp A, @sp B](f: A => B)(implicit ev: Order[B]): Order[A] = ev.on(f)

  def from[@sp A](f: (A, A) => Int): Order[A] =
    new Order[A] {
      def compare(x: A, y: A) = f(x, y)
    }

  implicit def ordering[A](implicit ev: Order[A]): Ordering[A] =
    new Ordering[A] {
      def compare(x: A, y: A) = ev.compare(x, y)
    }
}

trait OrderFunctions {
  def compare[@sp A](x: A, y: A)(implicit ev: Order[A]): Int =
    ev.compare(x, y)
  def gt[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.gt(x, y)
  def lt[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.lt(x, y)
  def gteqv[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.gteqv(x, y)
  def lteqv[@sp A](x: A, y: A)(implicit ev: Order[A]): Boolean =
    ev.lteqv(x, y)

  def min[@sp A](x: A, y: A)(implicit ev: Order[A]): A =
    ev.min(x, y)
  def max[@sp A](x: A, y: A)(implicit ev: Order[A]): A =
    ev.max(x, y)
}
