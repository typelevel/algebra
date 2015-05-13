package algebra
package ring

import scala.{specialized => sp}

import simulacrum._

/**
 * Rig consists of:
 * 
 *  - a commutative monoid for addition (+)
 *  - a monoid for multiplication (*)
 * 
 * Alternately, a Rig can be thought of as a ring without
 * multiplicative or additive inverses (or as a semiring with a
 * multiplicative identity).

 * Mnemonic: "Rig is a Ring without 'N'egation."
 */
@typeclass trait Rig[@sp(Int, Long, Float, Double) A] extends Any with Semiring[A] with MultiplicativeMonoid[A]

object Rig extends AdditiveMonoidFunctions with MultiplicativeMonoidFunctions
