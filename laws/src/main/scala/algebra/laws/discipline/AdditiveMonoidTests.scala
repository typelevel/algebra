package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}
import Prop._
import cats.kernel.instances.boolean._

trait AdditiveMonoidTests[A] extends AdditiveSemigroupTests[A] {

  def laws: AdditiveMonoidLaws[A]

  def additiveMonoid(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new DefaultRuleSet(
      "additiveMonoid",
      Some(additiveSemigroup),
      "left zero" -> forAll(laws.leftZero _),
      "right zero" -> forAll(laws.rightZero _),
      "sumN0" -> forAll(laws.sumN0 _),
      "sumAll" -> laws.sumAll,
      "isZero" -> forAll((a: A) => laws.isZero(a, eqA))
    )
}

object AdditiveMonoidTests {
  def apply[A: AdditiveMonoid]: AdditiveMonoidTests[A] =
    new AdditiveMonoidTests[A] { def laws: AdditiveMonoidLaws[A] = AdditiveMonoidLaws[A] }
}
