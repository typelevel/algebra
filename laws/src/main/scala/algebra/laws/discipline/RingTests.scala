package algebra.laws.discipline

import algebra.laws.RingLaws
import algebra.ring.Ring
import cats.kernel.Eq
import org.scalacheck.Arbitrary

trait RingTests[A] extends RigTests[A] with RngTests[A] {
  def laws: RingLaws[A]

  def ring(implicit arbA: Arbitrary[A], eqA: Eq[A]): RingRuleSet =
    new RingRuleSet(
      "ring",
      additiveCommutativeGroup,
      multiplicativeMonoid,
      Seq(rig, rng)
    )
}

object RingTests {
  def apply[A: Ring]: RingTests[A] =
    new RingTests[A] { def laws: RingLaws[A] = RingLaws[A] }
}
