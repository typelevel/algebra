package algebra
package std

import algebra.number._
import algebra.ring._

object bigInt extends BigIntInstances

trait BigIntInstances {
  implicit val bigIntAlgebra = new BigIntAlgebra
}

class BigIntAlgebra extends EuclideanRing[BigInt]
    with Order[BigInt] with Signed[BigInt] with IsIntegral[BigInt] with Serializable {

  def compare(x: BigInt, y: BigInt): Int = x compare y

  def abs(x: BigInt): BigInt = x.abs

  def signum(x: BigInt): Int = x.signum

  val zero: BigInt = BigInt(0)
  val one: BigInt = BigInt(1)

  def plus(a: BigInt, b: BigInt): BigInt = a + b
  def negate(a: BigInt): BigInt = -a
  override def minus(a: BigInt, b: BigInt): BigInt = a - b

  def times(a: BigInt, b: BigInt): BigInt = a * b
  def quot(a: BigInt, b: BigInt) = a / b
  def mod(a: BigInt, b: BigInt) = a % b

  override def fromInt(n: Int): BigInt = BigInt(n)
  override def toDouble(n: BigInt): Double = n.toDouble
}
