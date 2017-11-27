package algebra
package laws

import algebra.ring._
import cats.kernel.laws._


trait AdditiveCommutativeSemigroupLaws[A] extends AdditiveSemigroupLaws[A] {
  override implicit def S: AdditiveCommutativeSemigroup[A]

  def plusCommutative(x: A, y: A): IsEq[A] =
    S.plus(x, y) <-> S.plus(y, x)

}

object AdditiveCommutativeSemigroupLaws {
  def apply[A](implicit ev: AdditiveCommutativeSemigroup[A]): AdditiveCommutativeSemigroupLaws[A] =
    new AdditiveCommutativeSemigroupLaws[A] { def S: AdditiveCommutativeSemigroup[A] = ev }
}
