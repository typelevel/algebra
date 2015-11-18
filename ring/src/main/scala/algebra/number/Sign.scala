package algebra
package number

import algebra.ring.MultiplicativeCommutativeMonoid

/**
 * A simple ADT representing the `Sign` of an object.
 */
sealed abstract class Sign(val toInt: Int) {
  import Sign._

  def abs: Sign =
    if (this == Negative) Positive else this

  def compare(that: Sign): Int =
    this.toInt - that.toInt

  def unary_-(): Sign =
    this match {
      case Positive => Negative
      case Negative => Positive
      case Zero => Zero
    }

  def *(that: Sign): Sign =
    Sign(this.toInt * that.toInt)

  def **(that: Int): Sign =
    if (this == Negative && (that & 1) == 0) Positive else this

  def pow(that: Int): Sign =
    this ** that
}

object Sign {
  case object Zero extends Sign(0)
  case object Positive extends Sign(1)
  case object Negative extends Sign(-1)

  def apply(i: Int): Sign =
    if (i == 0) Zero else if (i > 0) Positive else Negative
  
  implicit final val SignAlgebra =
    new CommutativeMonoid[Sign] with Signed[Sign] with Order[Sign] {
      def empty: Sign = Positive
      def combine(a: Sign, b: Sign): Sign = a * b
      override def sign(a: Sign): Sign = a
      def signum(a: Sign): Int = a.toInt
      def abs(a: Sign): Sign = a.abs
      def compare(x: Sign, y: Sign): Int = x compare y
    }

  implicit final val SignMultiplicative =
    new MultiplicativeCommutativeMonoid[Sign] {
      def one: Sign = Positive
      def times(a: Sign, b: Sign): Sign = a * b
      override def pow(a: Sign, n: Int): Sign = a ** n
    }
}
