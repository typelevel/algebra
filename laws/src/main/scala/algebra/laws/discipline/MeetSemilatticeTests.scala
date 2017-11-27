package algebra
package laws
package discipline

import algebra.lattice.MeetSemilattice
import cats.kernel.laws.discipline.SemilatticeTests
import org.scalacheck.Arbitrary

trait MeetSemilatticeTests[A] extends SemilatticeTests[A] {
  def laws: MeetSemilatticeLaws[A]

  def meetSemilattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new DefaultRuleSet(
      "meetSemilattice",
      Some(semilattice)
    )
}

object MeetSemilatticeTests {
  def apply[A : MeetSemilattice]: MeetSemilatticeTests[A] =
    new MeetSemilatticeTests[A] { def laws: MeetSemilatticeLaws[A] = MeetSemilatticeLaws[A] }
}
