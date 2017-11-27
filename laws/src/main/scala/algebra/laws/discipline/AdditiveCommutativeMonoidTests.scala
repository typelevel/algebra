package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait AdditiveCommutativeMonoidTests[A] extends AdditiveCommutativeSemigroupTests[A] with AdditiveMonoidTests[A] {
  def laws: AdditiveCommutativeMonoidLaws[A]

  def additiveCommutativeMonoid(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRuleSet =
    new CommutativeRuleSet(
      "additiveCommutativeMonoid",
      additiveMonoid,
      Some(additiveCommutativeSemigroup)
    )
}

object AdditiveCommutativeMonoidTests {
  def apply[A: AdditiveCommutativeMonoid]: AdditiveCommutativeMonoidTests[A] =
    new AdditiveCommutativeMonoidTests[A] { def laws: AdditiveCommutativeMonoidLaws[A] = AdditiveCommutativeMonoidLaws[A] }
}
