package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}
import Prop._

trait SemiringTests[A] extends AdditiveCommutativeMonoidTests[A] with MultiplicativeSemigroupTests[A] {
  def laws: SemiringLaws[A]

  def semiring(implicit arbA: Arbitrary[A], eqA: Eq[A]): RingRuleSet =
    new RingRuleSet(
      "semiring",
      additiveCommutativeMonoid,
      multiplicativeSemigroup,
      Nil,
      "left distributive" -> forAll(laws.leftDistributive _),
      "right distributive" -> forAll(laws.rightDistributive _),
      "zero left annihilates" -> forAll(laws.zeroLeftAnnihilates _),
      "zero right annihilates" -> forAll(laws.zeroRightAnnihilates _)
    )

  class RingRuleSet(
    val name: String,
    val additiveRuleSet: RuleSet,
    val multiplicativeRuleSet: RuleSet,
    val additionalParents: Seq[RingRuleSet],
    val props: (String, Prop)*
  ) extends RuleSet {
    def parents = additionalParents ++ Seq(additiveRuleSet, multiplicativeRuleSet)
    def bases = Nil
 }
}

object SemiringTests {
  def apply[A: Semiring]: SemiringTests[A] =
    new SemiringTests[A] { def laws: SemiringLaws[A] = SemiringLaws[A] }
}
