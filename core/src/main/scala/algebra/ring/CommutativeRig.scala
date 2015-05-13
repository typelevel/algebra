package algebra
package ring

import scala.{specialized => sp}

import simulacrum._

/**
 * CommutativeRig is a Rig that is commutative under multiplication.
 */
@typeclass trait CommutativeRig[@sp(Int, Long, Float, Double) A] extends Any with Rig[A] with MultiplicativeCommutativeMonoid[A]

object CommutativeRig extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions
