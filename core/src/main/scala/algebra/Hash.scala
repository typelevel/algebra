package algebra

import scala.{specialized => sp}

import scala.util.hashing.Hashing

/**
 * This is typeclass extends Eq to provide a method hash with the law:
 * if eqv(a, b) then hash(a) == hash(b).
 *
 * This is similar to scala.util.hashing.Hashing
 */
trait Hash[@sp A] extends Any with Eq[A] with Serializable { self =>
  def hash(a: A): Int
  /**
   * Constructs a new `Eq` instance for type `B` where 2 elements are
   * equivalent iff `eqv(f(x), f(y))`.
   */
  override def on[@sp B](f: B => A): Hash[B] =
    new Hash[B] {
      def eqv(x: B, y: B): Boolean = self.eqv(f(x), f(x))
      def hash(x: B): Int = self.hash(f(x))
    }
}

trait HashFunctions {
  def hash[@sp A](a: A)(implicit ev: Hash[A]): Int = ev.hash(a)
}

object Hash extends HashFunctions {
  /**
   * Access an implicit `Hash[A]`.
   */
  @inline final def apply[A](implicit ev: Hash[A]) = ev

  /**
   * Convert an implicit `Hash[B]` to an `Hash[A]`
   * using the given function `f`.
   */
  def by[@sp A, @sp B](f: A => B)(implicit ev: Hash[B]): Hash[A] =
    ev.on(f)

  /**
   * This gives compatibility with scala's Hashing trait
   */
  implicit def hashing[A](implicit h: Hash[A]): Hashing[A] =
    new Hashing[A] {
      def hash(a: A) = h.hash(a)
    }
}
