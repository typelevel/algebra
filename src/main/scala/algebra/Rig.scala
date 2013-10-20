package algebra

import annotation.tailrec
import scala.{specialized => sp}

/**
 * Rig is a ring whose additive structure doesn't have an inverse (ie. it is
 * monoid, not a group). Put another way, a Rig is a Ring without a negative.
 */
trait Rig[@sp(Byte, Short, Int, Long, Float, Double) A] extends Semiring[A] with MultiplicativeMonoid[A]

object Rig extends AdditiveMonoidFunctions with MultiplicativeMonoidFunctions {
  @inline final def apply[A](implicit ev: Rig[A]): Rig[A] = ev
}
