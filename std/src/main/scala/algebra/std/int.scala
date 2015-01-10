package algebra
package std

import algebra.lattice._
import algebra.number._
import algebra.ring._

package object int extends IntInstances

trait IntInstances {
  implicit val intAlgebra = new IntAlgebra

  val IntMinMaxLattice = new Lattice[Int] {
    def join(x: Int, y: Int): Int = if (x > y) x else y
    def meet(x: Int, y: Int): Int = if (x < y) x else y
  }
}

class IntAlgebra extends EuclideanRing[Int]
    with Order[Int] with Signed[Int] with IsIntegral[Int] with Serializable {

  def compare(x: Int, y: Int): Int =
    if (x < y) -1 else if (x > y) 1 else 0

  def abs(x: Int): Int =
    if (x < 0) -x else x

  def signum(x: Int): Int =
    java.lang.Integer.signum(x)

  def zero: Int = 0
  def one: Int = 1

  def plus(a: Int, b: Int): Int = a + b
  def negate(a: Int): Int = -a
  override def minus(a: Int, b: Int): Int = a - b

  def times(a: Int, b: Int): Int = a * b
  def quot(a: Int, b: Int) = a / b
  def mod(a: Int, b: Int) = a % b

  override def fromInt(n: Int): Int = n
  override def toDouble(n: Int): Double = n.toDouble
}
