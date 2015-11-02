package algebra
package ring

import lattice.Bool

/**
 * A Boolean ring is a ring whose multiplication is idempotent, that is
 * `a⋅a = a` for all elements ''a''. This property also implies `a+a = 0`
 * for all ''a'', and `a⋅b = b⋅a` (commutativity of multiplication).
 *
 * Every Boolean ring is equivalent to a Boolean algebra.
 * See [[BoolRing.asBool]] for details.
 */
trait BoolRing[A] extends Any with BoolRng[A] with CommutativeRing[A] { self =>

  /**
   * Every Boolean ring gives rise to a Boolean algebra:
   *  - 0 and 1 are preserved;
   *  - ring multiplication (`times`) corresponds to `and`;
   *  - ring addition (`plus`) corresponds to `xor`;
   *  - `a or b` is then defined as `a xor b xor (a and b)`;
   *  - complement (`¬a`) is defined as `a xor 1`.
   *
   * `BoolRing.asBool.asBoolRing` gives back the original `BoolRing`.
   *
   * @see [[algebra.lattice.Bool.asBoolRing]]
   */
  override def asBool: Bool[A] = new BoolFromBoolRing(self)
}

private[ring] class BoolFromBoolRing[A](orig: BoolRing[A]) extends GenBoolFromBoolRng(orig) with Bool[A] {
  def one: A = orig.one
  def complement(a: A): A = orig.plus(orig.one, a)
  override def without(a: A, b: A): A = super[GenBoolFromBoolRng].without(a, b)
  override def asBoolRing: BoolRing[A] = orig
}

object BoolRing extends RingFunctions {
  @inline final def apply[A](implicit r: BoolRing[A]): BoolRing[A] = r
}
