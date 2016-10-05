package algebra
package instances

import algebra.ring._

package object bigDecimal extends BigDecimalInstances

trait BigDecimalInstances extends cats.kernel.instances.BigDecimalInstances {
  implicit val bigDecimalAlgebra: BigDecimalAlgebra =
    new BigDecimalAlgebra
}

class BigDecimalAlgebra extends Field[BigDecimal] with Serializable {

  val zero: BigDecimal = BigDecimal(0, java.math.MathContext.UNLIMITED)
  val one: BigDecimal = BigDecimal(1, java.math.MathContext.UNLIMITED)

  def plus(a: BigDecimal, b: BigDecimal): BigDecimal = a + b
  def negate(a: BigDecimal): BigDecimal = -a
  override def minus(a: BigDecimal, b: BigDecimal): BigDecimal = a - b

  def times(a: BigDecimal, b: BigDecimal): BigDecimal = a * b
  def div(a: BigDecimal, b: BigDecimal): BigDecimal = a / b

  override def pow(a: BigDecimal, k: Int): BigDecimal = a pow k

  override def fromInt(n: Int): BigDecimal = BigDecimal(n, java.math.MathContext.UNLIMITED)
  override def fromBigInt(n: BigInt): BigDecimal = BigDecimal(n, java.math.MathContext.UNLIMITED)
}
