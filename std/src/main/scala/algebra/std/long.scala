package algebra
package std

import util.StaticMethods

import algebra.lattice._
import algebra.number._
import algebra.ring._

package object long extends LongInstances

trait LongInstances {
  implicit val longAlgebra = new LongAlgebra

  val LongMinMaxLattice: Lattice[Long] =
    Lattice.minMax[Long](longAlgebra)
}

class LongAlgebra extends EuclideanRing[Long]
    with Order[Long] with Signed[Long] with IsIntegral[Long] with Serializable {

  def compare(x: Long, y: Long): Int =
    if (x < y) -1 else if (x > y) 1 else 0

  override def eqv(x: Long, y: Long) = x == y
  override def neqv(x: Long, y: Long) = x != y
  override def gt(x: Long, y: Long) = x > y
  override def gteqv(x: Long, y: Long) = x >= y
  override def lt(x: Long, y: Long) = x < y
  override def lteqv(x: Long, y: Long) = x <= y

  def abs(x: Long): Long =
    if (x < 0) -x else x

  def signum(x: Long): Int =
    java.lang.Long.signum(x)

  def zero: Long = 0
  def one: Long = 1

  def plus(x: Long, y: Long): Long = x + y
  def negate(x: Long): Long = -x
  override def minus(x: Long, y: Long): Long = x - y

  def times(x: Long, y: Long): Long = x * y
  def quot(x: Long, y: Long) = x / y
  def mod(x: Long, y: Long) = x % y

  override def pow(x: Long, y: Int): Long = StaticMethods.pow(x, y.toLong)

  def gcd(x: Long, y: Long): Long = StaticMethods.gcd(x, y)

  override def fromInt(n: Int): Long = n.toLong
  override def toDouble(n: Long): Double = n.toDouble
}
