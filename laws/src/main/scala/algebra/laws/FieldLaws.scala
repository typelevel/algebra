package algebra
package laws

import algebra.ring._

trait FieldLaws[A] extends CommutativeRingLaws[A] with MultiplicativeCommutativeGroupLaws[A] {
  override implicit def S: Field[A]
}

object FieldLaws {
  def apply[A: Field]: FieldLaws[A] =
    new FieldLaws[A] { def S: Field[A] = implicitly }
}
