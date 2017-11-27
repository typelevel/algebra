package algebra
package laws
package discipline

import algebra.ring._
import org.scalacheck.Arbitrary

trait FieldTests[A] extends CommutativeRingTests[A] with MultiplicativeCommutativeGroupTests[A] {
  def laws: FieldLaws[A]

  def field(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new RuleSet {
      def name = "field"
      def parents = Seq(commutativeRing, multiplicativeCommutativeGroup)
      def bases = Nil
      def props = Nil
    }
}

object FieldTests {
  def apply[A: Field]: FieldTests[A] =
    new FieldTests[A] { def laws: FieldLaws[A] = FieldLaws[A] }
}
