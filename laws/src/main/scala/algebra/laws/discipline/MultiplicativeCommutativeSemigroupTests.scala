package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}
import Prop._

trait MultiplicativeCommutativeSemigroupTests[A] extends MultiplicativeSemigroupTests[A] with CommutativeTests {

  def laws: MultiplicativeCommutativeSemigroupLaws[A]

  def multiplicativeCommutativeSemigroup(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRuleSet =
    new CommutativeRuleSet(
      "multiplicativeCommutativeSemigroup",
      multiplicativeSemigroup,
      None,
      "times commutative" -> forAll(laws.timesCommutative _)
    )
}

object MultiplicativeCommutativeSemigroupTests {
  def apply[A: MultiplicativeCommutativeSemigroup]: MultiplicativeCommutativeSemigroupTests[A] =
    new MultiplicativeCommutativeSemigroupTests[A] { def laws: MultiplicativeCommutativeSemigroupLaws[A] = MultiplicativeCommutativeSemigroupLaws[A] }
}
