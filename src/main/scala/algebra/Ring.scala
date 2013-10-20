package algebra

import scala.{specialized => sp}

/**
 * Ring represents a set (A) that is a group over addition (+) and a monoid
 * over multiplication (*). Aside from this, the multiplication must distribute
 * over addition.
 *
 * Ring implements some methods (for example fromInt) in terms of other more
 * fundamental methods (zero, one and plus). Where possible, these methods
 * should be overridden by more efficient implementations.
 */
trait Ring[@sp(Byte, Short, Int, Long, Float, Double) A] extends Rig[A] with Rng[A] {
  /**
   * Defined to be equivalent to `Group.sumn(one, n)(ring.additive)`. That is,
   * `n` repeated summations of this ring's `one`, or `-one` if `n` is negative.
   */
  def fromInt(n: Int): A = additive.sumn(one, n)
}

object Ring extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit ev: Ring[A]): Ring[A] = ev
}
