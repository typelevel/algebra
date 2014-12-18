package algebra
package lattice

import scala.{specialized => sp}

trait BoundedSemilattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with Semilattice[A]

object BoundedSemilattice {

  /**
   * Access an implicit `BoundedSemilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: BoundedSemilattice[A]): BoundedSemilattice[A] = ev
}
