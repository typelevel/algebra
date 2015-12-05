package algebra
package std

import algebra.lattice.DistributiveLattice
import algebra.ring.Field
import algebra.std.util.StaticMethods
import java.lang.Math
import scala.annotation.tailrec

trait FloatInstances {
  implicit val floatAlgebra = new FloatAlgebra

  // Not bounded due to the presence of NaN (min(x, NaN) == NaN, max(x, NaN) == NaN)
  val FloatMinMaxLattice: DistributiveLattice[Float] =
    DistributiveLattice.minMax[Float](floatAlgebra)
}

/**
 * Due to the way floating-point equality works, this instance is not
 * lawful under equality, but is correct when taken as an
 * approximation of an exact value.
 *
 * If you would prefer an absolutely lawful fractional value, you'll
 * need to investigate rational numbers or more exotic types.
 */
class FloatAlgebra extends Field[Float] with Order[Float] with Serializable {

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
}
