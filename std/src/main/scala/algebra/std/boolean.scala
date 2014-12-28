package algebra
package std

object boolean extends BooleanInstances

trait BooleanInstances {
  implicit val booleanEq: Eq[Boolean] = new BooleanEq
}

class BooleanEq extends Eq[Boolean] {
  def eqv(x: Boolean, y: Boolean): Boolean = x == y
}
