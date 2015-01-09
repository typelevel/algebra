package algebra
package ring

/**
 * CommutativeRig is a Rig that is commutative under multiplication.
 */
trait CommutativeRig[@mb @sp(Byte, Short, Int, Long, Float, Double) A] extends Any with Rig[A] with MultiplicativeCommutativeMonoid[A]

object CommutativeRig extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit r: CommutativeRig[A]): CommutativeRig[A] = r
}
