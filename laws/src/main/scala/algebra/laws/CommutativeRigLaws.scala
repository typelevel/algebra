package algebra
package laws

import algebra.ring._

trait CommutativeRigLaws[A] extends RigLaws[A] with CommutativeSemiringLaws[A] with MultiplicativeCommutativeMonoidLaws[A] {
  override implicit def S: CommutativeRig[A]
}

object CommutativeRigLaws {
  def apply[A](implicit ev: CommutativeRig[A]): CommutativeRigLaws[A] =
    new CommutativeRigLaws[A] { def S: CommutativeRig[A] = ev }
}
