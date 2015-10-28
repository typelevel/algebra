package algebra
package ring

import lattice.Bool

/**
 * A Boolean ring is a ring whose multiplication is idempotent, that is
 * `a⋅a = a` for all elements ''a''. This property also implies `a+a = 0`
 * for all ''a'', and `a⋅b = b⋅a` (commutativity of multiplication).
 */
trait BoolRing[A] extends Any with CommutativeRing[A] { self =>
  override final def negate(x: A): A = x

  def asBool: Bool[A] = new Bool[A] {
    def zero: A = self.zero
    def one: A = self.one
    def and(a: A, b: A): A = self.times(a, b)
    def complement(a: A): A = self.plus(self.one, a)
    def or(a: A, b: A): A = self.plus(self.plus(a, b), self.times(a, b))
    override def asCommutativeRing: BoolRing[A] = self
  }
}

object BoolRing extends RingFunctions {
  @inline final def apply[A](implicit r: BoolRing[A]): BoolRing[A] = r
}
