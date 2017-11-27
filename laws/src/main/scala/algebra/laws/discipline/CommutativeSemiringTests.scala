package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}

trait CommutativeSemiringTests[A] extends SemiringTests[A] with MultiplicativeCommutativeSemigroupTests[A] {
  def laws: CommutativeSemiringLaws[A]

  def commutativeSemiring(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRingRuleSet =
    new CommutativeRingRuleSet(
      "commutativeSemiring",
      semiring,
      Nil
    )

  class CommutativeRingRuleSet(val name: String,
    val noncommutativeParent: RingRuleSet,
    val commutativeParents: Seq[CommutativeRingRuleSet],
    val props: (String, Prop)*
  ) extends RuleSet {
    def bases = Nil
    val parents = Seq(noncommutativeParent) ++ commutativeParents
  }
}

object CommutativeSemiringTests {
  def apply[A: CommutativeSemiring]: CommutativeSemiringTests[A] =
    new CommutativeSemiringTests[A] { def laws: CommutativeSemiringLaws[A] = CommutativeSemiringLaws[A] }
}
