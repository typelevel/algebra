package algebra
package ring

import scala.{specialized => sp}

/**
 * CommutativeRing is a Ring that is commutative under multiplication.
 */
trait CommutativeRing[@sp(Byte, Short, Int, Long, Float, Double) A] extends Ring[A] with MultiplicativeCommutativeMonoid[A]

object CommutativeRing extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit r: CommutativeRing[A]): CommutativeRing[A] = r
}
