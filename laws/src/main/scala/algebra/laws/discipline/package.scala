package algebra.laws

import cats.kernel.Eq
import cats.kernel.laws.IsEq
import org.scalacheck.Prop

package object discipline {

  implicit def catsLawsIsEqToProp[A: Eq](isEq: IsEq[A]): Prop =
    cats.kernel.laws.discipline.catsLawsIsEqToProp[A](isEq)
}
