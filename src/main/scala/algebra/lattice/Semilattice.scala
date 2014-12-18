package algebra
package lattice

import scala.{specialized => sp}

/**
 * Semilattices are commutative semigroups whose operation
 * (i.e. combine) is also idempotent.
 */
trait Semilattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with CommutativeSemigroup[A]

object Semilattice {

  /**
   * Access an implicit `Semilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: Semilattice[A]): Semilattice[A] = ev
}
