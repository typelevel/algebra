package algebra
package laws
package discipline

import algebra.lattice.BoundedLattice
import org.scalacheck.{Arbitrary, Prop}
import Prop.forAll
import cats.kernel.instances.boolean._

trait BoundedLatticeTests[A] extends LatticeTests[A] with BoundedJoinSemilatticeTests[A] with BoundedMeetSemilatticeTests[A] {
  def laws: BoundedLatticeLaws[A]

  def boundedLattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new RuleSet {
      def name: String = "boundedLattice"
      def bases: Seq[(String, RuleSet)] = Nil
      def parents: Seq[RuleSet] = Seq(lattice, boundedJoinSemilattice, boundedMeetSemilattice)
      def props: Seq[(String, Prop)] = Seq(
        "lattice absorption" -> forAll(laws.absorption _)
      )
    }
}

object BoundedLatticeTests {
  def apply[A : BoundedLattice]: BoundedLatticeTests[A] =
    new BoundedLatticeTests[A] { def laws: BoundedLatticeLaws[A] = BoundedLatticeLaws[A] }
}
