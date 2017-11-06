package algebra.laws

trait BoundedMeetSemilatticeLaws[A] extends MeetSemilatticeLaws[A] with BoundedSemilatticeLaws[A] {
  override implicit def S: BoundedMeetSemilattice[A]
}

object BoundedMeetSemilatticeLaws {
  def apply[A](implicit ev: BoundedMeetSemilattice[A]): BoundedMeetSemilatticeLaws[A] =
    new BoundedMeetSemilatticeLaws[A] { def S: BoundedMeetSemilattice[A] = ev }
}
