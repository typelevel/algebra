package algebra
package std

import algebra.number._
import algebra.ring._

package object bigInt extends BigIntInstances

trait BigIntInstances {
  implicit val bigIntAlgebra: BigIntAlgebra =
    new BigIntAlgebra
}

class BigIntAlgebra extends EuclideanRing[BigInt] with Order[BigInt] with Signed[BigInt] with IsIntegral[BigInt] with Serializable {

  val zero: BigInt = BigInt(0)
  val one: BigInt = BigInt(1)

  def compare(x: BigInt, y: BigInt): Int = x compare y
  override def eqv(x:BigInt, y:BigInt) = x == y
  override def neqv(x:BigInt, y:BigInt) = x != y
  override def gt(x: BigInt, y: BigInt) = x > y
  override def gteqv(x: BigInt, y: BigInt) = x >= y
  override def lt(x: BigInt, y: BigInt) = x < y
  override def lteqv(x: BigInt, y: BigInt) = x <= y
  override def min(x: BigInt, y: BigInt) = x min y
  override def max(x: BigInt, y: BigInt) = x max y

  def abs(x: BigInt): BigInt = x.abs
  def signum(x: BigInt): Int = x.signum

  def plus(a: BigInt, b: BigInt): BigInt = a + b
  def negate(a: BigInt): BigInt = -a
  override def minus(a: BigInt, b: BigInt): BigInt = a - b

  def times(a: BigInt, b: BigInt): BigInt = a * b
  def quot(a: BigInt, b: BigInt) = a / b
  def mod(a: BigInt, b: BigInt) = a % b
  override def quotmod(a:BigInt, b:BigInt) = a /% b

  override def pow(a: BigInt, k: Int): BigInt = a pow k

  def gcd(a:BigInt, b:BigInt) = a gcd b

  override def fromInt(n: Int): BigInt = BigInt(n)
  override def toDouble(n: BigInt): Double = n.toDouble
}
