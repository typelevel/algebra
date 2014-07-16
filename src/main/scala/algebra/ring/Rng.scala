package algebra
package ring

import annotation.tailrec
import scala.{specialized => sp}

/**
 * Rng is a ring whose multiplicative structure doesn't have an
 * identity (i.e. it is semigroup, not a monoid). Put another way, a
 * Rng is a Ring without a multiplicative identity.
 */
trait Rng[@sp(Byte, Short, Int, Long, Float, Double) A] extends Semiring[A] with AdditiveCommutativeGroup[A]

object Rng extends AdditiveGroupFunctions with MultiplicativeSemigroupFunctions {
  @inline final def apply[A](implicit ev: Rng[A]): Rng[A] = ev
}
