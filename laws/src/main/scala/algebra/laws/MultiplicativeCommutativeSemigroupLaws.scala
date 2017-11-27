package algebra
package laws

import algebra.ring._
import cats.kernel.laws._

trait MultiplicativeCommutativeSemigroupLaws[A] extends MultiplicativeSemigroupLaws[A] {
  override implicit def S: MultiplicativeCommutativeSemigroup[A]

  def timesCommutative(x: A, y: A): IsEq[A] =
    S.times(x, y) <-> S.times(y, x)

}

object MultiplicativeCommutativeSemigroupLaws {
  def apply[A](implicit ev: MultiplicativeCommutativeSemigroup[A]): MultiplicativeCommutativeSemigroupLaws[A] =
    new MultiplicativeCommutativeSemigroupLaws[A] { def S: MultiplicativeCommutativeSemigroup[A] = ev }
}
