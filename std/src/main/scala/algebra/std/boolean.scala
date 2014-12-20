package algebra
package std

object boolean {

  implicit val booleanEq: Eq[Boolean] = new BooleanEq

  class BooleanEq[A] extends Eq[Boolean] {
    def eqv(x: Boolean, y: Boolean): Boolean = x == y
  }
}
