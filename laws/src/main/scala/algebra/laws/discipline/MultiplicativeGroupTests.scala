package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}
import Prop._

trait MultiplicativeGroupTests[A] extends MultiplicativeMonoidTests[A] {

  def laws: MultiplicativeGroupLaws[A]

  def multiplicativeGroup(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new DefaultRuleSet(
      "multiplicativeGroup",
      Some(multiplicativeMonoid),
      "left reciprocal" -> forAll(laws.leftReciprocal _),
      "right reciprocal" -> forAll(laws.rightReciprocal _),
      "consistent div" -> forAll(laws.consistentDiv _)
    )
}

object MultiplicativeGroupTests {
  def apply[A: MultiplicativeGroup]: MultiplicativeGroupTests[A] =
    new MultiplicativeGroupTests[A] { def laws: MultiplicativeGroupLaws[A] = MultiplicativeGroupLaws[A] }
}
