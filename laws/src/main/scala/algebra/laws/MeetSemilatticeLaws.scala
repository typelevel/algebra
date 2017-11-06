package algebra.laws

import cats.kernel.laws._

trait MeetSemilatticeLaws[A] extends SemilatticeLaws[A] {
  override implicit def S: MeetSemilattice[A]
}

object MeetSemilatticeLaws {
  def apply[A](implicit ev: MeetSemilattice[A]): MeetSemilatticeLaws[A] =
    new MeetSemilatticeLaws[A] { def S: MeetSemilatticeLaws[A] = ev }
}
