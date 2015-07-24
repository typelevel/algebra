package algebra
package std

import algebra.lattice.Lattice
import algebra.number.{IsReal, NRoot, Signed}
import algebra.ring.Field
import algebra.std.util.StaticMethods
import java.lang.Math
import scala.annotation.tailrec

trait FloatInstances {
  implicit val floatAlgebra = new FloatAlgebra

  val FloatMinMaxLattice: Lattice[Float] =
    Lattice.minMax[Float](floatAlgebra)
}

/**
 * Due to the way floating-point equality works, this instance is not
 * lawful under equality, but is correct when taken as an
 * approximation of an exact value.
 *
 * If you would prefer an absolutely lawful fractional value, you'll
 * need to investigate rational numbers or more exotic types.
 */
class FloatAlgebra extends Field[Float] with NRoot[Float] with Order[Float] with Signed[Float] with IsReal[Float] with Serializable {

  def compare(x: Float, y: Float) =
    java.lang.Float.compare(x, y)

  override def eqv(x:Float, y:Float) = x == y
  override def neqv(x:Float, y:Float) = x != y
  override def gt(x: Float, y: Float) = x > y
  override def gteqv(x: Float, y: Float) = x >= y
  override def lt(x: Float, y: Float) = x < y
  override def lteqv(x: Float, y: Float) = x <= y
  override def min(x: Float, y: Float) = Math.min(x, y)
  override def max(x: Float, y: Float) = Math.max(x, y)

  def zero: Float = 0.0F
  def one: Float = 1.0F

  def plus(x: Float, y: Float): Float = x + y
  def negate(x: Float): Float = -x
  override def minus(x: Float, y: Float): Float = x - y

  def times(x: Float, y: Float): Float = x * y
  def div(x: Float, y: Float): Float = x / y
  override def reciprocal(x: Float): Float = 1.0F / x

  override def pow(x: Float, y: Int): Float =
    Math.pow(x.toDouble, y.toDouble).toFloat

  def quot(x: Float, y: Float): Float = (x - (x % y)) / y
  def mod(x: Float, y: Float): Float = x % y

  override def quotmod(x: Float, y: Float): (Float, Float) = {
    val m = x % y
    ((x - m) / y, m)
  }

  def gcd(x: Float, y: Float): Float = StaticMethods.gcd(x, y)

  override def fromInt(x: Int): Float = x.toFloat
  override def fromDouble(x: Double): Float = x.toFloat

  override def toDouble(x: Float): Double = x.toDouble

  override def sqrt(x: Float): Float = Math.sqrt(x.toDouble).toFloat
  def nroot(x: Float, k: Int): Float = Math.pow(x.toDouble, 1.0 / k.toDouble).toFloat
  def fpow(x: Float, y: Float): Float = Math.pow(x.toDouble, y.toDouble).toFloat

  def signum(x: Float): Int = Math.signum(x).toInt
  def abs(x: Float): Float = Math.abs(x)

  def ceil(x: Float): Float = Math.ceil(x.toDouble).toFloat
  def floor(x: Float): Float = Math.floor(x.toDouble).toFloat
  def round(x: Float): Float = Platform.rint(x.toDouble).toFloat
  def isWhole(x: Float): Boolean = x % 1.0F == 0.0F
}
