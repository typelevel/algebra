package algebra.laws

import algebra.lattice._
import cats.kernel.laws.BoundedSemilatticeLaws

trait BoundedJoinSemilatticeLaws[A] extends JoinSemilatticeLaws[A] with BoundedSemilatticeLaws[A] {
  override implicit def S: BoundedJoinSemilattice[A]
}

object BoundedJoinSemilatticeLaws {
  def apply[A](implicit ev: BoundedJoinSemilattice[A]): BoundedJoinSemilatticeLaws[A] =
    new BoundedJoinSemilatticeLaws[A] { def S: BoundedJoinSemilattice[A] = ev }
}
