package algebra
package laws

import algebra.lattice._

trait BoundedJoinLatticeLaws[A] extends LatticeLaws[A] with BoundedJoinSemilatticeLaws[A] {
  override implicit def S: Lattice[A] with BoundedJoinSemilattice[A]
}

object BoundedJoinLatticeLaws {
  def apply[A](implicit ev: Lattice[A] with BoundedJoinSemilattice[A]): BoundedJoinLatticeLaws[A] =
    new BoundedJoinLatticeLaws[A] { def S: Lattice[A] with BoundedJoinSemilattice[A] = ev }
}
