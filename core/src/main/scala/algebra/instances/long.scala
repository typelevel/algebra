package algebra
package instances

import algebra.lattice._
import algebra.ring._

package object long extends LongInstances

trait LongInstances extends cats.kernel.instances.LongInstances {
  implicit val longAlgebra: EuclideanRing[Long] =
    new LongAlgebra

  val LongMinMaxLattice: BoundedDistributiveLattice[Long] =
    BoundedDistributiveLattice.minMax[Long](Long.MinValue, Long.MaxValue)
}

class LongAlgebra extends EuclideanRing[Long] with Serializable {

  def zero: Long = 0
  def one: Long = 1

  def plus(x: Long, y: Long): Long = x + y
  def negate(x: Long): Long = -x
  override def minus(x: Long, y: Long): Long = x - y

  def times(x: Long, y: Long): Long = x * y
  def quot(x: Long, y: Long) = x / y
  def mod(x: Long, y: Long) = x % y

  override def pow(x: Long, y: Int): Long = StaticMethods.pow(x, y.toLong)

  override def fromInt(n: Int): Long = n.toLong
}
