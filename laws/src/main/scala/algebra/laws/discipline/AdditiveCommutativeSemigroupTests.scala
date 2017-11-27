package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}
import Prop._

trait AdditiveCommutativeSemigroupTests[A] extends AdditiveSemigroupTests[A] with CommutativeTests {

  def laws: AdditiveCommutativeSemigroupLaws[A]

  def additiveCommutativeSemigroup(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRuleSet =
    new CommutativeRuleSet(
      "additiveCommutativeSemigroup",
      additiveSemigroup,
      None,
      "plus commutative" -> forAll(laws.plusCommutative _)
    )
}

object AdditiveCommutativeSemigroupTests {
  def apply[A: AdditiveCommutativeSemigroup]: AdditiveCommutativeSemigroupTests[A] =
    new AdditiveCommutativeSemigroupTests[A] { def laws: AdditiveCommutativeSemigroupLaws[A] = AdditiveCommutativeSemigroupLaws[A] }
}
