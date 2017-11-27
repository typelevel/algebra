package algebra.laws

import algebra._
import algebra.lattice._

import cats.kernel.laws._


trait HeytingLaws[A] extends BoundedDistributiveLatticeLaws[A] {
  override implicit def S: Heyting[A]

  def absorption(x: A, y: A)(implicit E: Eq[A]): IsEq[Boolean] =
    (E.eqv(S.join(x, S.meet(x, y)), x) && E.eqv(S.meet(x, S.join(x, y)), x)) <-> true
}

object HeytingLaws {
  def apply[A](implicit ev: Heyting[A]): HeytingLaws[A] =
    new HeytingLaws[A] { def S: Heyting[A] = ev }
}
