package algebra

import scala.{ specialized => sp }

/**
 * An abelian group is a group whose operation is commutative.
 */
trait CommutativeGroup[@sp(Byte, Short, Int, Long, Float, Double) A]
    extends Group[A] with CommutativeMonoid[A]

object CommutativeGroup extends GroupFunctions {
  @inline final def apply[A](implicit ev: CommutativeGroup[A]): CommutativeGroup[A] = ev
}
