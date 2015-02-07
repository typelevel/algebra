package algebra
package std

import algebra.lattice._
import algebra.number._
import algebra.ring._
import algebra.std.util.StaticMethods

package object byte extends ByteInstances

trait ByteInstances {
  implicit val byteAlgebra = new ByteAlgebra

  val ByteMinMaxLattice = new Lattice[Byte] {
    def join(x: Byte, y: Byte): Byte = if (x > y) x else y
    def meet(x: Byte, y: Byte): Byte = if (x < y) x else y
  }
}

class ByteAlgebra extends EuclideanRing[Byte]
    with Order[Byte] with Signed[Byte] with IsIntegral[Byte] with Serializable {

  def compare(x: Byte, y: Byte): Int =
    if (x < y) -1 else if (x > y) 1 else 0

  override def eqv(x: Byte, y: Byte) = x == y
  override def neqv(x: Byte, y: Byte) = x != y
  override def gt(x: Byte, y: Byte) = x > y
  override def gteqv(x: Byte, y: Byte) = x >= y
  override def lt(x: Byte, y: Byte) = x < y
  override def lteqv(x: Byte, y: Byte) = x <= y

  def abs(x: Byte): Byte =
    if (x < 0) (-x).toByte else x

  def signum(x: Byte): Int =
    x.toInt

  def zero: Byte = 0
  def one: Byte = 1

  def plus(x: Byte, y: Byte): Byte = (x + y).toByte
  def negate(x: Byte): Byte = (-x).toByte
  override def minus(x: Byte, y: Byte): Byte = (x - y).toByte

  def times(x: Byte, y: Byte): Byte = (x * y).toByte
  def quot(x: Byte, y: Byte) = (x / y).toByte
  def mod(x: Byte, y: Byte) = (x % y).toByte

  override def pow(x: Byte, y: Int): Byte =
    Math.pow(x.toDouble, y.toDouble).toByte

  def gcd(x: Byte, y: Byte): Byte =
    StaticMethods.gcd(x.toLong, y.toLong).toByte

  override def fromInt(n: Int): Byte = n.toByte
  override def toDouble(n: Byte): Double = n.toDouble
}
