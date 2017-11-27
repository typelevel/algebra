package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait CommutativeRingTests[A] extends RingTests[A] with CommutativeRigTests[A] with CommutativeRngTests[A] {
  def laws: CommutativeRingLaws[A]

  def commutativeRing(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRingRuleSet =
    new CommutativeRingRuleSet(
      "commutativeRing",
      rng,
      Seq(commutativeRig, commutativeRng)
    )
}

object CommutativeRingTests {
  def apply[A: CommutativeRing]: CommutativeRingTests[A] =
    new CommutativeRingTests[A] { def laws: CommutativeRingLaws[A] = CommutativeRingLaws[A] }
}
