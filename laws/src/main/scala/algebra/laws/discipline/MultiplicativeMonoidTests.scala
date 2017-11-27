package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.{Arbitrary, Prop}
import Prop._
import cats.kernel.instances.boolean._

trait MultiplicativeMonoidTests[A] extends MultiplicativeSemigroupTests[A] {

  def laws: MultiplicativeMonoidLaws[A]

  def multiplicativeMonoid(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new DefaultRuleSet(
      "multiplicativeMonoid",
      Some(multiplicativeSemigroup),
      "left one" -> forAll(laws.leftOne _),
      "right one" -> forAll(laws.rightOne _),
      "pow0" -> forAll(laws.pow0 _),
      "product" -> laws.product,
      "isOne" -> forAll((a: A) => laws.isOne(a, eqA))
    )
}

object MultiplicativeMonoidTests {
  def apply[A: MultiplicativeMonoid]: MultiplicativeMonoidTests[A] =
    new MultiplicativeMonoidTests[A] { def laws: MultiplicativeMonoidLaws[A] = MultiplicativeMonoidLaws[A] }
}
