package algebra
package lattice

import scala.{specialized => sp}

trait Lattice[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with JoinSemilattice[A] with MeetSemilattice[A]

trait LatticeFunctions {
  def join[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: JoinSemilattice[A]): A =
    ev.join(x, y)

  def meet[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: MeetSemilattice[A]): A =
    ev.meet(x, y)
}

object Lattice extends LatticeFunctions {

  /**
   * Access an implicit `Lattice[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A](implicit ev: Lattice[A]): Lattice[A] = ev
}
