package algebra

import scala.{ specialized => sp }

/**
 * CommutativeMonoid represents a commutative monoid.
 * 
 * A monoid is commutative if for all x and y, x |+| y === y |+| x.
 */
trait CommutativeMonoid[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A]
    extends Monoid[A] with CommutativeSemigroup[A]

object CommutativeMonoid extends MonoidFunctions {
  @inline final def apply[A](implicit ev: CommutativeMonoid[A]): CommutativeMonoid[A] = ev

  @inline final def additive[A](implicit ev: ring.AdditiveCommutativeMonoid[A]): CommutativeMonoid[A] =  ev.additive
  @inline final def multiplicative[A](implicit ev: ring.MultiplicativeCommutativeMonoid[A]): CommutativeMonoid[A] = ev.multiplicative
}
