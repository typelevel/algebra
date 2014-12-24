package algebra
package std

import algebra.number._
import algebra.ring._

import org.scalacheck._
import Arbitrary.arbitrary

class Rat(val num: BigInt, val den: BigInt) {
  override def toString: String = s"($num/$den)"

  override def equals(that: Any): Boolean =
    that match {
      case r: Rat => num == r.num && den == r.den
      case _ => false
    }

  override def hashCode(): Int = (num, den).##
}

object Rat {
  def apply(num: BigInt, den: BigInt): Rat =
    if (den == 0) throw new ArithmeticException("/0")
    else if (den < 0) apply(-num, -den)
    else if (num == 0) new Rat(0, 1)
    else {
      val g = num gcd den
      new Rat(num / g, den / g)
    }

  implicit val ratArbitrary =
    Arbitrary(arbitrary[(BigInt, BigInt)].filter(_._2 != 0).map { case (n, d) => Rat(n, d) })
  implicit val ratAlgebra =
    new RatAlgebra
}

class RatAlgebra extends Field[Rat] with Order[Rat] with Signed[Rat] with IsReal[Rat] with Serializable {

  def compare(x: Rat, y: Rat): Int =
    (x.num * y.den) compare (y.num * x.den)

  def abs(x: Rat): Rat =
    Rat(x.num.abs, x.den)

  def signum(x: Rat): Int =
    x.num.signum

  val zero: Rat = Rat(0, 1)
  val one: Rat = Rat(1, 1)

  def plus(a: Rat, b: Rat): Rat =
    Rat((a.num * b.den) + (b.num * a.den), (a.den * b.den))

  def negate(a: Rat): Rat =
    Rat(-a.num, a.den)

  def times(a: Rat, b: Rat): Rat =
    Rat(a.num * b.num, a.den * b.den)

  def quot(a: Rat, b: Rat) = div(a, b)
  def mod(a: Rat, b: Rat) = zero

  override def reciprocal(a: Rat): Rat =
    if (a.num == 0) throw new ArithmeticException("/0") else Rat(a.den, a.num)

  def div(a: Rat, b: Rat): Rat =
    times(a, reciprocal(b))

  override def fromInt(n: Int): Rat =
    Rat(n, 1)

  override def toDouble(a: Rat): Double =
    a.num.toDouble / a.den.toDouble

  def ceil(a: Rat): Rat = Rat((a.num + a.den - 1) / a.den, 1)
  def floor(a: Rat): Rat = Rat(a.num / a.den, 1)
  def isWhole(a: Rat): Boolean = a.den == 1
  def round(a: Rat): Rat = Rat((a.num + (a.den / 2)) / a.den, 1)
}
