package algebra
package ring

import annotation.tailrec
import scala.{specialized => sp}

/**
 * Semiring is a ring without a multiplicative identity or an
 * inverse. Thus, it has no negation or one.
 *
 * A Semiring with an additive inverse (-) is a Rng.
 * A Semiring with a multiplicative identity (1) is a Rig.
 * A Semiring with all of the above is a Ring.
 */
trait Semiring[@sp(Byte, Short, Int, Long, Float, Double) A] extends AdditiveMonoid[A] with MultiplicativeSemigroup[A]

object Semiring extends AdditiveMonoidFunctions with MultiplicativeSemigroupFunctions {
  @inline final def apply[A](implicit ev: Semiring[A]): Semiring[A] = ev
}
