package algebra
package laws
package discipline

import algebra.lattice.JoinSemilattice
import cats.kernel.laws.discipline.SemilatticeTests
import org.scalacheck.Arbitrary

trait JoinSemilatticeTests[A] extends SemilatticeTests[A] {
  def laws: JoinSemilatticeLaws[A]

  def joinSemilattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new DefaultRuleSet(
      "joinSemilattice",
      Some(semilattice)
    )
}

object JoinSemilatticeTests {
  def apply[A : JoinSemilattice]: JoinSemilatticeTests[A] =
    new JoinSemilatticeTests[A] { def laws: JoinSemilatticeLaws[A] = JoinSemilatticeLaws[A] }
}
