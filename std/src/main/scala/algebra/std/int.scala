package algebra
package std

import algebra.lattice._
import algebra.ring._
import algebra.std.util.StaticMethods

package object int extends IntInstances

trait IntInstances extends cats.kernel.std.IntInstances {
  implicit val intAlgebra: EuclideanRing[Int] =
    new IntAlgebra

  val IntMinMaxLattice: BoundedDistributiveLattice[Int] =
    BoundedDistributiveLattice.minMax[Int](Int.MinValue, Int.MaxValue)
}

class IntAlgebra extends EuclideanRing[Int] with Serializable {

  def zero: Int = 0
  def one: Int = 1

  def plus(x: Int, y: Int): Int = x + y
  def negate(x: Int): Int = -x
  override def minus(x: Int, y: Int): Int = x - y

  def times(x: Int, y: Int): Int = x * y
  def quot(x: Int, y: Int) = x / y
  def mod(x: Int, y: Int) = x % y

  override def pow(x: Int, y: Int): Int =
    StaticMethods.pow(x.toLong, y.toLong).toInt

  def gcd(x: Int, y: Int): Int =
    StaticMethods.gcd(x.toLong, y.toLong).toInt

  override def fromInt(n: Int): Int = n
}
