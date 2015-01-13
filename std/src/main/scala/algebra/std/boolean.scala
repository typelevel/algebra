package algebra
package std

import algebra.lattice.Bool

package object boolean extends BooleanInstances

trait BooleanInstances {
  implicit val booleanAlgebra = new BooleanAlgebra
}

class BooleanAlgebra extends Bool[Boolean] with Order[Boolean] {
  def compare(x: Boolean, y: Boolean): Int =
    if (x) { if (y) 0 else 1 } else { if (y) -1 else 0 }

  def zero: Boolean = false
  def one: Boolean = true
  def and(x: Boolean, y: Boolean): Boolean = x && y
  def or(x: Boolean, y: Boolean): Boolean = x || y
  def complement(x: Boolean): Boolean = !x
}
