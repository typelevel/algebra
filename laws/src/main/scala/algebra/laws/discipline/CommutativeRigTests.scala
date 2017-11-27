package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait CommutativeRigTests[A] extends RigTests[A] with CommutativeSemiringTests[A] {
  def laws: CommutativeRigLaws[A]

  def commutativeRig(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRingRuleSet =
    new CommutativeRingRuleSet(
      "commutativeRig",
      rig,
      Seq(commutativeSemiring)
    )
}

object CommutativeRigTests {
  def apply[A: CommutativeRig]: CommutativeRigTests[A] =
    new CommutativeRigTests[A] { def laws: CommutativeRigLaws[A] = CommutativeRigLaws[A] }
}
