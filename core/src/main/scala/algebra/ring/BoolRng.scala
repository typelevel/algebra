package algebra
package ring

import lattice.GenBool

/**
 * A Boolean rng is a rng whose multiplication is idempotent, that is
 * `a⋅a = a` for all elements ''a''. This property also implies `a+a = 0`
 * for all ''a'', and `a⋅b = b⋅a` (commutativity of multiplication).
 *
 * Every `BoolRng` is equivalent to [[algebra.lattice.GenBool]].
 * See [[BoolRng.asBool]] for details.
 */
trait BoolRng[A] extends Any with Rng[A] { self =>
  override final def negate(x: A): A = x

  /**
   * Every Boolean rng gives rise to a Boolean algebra without top:
   *  - 0 is preserved;
   *  - ring multiplication (`times`) corresponds to `and`;
   *  - ring addition (`plus`) corresponds to `xor`;
   *  - `a or b` is then defined as `a xor b xor (a and b)`;
   *  - relative complement `a\b` is defined as `a xor (a and b)`.
   *
   * `BoolRng.asBool.asBoolRing` gives back the original `BoolRng`.
   *
   * @see [[algebra.lattice.GenBool.asBoolRing]]
   */
  def asBool: GenBool[A] = new GenBoolFromBoolRng(self)
}

private[ring] class GenBoolFromBoolRng[A](orig: BoolRng[A]) extends GenBool[A] {
  def zero: A = orig.zero
  def and(a: A, b: A): A = orig.times(a, b)
  def or(a: A, b: A): A = orig.plus(orig.plus(a, b), orig.times(a, b))
  def without(a: A, b: A): A = orig.plus(a, orig.times(a, b))
  override def asBoolRing: BoolRng[A] = orig
}

object BoolRng extends AdditiveGroupFunctions with MultiplicativeSemigroupFunctions {
  @inline final def apply[A](implicit r: BoolRng[A]): BoolRng[A] = r
}
