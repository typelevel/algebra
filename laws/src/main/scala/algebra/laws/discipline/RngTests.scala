package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary}

trait RngTests[A] extends SemiringTests[A] with AdditiveCommutativeGroupTests[A] {
  def laws: RngLaws[A]

  def rng(implicit arbA: Arbitrary[A], eqA: Eq[A]): RingRuleSet =
    new RingRuleSet(
      "rng",
      additiveCommutativeGroup,
      multiplicativeSemigroup,
      Seq(semiring)
    )
}

object RngTests {
  def apply[A: Rng]: RngTests[A] =
    new RngTests[A] { def laws: RngLaws[A] = RngLaws[A] }
}
