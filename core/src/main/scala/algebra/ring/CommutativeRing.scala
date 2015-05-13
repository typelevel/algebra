package algebra
package ring

import scala.{specialized => sp}

import simulacrum._

/**
 * CommutativeRing is a Ring that is commutative under multiplication.
 */
@typeclass trait CommutativeRing[@sp(Int, Long, Float, Double) A] extends Any with Ring[A] with CommutativeRig[A]

object CommutativeRing extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions
