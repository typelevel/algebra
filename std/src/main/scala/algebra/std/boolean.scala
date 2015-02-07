package algebra
package std

import algebra.lattice.Bool
import algebra.ring.CommutativeRig

package object boolean extends BooleanInstances

trait BooleanInstances {
  implicit val booleanAlgebra: BooleanAlgebra =
    new BooleanAlgebra
}

class BooleanAlgebra extends Bool[Boolean] with Order[Boolean] with CommutativeRig[Boolean] with Serializable {
  def compare(x: Boolean, y: Boolean): Int =
    if (x) { if (y) 0 else 1 } else { if (y) -1 else 0 }

  override def eqv(x:Boolean, y:Boolean): Boolean = x == y
  override def neqv(x:Boolean, y:Boolean): Boolean = x != y
  override def gt(x: Boolean, y: Boolean): Boolean = x && !y
  override def lt(x: Boolean, y: Boolean): Boolean = !x && y
  override def gteqv(x: Boolean, y: Boolean): Boolean = x == y || x
  override def lteqv(x: Boolean, y: Boolean): Boolean = x == y || y

  override def min(x: Boolean, y: Boolean): Boolean = x && y
  override def max(x: Boolean, y: Boolean): Boolean = x || y

  def zero: Boolean = false
  def one: Boolean = true

  override def isZero(x: Boolean)(implicit ev: Eq[Boolean]): Boolean = !x
  override def isOne(x: Boolean)(implicit ev: Eq[Boolean]): Boolean = x

  def and(x: Boolean, y: Boolean): Boolean = x && y
  def or(x: Boolean, y: Boolean): Boolean = x || y
  def complement(x: Boolean): Boolean = !x

  def plus(a:Boolean, b:Boolean): Boolean = a || b
  override def pow(a:Boolean, b:Int): Boolean = a
  override def times(a:Boolean, b:Boolean): Boolean = a && b
}
