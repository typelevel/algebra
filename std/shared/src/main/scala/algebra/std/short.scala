package algebra
package std

import algebra.lattice._
import algebra.number._
import algebra.ring._
import algebra.std.util.StaticMethods

package object short extends ShortInstances

trait ShortInstances {
  implicit val shortAlgebra = new ShortAlgebra

  val ShortMinMaxLattice: Lattice[Short] =
    Lattice.minMax[Short](shortAlgebra)
}

class ShortAlgebra extends EuclideanRing[Short]
    with Order[Short] with Signed[Short] with IsIntegral[Short] with Serializable {

  def compare(x: Short, y: Short): Int =
    if (x < y) -1 else if (x > y) 1 else 0

  override def eqv(x: Short, y: Short) = x == y
  override def neqv(x: Short, y: Short) = x != y
  override def gt(x: Short, y: Short) = x > y
  override def gteqv(x: Short, y: Short) = x >= y
  override def lt(x: Short, y: Short) = x < y
  override def lteqv(x: Short, y: Short) = x <= y

  def abs(x: Short): Short =
    if (x < 0) (-x).toShort else x

  def signum(x: Short): Int =
    x.toInt

  def zero: Short = 0
  def one: Short = 1

  def plus(x: Short, y: Short): Short = (x + y).toShort
  def negate(x: Short): Short = (-x).toShort
  override def minus(x: Short, y: Short): Short = (x - y).toShort

  def times(x: Short, y: Short): Short = (x * y).toShort
  def quot(x: Short, y: Short) = (x / y).toShort
  def mod(x: Short, y: Short) = (x % y).toShort

  override def pow(x: Short, y: Int): Short =
    Math.pow(x.toDouble, y.toDouble).toShort

  def gcd(x: Short, y: Short): Short =
    StaticMethods.gcd(x.toLong, y.toLong).toShort

  override def fromInt(n: Int): Short = n.toShort
  override def toDouble(n: Short): Double = n.toDouble
}
