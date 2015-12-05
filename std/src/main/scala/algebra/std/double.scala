package algebra
package std

import algebra.lattice.DistributiveLattice
import algebra.ring.Field
import algebra.std.util.StaticMethods

import java.lang.Double.{ longBitsToDouble, doubleToLongBits }
import java.lang.Long.{ numberOfTrailingZeros, numberOfLeadingZeros }
import java.lang.Math

import scala.annotation.tailrec

trait DoubleInstances {
  implicit val doubleAlgebra = new DoubleAlgebra

  // This is not Bounded due to the presence of NaN
  val DoubleMinMaxLattice: DistributiveLattice[Double] =
    DistributiveLattice.minMax[Double](doubleAlgebra)
}

/**
 * Due to the way floating-point equality works, this instance is not
 * lawful under equality, but is correct when taken as an
 * approximation of an exact value.
 *
 * If you would prefer an absolutely lawful fractional value, you'll
 * need to investigate rational numbers or more exotic types.
 */
class DoubleAlgebra extends Field[Double] with Order[Double] with Serializable {

  def compare(x: Double, y: Double) =
    java.lang.Double.compare(x, y)

  override def eqv(x:Double, y:Double) = x == y
  override def neqv(x:Double, y:Double) = x != y
  override def gt(x: Double, y: Double) = x > y
  override def gteqv(x: Double, y: Double) = x >= y
  override def lt(x: Double, y: Double) = x < y
  override def lteqv(x: Double, y: Double) = x <= y
  override def min(x: Double, y: Double) = Math.min(x, y)
  override def max(x: Double, y: Double) = Math.max(x, y)

  def zero: Double = 0.0
  def one: Double = 1.0

  def plus(x: Double, y: Double): Double = x + y
  def negate(x: Double): Double = -x
  override def minus(x: Double, y: Double): Double = x - y

  def times(x: Double, y: Double): Double = x * y
  def div(x: Double, y: Double): Double = x / y
  override def reciprocal(x: Double): Double = 1.0 / x
  override def pow(x: Double, y: Int): Double = Math.pow(x, y.toDouble)

  def quot(x: Double, y: Double): Double = (x - (x % y)) / y
  def mod(x: Double, y: Double): Double = x % y

  override def quotmod(x: Double, y: Double): (Double, Double) = {
    val m = x % y
    ((x - m) / y, m)
  }

  def gcd(x: Double, y: Double): Double = StaticMethods.gcd(x, y)

  override def fromInt(x: Int): Double = x.toDouble
  override def fromDouble(x: Double): Double = x
}
