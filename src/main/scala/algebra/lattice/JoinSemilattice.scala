package algebra
package lattice

import scala.{specialized => sp}

trait JoinSemilattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any {
  def join(lhs: A, rhs: A): A
}

object JoinSemilattice {

  /**
   * Access an implicit `JoinSemilattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: JoinSemilattice[A]): JoinSemilattice[A] = ev
}
