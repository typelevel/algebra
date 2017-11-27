package algebra
package laws

import algebra.lattice._
import cats.kernel.laws._


trait DistributiveLatticeLaws[A] extends LatticeLaws[A] {
  override implicit def S: DistributiveLattice[A]

  def distributive(x: A, y: A, z: A)(implicit E: Eq[A]): IsEq[Boolean] =
    (E.eqv(S.join(x, S.meet(y, z)), S.meet(S.join(x, y), S.join(x, z))) &&
      E.eqv(S.meet(x, S.join(y, z)), S.join(S.meet(x, y), S.meet(x, z)))) <-> true

}

object DistributiveLatticeLaws {
  def apply[A](implicit ev: DistributiveLattice[A]): DistributiveLatticeLaws[A] =
    new DistributiveLatticeLaws[A] { def S: DistributiveLattice[A] = ev }
}
