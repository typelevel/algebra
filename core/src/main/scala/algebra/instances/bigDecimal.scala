package algebra
package instances

import algebra.ring._

package object bigDecimal extends BigDecimalInstances

trait BigDecimalInstances extends cats.kernel.instances.BigDecimalInstances {
  implicit val bigDecimalAlgebra: BigDecimalAlgebra =
    new BigDecimalAlgebra
}

class BigDecimalAlgebra extends Field[BigDecimal] with Serializable {

  val zero: BigDecimal = BigDecimal(0)
  val one: BigDecimal = BigDecimal(1)

  def plus(a: BigDecimal, b: BigDecimal): BigDecimal = a + b
  def negate(a: BigDecimal): BigDecimal = -a
  override def minus(a: BigDecimal, b: BigDecimal): BigDecimal = a - b

  def times(a: BigDecimal, b: BigDecimal): BigDecimal = a * b
  def div(a: BigDecimal, b: BigDecimal): BigDecimal = a / b
  def quot(a: BigDecimal, b: BigDecimal) = a.quot(b)
  def mod(a: BigDecimal, b: BigDecimal) = a.remainder(b)

  override def quotmod(a: BigDecimal, b: BigDecimal) = {
    val arr = a.bigDecimal.divideAndRemainder(b.bigDecimal)
    (BigDecimal(arr(0)), BigDecimal(arr(1)))
  }

  override def pow(a: BigDecimal, k: Int): BigDecimal = a pow k

  override def fromInt(n: Int): BigDecimal = BigDecimal(n)
}
