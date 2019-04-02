package algebra
package lattice

import scala.{specialized => sp}

/**
  * Logic models a logic generally.
  * For intuitionistic logic see `Heyting`
  * For fuzzy logic see `DeMorgan`
  */
trait Logic[@sp(Int, Long) A] extends Any { self =>
  def and(a: A, b: A): A

  def or(a: A, b: A): A

  def imp(a: A, b: A): A
  def complement(a: A): A

  def xor(a: A, b: A): A = or(and(a, complement(b)), and(complement(a), b))
  def nand(a: A, b: A): A = complement(and(a, b))
  def nor(a: A, b: A): A = complement(or(a, b))
  def nxor(a: A, b: A): A = complement(xor(a, b))
}

trait LogicGenBoolOverlap[H[A] <: Logic[A]] {
  def and[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.and(x, y)
  def or[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.or(x, y)
  def xor[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.xor(x, y)
}

trait LogicFunctions[H[A] <: Logic[A]] {
  def complement[@sp(Int, Long) A](x: A)(implicit ev: H[A]): A =
    ev.complement(x)

  def imp[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.imp(x, y)
  def nor[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.nor(x, y)
  def nxor[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.nxor(x, y)
  def nand[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.nand(x, y)
}


object Logic extends LogicFunctions[Logic] with LogicGenBoolOverlap[Logic] {

  /**
    * Access an implicit `Logic[A]`.
    */
  @inline final def apply[@sp(Int, Long) A](implicit ev: Logic[A]): Logic[A] = ev

  /**
    * Turn a [[Heyting]] into a `Logic`.
    * Used for binary compatibility.
    */
  final def fromHeyting[@sp(Int, Long) A](h: Heyting[A]): Logic[A] =
    new Logic[A] {
      override def and(a: A, b: A): A = h.and(a, b)

      override def or(a: A, b: A): A = h.or(a, b)

      override def imp(a: A, b: A): A = h.imp(a, b)

      override def complement(a: A): A = h.complement(a)
    }
}
