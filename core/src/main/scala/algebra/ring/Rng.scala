package algebra
package ring

import scala.{specialized => sp}

import simulacrum._

/**
 * Rng (pronounced "Rung") consists of:
 * 
 *  - a commutative group for addition (+)
 *  - a semigroup for multiplication (*)
 *
 * Alternately, a Rng can be thought of as a ring without a
 * multiplicative identity (or as a semiring with an additive
 * inverse).
 * 
 * Mnemonic: "Rng is a Ring without multiplicative 'I'dentity."
 */
@typeclass trait Rng[@sp(Int, Long, Float, Double) A] extends Any with Semiring[A] with AdditiveCommutativeGroup[A]

object Rng extends AdditiveGroupFunctions with MultiplicativeSemigroupFunctions
