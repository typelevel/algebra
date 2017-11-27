package algebra
package laws

import algebra.ring._

trait CommutativeSemiringLaws[A] extends SemiringLaws[A] with MultiplicativeCommutativeSemigroupLaws[A] {
  override implicit def S: CommutativeSemiring[A]
}

object CommutativeSemiringLaws {
  def apply[A](implicit ev: CommutativeSemiring[A]): CommutativeSemiringLaws[A] =
    new CommutativeSemiringLaws[A] { def S: CommutativeSemiring[A] = ev }
}
