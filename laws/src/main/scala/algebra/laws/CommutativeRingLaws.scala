package algebra
package laws

import algebra.ring._

trait CommutativeRingLaws[A] extends RingLaws[A] with CommutativeRigLaws[A] with CommutativeRngLaws[A] {
  override implicit def S: CommutativeRing[A]
}

object CommutativeRingLaws {
  def apply[A](implicit ev: CommutativeRing[A]): CommutativeRingLaws[A] =
    new CommutativeRingLaws[A] { def S: CommutativeRing[A] = ev }
}
