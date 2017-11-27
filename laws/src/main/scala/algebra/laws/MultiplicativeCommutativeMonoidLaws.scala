package algebra
package laws

import algebra.ring._

trait MultiplicativeCommutativeMonoidLaws[A] extends MultiplicativeMonoidLaws[A] with MultiplicativeCommutativeSemigroupLaws[A] {
  override implicit def S: MultiplicativeCommutativeMonoid[A]
}

object MultiplicativeCommutativeMonoidLaws {
  def apply[A](implicit ev: MultiplicativeCommutativeMonoid[A]): MultiplicativeCommutativeMonoidLaws[A] =
  new MultiplicativeCommutativeMonoidLaws[A] { def S: MultiplicativeCommutativeMonoid[A] = ev }
}
