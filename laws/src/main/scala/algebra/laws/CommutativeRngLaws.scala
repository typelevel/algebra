package algebra
package laws

import algebra.ring._

trait CommutativeRngLaws[A] extends CommutativeSemiringLaws[A] with RngLaws[A] {
  override implicit def S: CommutativeRng[A]
}

object CommutativeRngLaws {
  def apply[A](implicit ev: CommutativeRng[A]): CommutativeRngLaws[A] =
    new CommutativeRngLaws[A] { def S: CommutativeRng[A] = ev }
}
