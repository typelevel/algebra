package algebra.laws

trait BoundedDistributiveLatticeLaws[A] extends BoundedLatticeLaws[A] with DistributiveLatticeLaws[A] {
  override implicit def S: BoundedDistributiveLattice[A]
}

object BoundedDistributiveLatticeLaws {
  def apply[A](implicit ev: BoundedDistributiveLattice[A]): BoundedDistributiveLatticeLaws[A] =
    new BoundedDistributiveLatticeLaws[A] { def S: BoundedDistributiveLattice[A] = ev }
}
