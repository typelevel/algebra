package algebra
package laws

import algebra.ring._

trait RngLaws[A] extends SemiringLaws[A] with AdditiveCommutativeGroupLaws[A] {
  override implicit def S: Rng[A]
}

object RngLaws {
  def apply[A](implicit ev: Rng[A]): RngLaws[A] =
    new RngLaws[A] { def S: Rng[A] = ev }
}
