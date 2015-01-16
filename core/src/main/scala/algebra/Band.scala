package algebra

import scala.{specialized => sp}


/**
 * Bands are semigroups whose operation
 * (i.e. combine) is also idempotent.
 */
trait Band[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with Semigroup[A]

object Band {

  /**
   * Access an implicit `Semilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: Band[A]): Band[A] = ev
}
