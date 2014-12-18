package algebra
package lattice

import scala.{specialized => sp}

trait Heyting[@sp(Boolean, Byte, Short, Int, Long) A] extends Any with BoundedLattice[A] {
  def and(a: A, b: A): A
  def meet(a: A, b: A): A = and(a, b)

  def or(a: A, b: A): A
  def join(a: A, b: A): A = or(a, b)

  def imp(a: A, b: A): A
  def complement(a: A): A
}

trait HeytingFunctions {
  def zero[@sp(Boolean, Byte, Short, Int, Long) A](implicit ev: Bool[A]): A = ev.zero
  def one[@sp(Boolean, Byte, Short, Int, Long) A](implicit ev: Bool[A]): A = ev.one

  def complement[@sp(Boolean, Byte, Short, Int, Long) A](x: A)(implicit ev: Bool[A]): A =
    ev.complement(x)

  def and[@sp(Boolean, Byte, Short, Int, Long) A](x: A, y: A)(implicit ev: Bool[A]): A =
    ev.and(x, y)
  def or[@sp(Boolean, Byte, Short, Int, Long) A](x: A, y: A)(implicit ev: Bool[A]): A =
    ev.or(x, y)
  def imp[@sp(Boolean, Byte, Short, Int, Long) A](x: A, y: A)(implicit ev: Bool[A]): A =
    ev.imp(x, y)
}


object Heyting extends HeytingFunctions {

  /**
   * Access an implicit `Heyting[A]`.
   */
  @inline final def apply[@sp(Boolean, Byte, Short, Int, Long) A](implicit ev: Heyting[A]): Heyting[A] = ev
}
