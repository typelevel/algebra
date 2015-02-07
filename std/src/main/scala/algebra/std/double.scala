package algebra
package std

import algebra.lattice.Lattice
import algebra.number.{IsReal, NRoot, Signed}
import algebra.ring.Field
import algebra.std.util.StaticMethods

import java.lang.Double.{ longBitsToDouble, doubleToLongBits }
import java.lang.Long.{ numberOfTrailingZeros, numberOfLeadingZeros }
import java.lang.Math

import scala.annotation.tailrec

trait DoubleInstances {
  implicit val doubleAlgebra = new DoubleAlgebra

  val DoubleMinMaxLattice = new Lattice[Double] {
    def join(x: Double, y: Double): Double = if (x > y) x else y
    def meet(x: Double, y: Double): Double = if (x < y) x else y
  }
}

class DoubleAlgebra extends Field[Double] with NRoot[Double] with Order[Double] with Signed[Double] with IsReal[Double] with Serializable {

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

  override def toDouble(x: Double): Double = x

  override def sqrt(x: Double): Double = Math.sqrt(x)
  def nroot(x: Double, k: Int): Double = Math.pow(x, 1.0 / k.toDouble)
  def fpow(x: Double, y: Double): Double = Math.pow(x, y)

  def signum(x: Double): Int = Math.signum(x).toInt
  def abs(x: Double): Double = Math.abs(x)

  def ceil(x: Double): Double = Math.ceil(x)
  def floor(x: Double): Double = Math.floor(x)
  def round(x: Double): Double = Math.rint(x)
  def isWhole(x: Double): Boolean = x % 1.0 == 0.0
}
