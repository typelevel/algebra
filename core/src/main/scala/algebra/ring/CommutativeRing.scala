package algebra
package ring

/**
 * CommutativeRing is a Ring that is commutative under multiplication.
 */
trait CommutativeRing[@mb @sp(Byte, Short, Int, Long, Float, Double) A] extends Any with Ring[A] with CommutativeRig[A]

object CommutativeRing extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit r: CommutativeRing[A]): CommutativeRing[A] = r
}
