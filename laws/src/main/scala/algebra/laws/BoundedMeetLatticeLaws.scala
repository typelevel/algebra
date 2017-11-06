package algebra.laws

import algebra.lattice.{BoundedMeetSemilattice, Lattice}

trait BoundedMeetLatticeLaws[A] extends LatticeLaws[A] with BoundedMeetSemilatticeLaws[A] {
  override implicit def S: Lattice[A] with BoundedMeetSemilattice[A]
}

object BoundedMeetLatticeLaws {
  def apply[A](implicit ev: Lattice[A] with BoundedMeetSemilattice[A]): BoundedMeetLatticeLaws[A] =
    new BoundedMeetLatticeLaws[A] { def S: Lattice[A] with BoundedMeetSemilattice[A] = ev }
}

