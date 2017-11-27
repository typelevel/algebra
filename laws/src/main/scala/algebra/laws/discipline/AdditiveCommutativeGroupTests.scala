package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait AdditiveCommutativeGroupTests[A] extends AdditiveCommutativeMonoidTests[A] with AdditiveGroupTests[A] {
  def laws: AdditiveCommutativeGroupLaws[A]

  def additiveCommutativeGroup(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRuleSet =
    new CommutativeRuleSet(
      "additiveCommutativeGroup",
      additiveGroup,
      Some(additiveCommutativeMonoid)
    )
}

object AdditiveCommutativeGroupTests {
  def apply[A: AdditiveCommutativeGroup]: AdditiveCommutativeGroupTests[A] =
    new AdditiveCommutativeGroupTests[A] { def laws: AdditiveCommutativeGroupLaws[A] = AdditiveCommutativeGroupLaws[A] }
}
