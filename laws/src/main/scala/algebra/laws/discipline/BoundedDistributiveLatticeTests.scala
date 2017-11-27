package algebra
package laws
package discipline

import algebra.lattice.BoundedDistributiveLattice
import org.scalacheck.{Arbitrary, Prop}

trait BoundedDistributiveLatticeTests[A] extends DistributiveLatticeTests[A] with BoundedLatticeTests[A] {
  def laws: BoundedDistributiveLatticeLaws[A]

  def boundedDistributiveLattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new RuleSet {
      def name: String = "boundedDistributiveLattice"
      def bases: Seq[(String, RuleSet)] = Nil
      def parents: Seq[RuleSet] = Seq(distributiveLattice, boundedLattice)
      def props: Seq[(String, Prop)] = Nil
    }
}

object BoundedDistributiveLatticeTests {
  def apply[A : BoundedDistributiveLattice]: BoundedDistributiveLatticeTests[A] =
    new BoundedDistributiveLatticeTests[A] {
      def laws: BoundedDistributiveLatticeLaws[A] = BoundedDistributiveLatticeLaws[A]
    }
}
