package algebra.laws

import algebra.lattice.JoinSemilattice
import cats.kernel.laws.SemilatticeLaws

trait JoinSemilatticeLaws[A] extends SemilatticeLaws[A] {
  override implicit def S: JoinSemilattice[A]
}

object JoinSemilatticeLaws {
  def apply[A](implicit ev: JoinSemilattice[A]): JoinSemilatticeLaws[A] =
    new JoinSemilatticeLaws[A] { def S: JoinSemilattice[A] = ev }
}
