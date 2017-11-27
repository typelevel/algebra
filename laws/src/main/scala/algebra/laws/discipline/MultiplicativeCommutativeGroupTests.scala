package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait MultiplicativeCommutativeGroupTests[A] extends MultiplicativeCommutativeMonoidTests[A] with MultiplicativeGroupTests[A] {
  def laws: MultiplicativeCommutativeGroupLaws[A]

  def multiplicativeCommutativeGroup(implicit arbA: Arbitrary[A], eqA: Eq[A]): CommutativeRuleSet =
    new CommutativeRuleSet(
      "multiplicativeCommutativeGroup",
      multiplicativeGroup,
      Some(multiplicativeCommutativeMonoid)
    )
}

object MultiplicativeCommutativeGroupTests {
  def apply[A: MultiplicativeCommutativeGroup]: MultiplicativeCommutativeGroupTests[A] =
    new MultiplicativeCommutativeGroupTests[A] { def laws: MultiplicativeCommutativeGroupLaws[A] = MultiplicativeCommutativeGroupLaws[A] }
}
