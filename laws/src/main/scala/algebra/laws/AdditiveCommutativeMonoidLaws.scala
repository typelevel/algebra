package algebra
package laws

import algebra.ring._

trait AdditiveCommutativeMonoidLaws[A] extends AdditiveMonoidLaws[A] with AdditiveCommutativeSemigroupLaws[A] {
  override implicit def S: AdditiveCommutativeMonoid[A]
}

object AdditiveCommutativeMonoidLaws {
  def apply[A](implicit ev: AdditiveCommutativeMonoid[A]): AdditiveCommutativeMonoidLaws[A] =
  new AdditiveCommutativeMonoidLaws[A] { def S: AdditiveCommutativeMonoid[A] = ev }
}
