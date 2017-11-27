package algebra
package laws

import algebra.ring._

trait AdditiveCommutativeGroupLaws[A] extends AdditiveGroupLaws[A] with AdditiveCommutativeMonoidLaws[A] {
  override implicit def S: AdditiveCommutativeGroup[A]
}

object AdditiveCommutativeGroupLaws {
  def apply[A](implicit ev: AdditiveCommutativeGroup[A]): AdditiveCommutativeGroupLaws[A] =
    new AdditiveCommutativeGroupLaws[A] { def S: AdditiveCommutativeGroup[A] = ev }
}
