package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait CommutativeRngTests[A] extends RngTests[A] with CommutativeSemiringTests[A] {
  def laws: CommutativeRngLaws[A]

  def commutativeRng(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRingRuleSet =
    new CommutativeRingRuleSet(
      "commutativeRng",
      rng,
      Seq(commutativeSemiring)
    )
}

object CommutativeRngTests {
  def apply[A: CommutativeRng]: CommutativeRngTests[A] =
    new CommutativeRngTests[A] { def laws: CommutativeRngLaws[A] = CommutativeRngLaws[A] }
}
