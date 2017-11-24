package algebra
package laws
package discipline

import algebra.lattice.BoundedJoinSemilattice
import cats.kernel.laws.discipline.BoundedSemilatticeTests
import org.scalacheck.{Arbitrary, Prop}

trait BoundedJoinSemilatticeTests[A] extends BoundedSemilatticeTests[A] with JoinSemilatticeTests[A] {
  def laws: BoundedJoinSemilatticeLaws[A]

  def boundedJoinSemilattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new RuleSet {
      def name: String = "boundedJoinSemilattice"
      def bases: Seq[(String, RuleSet)] = Nil
      def parents: Seq[RuleSet] = Seq(boundedSemilattice, joinSemilattice)
      def props: Seq[(String, Prop)] = Nil
    }
}

object BoundedJoinSemilatticeTests {
  def apply[A : BoundedJoinSemilattice]: BoundedJoinSemilatticeTests[A] =
    new BoundedJoinSemilatticeTests[A] { def laws: BoundedJoinSemilatticeLaws[A] = BoundedJoinSemilatticeLaws[A] }
}
