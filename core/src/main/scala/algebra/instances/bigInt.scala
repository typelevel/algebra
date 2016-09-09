package algebra
package instances

import algebra.ring._

package object bigInt extends BigIntInstances

trait BigIntInstances extends cats.kernel.instances.BigIntInstances {
  implicit val bigIntAlgebra: BigIntAlgebra =
    new BigIntAlgebra
}

class BigIntAlgebra extends EuclideanRing[BigInt] with Serializable {

  val zero: BigInt = BigInt(0)
  val one: BigInt = BigInt(1)

  def plus(a: BigInt, b: BigInt): BigInt = a + b
  def negate(a: BigInt): BigInt = -a
  override def minus(a: BigInt, b: BigInt): BigInt = a - b

  def times(a: BigInt, b: BigInt): BigInt = a * b
  def quot(a: BigInt, b: BigInt) = a / b
  def mod(a: BigInt, b: BigInt) = a % b
  override def quotmod(a:BigInt, b:BigInt) = a /% b

  override def pow(a: BigInt, k: Int): BigInt = a pow k

  override def fromInt(n: Int): BigInt = BigInt(n)
}
