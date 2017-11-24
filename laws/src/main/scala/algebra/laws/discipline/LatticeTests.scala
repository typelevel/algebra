package algebra
package laws
package discipline

import algebra.lattice.Lattice
import org.scalacheck.{Arbitrary, Prop}
import Prop.forAll
import cats.kernel.instances.boolean._

trait LatticeTests[A] extends JoinSemilatticeTests[A] with MeetSemilatticeTests[A] {
  def laws: LatticeLaws[A]

  def lattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new RuleSet {
      def name: String = "lattice"
      def bases: Seq[(String, RuleSet)] = Nil
      def parents: Seq[RuleSet] = Seq(joinSemilattice, meetSemilattice)
      def props: Seq[(String, Prop)] = Seq(
        "lattice absorption" -> forAll(laws.absorption _)
      )
    }
}

object LatticeTests {
  def apply[A : Lattice]: LatticeTests[A] =
    new LatticeTests[A] { def laws: LatticeLaws[A] = LatticeLaws[A] }
}
