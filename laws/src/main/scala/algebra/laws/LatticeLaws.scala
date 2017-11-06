package algebra.laws

import algebra._
import algebra.lattice._

import cats.kernel.laws._


trait LatticeLaws[A] extends JoinSemilatticeLaws[A] with MeetSemilatticeLaws[A] {
  override implicit def S: Lattice[A]

  def absorption(x: A, y: A)(implicit A: Eq[A]): IsEq[Boolean] =
    (S.join(x, S.meet(x, y)) === x) && (S.meet(x, S.join(x, y)) === x) <-> true
}

object LatticeLaws {
  def apply[A](implicit ev: Lattice[A]): LatticeLaws[A] =
    new LatticeLaws[A] { def S: Lattice[A] = ev }
}
