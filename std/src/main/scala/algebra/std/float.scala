package algebra
package std

import algebra.lattice.DistributiveLattice
import algebra.ring.Field
import algebra.std.util.StaticMethods
import java.lang.Math

trait FloatInstances extends cats.kernel.std.FloatInstances {
  implicit val floatAlgebra: Field[Float] =
    new FloatAlgebra

  // Not bounded due to the presence of NaN
  val FloatMinMaxLattice: DistributiveLattice[Float] =
    DistributiveLattice.minMax[Float]
}

/**
 * Due to the way floating-point equality works, this instance is not
 * lawful under equality, but is correct when taken as an
 * approximation of an exact value.
 *
 * If you would prefer an absolutely lawful fractional value, you'll
 * need to investigate rational numbers or more exotic types.
 */
class FloatAlgebra extends Field[Float] with Serializable {

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
