package algebra
package laws

import algebra.ring._

trait MultiplicativeCommutativeGroupLaws[A] extends MultiplicativeGroupLaws[A] with MultiplicativeCommutativeMonoidLaws[A] {
  override implicit def S: MultiplicativeCommutativeGroup[A]
}

object MultiplicativeCommutativeGroupLaws {
  def apply[A](implicit ev: MultiplicativeCommutativeGroup[A]): MultiplicativeCommutativeGroupLaws[A] =
    new MultiplicativeCommutativeGroupLaws[A] { def S: MultiplicativeCommutativeGroup[A] = ev }
}
