package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait MultiplicativeCommutativeMonoidTests[A] extends MultiplicativeCommutativeSemigroupTests[A] with MultiplicativeMonoidTests[A] {
  def laws: MultiplicativeCommutativeMonoidLaws[A]

  def multiplicativeCommutativeMonoid(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRuleSet =
    new CommutativeRuleSet(
      "multiplicativeCommutativeMonoid",
      multiplicativeMonoid,
      Some(multiplicativeCommutativeSemigroup)
    )
}

object MultiplicativeCommutativeMonoidTests {
  def apply[A: MultiplicativeCommutativeMonoid]: MultiplicativeCommutativeMonoidTests[A] =
    new MultiplicativeCommutativeMonoidTests[A] { def laws: MultiplicativeCommutativeMonoidLaws[A] = MultiplicativeCommutativeMonoidLaws[A] }
}
