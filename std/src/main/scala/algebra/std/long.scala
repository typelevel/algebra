package algebra
package std

import algebra.lattice._
import algebra.number._
import algebra.ring._

package object long extends LongInstances

trait LongInstances {
  implicit val longAlgebra = new LongAlgebra

  val LongMinMaxLattice = new Lattice[Long] {
    def join(x: Long, y: Long): Long = if (x > y) x else y
    def meet(x: Long, y: Long): Long = if (x < y) x else y
  }
}

class LongAlgebra extends EuclideanRing[Long]
    with Order[Long] with Hash[Long] with Signed[Long] with IsIntegral[Long] with Serializable {

  // high 32 bits from knuth's MMIX.
  // TODO: determine what this should actually be.
  def hash(x: Long): Int =
    ((x * 6364136223846793005L + 1442695040888963407L) >>> 32).toInt

  def compare(x: Long, y: Long): Int =
    if (x < y) -1 else if (x > y) 1 else 0

  def abs(x: Long): Long =
    if (x < 0) -x else x

  def signum(x: Long): Int =
    java.lang.Long.signum(x)

  def zero: Long = 0
  def one: Long = 1

  def plus(a: Long, b: Long): Long = a + b
  def negate(a: Long): Long = -a
  override def minus(a: Long, b: Long): Long = a - b

  def times(a: Long, b: Long): Long = a * b
  def quot(a: Long, b: Long) = a / b
  def mod(a: Long, b: Long) = a % b

  override def fromInt(n: Int): Long = n.toLong
  override def toDouble(n: Long): Double = n.toDouble
}
