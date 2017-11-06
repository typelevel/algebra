package algebra.laws

trait DistributiveLatticeLaws[A] extends LatticeLaws[A] {
  override implicit def S: DistributiveLattice[A]

  def distributive(x: A, y: A)(implicit ev: Eq[A]) =
    ((S.join(x, S.meet(y, z)) === S.meet(S.join(x, y), S.join(x, z))) &&
      (S.meet(x, S.join(y, z)) === S.join(S.meet(x, y), S.meet(x, z)))) <-> true

}

object DistributiveLatticeLaws {
  def apply[A](implicit ev: DistributiveLattice[A]): DistributiveLatticeLaws[A] =
    new DistributiveLatticeLaws[A] { def S: DistributiveLattice[A] = ev }
}
