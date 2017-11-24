package algebra
package laws
package discipline

import algebra.lattice.BoundedMeetSemilattice
import cats.kernel.laws.discipline.BoundedSemilatticeTests
import org.scalacheck.{Arbitrary, Prop}

trait BoundedMeetSemilatticeTests[A] extends BoundedSemilatticeTests[A] with MeetSemilatticeTests[A] {
  def laws: BoundedMeetSemilatticeLaws[A]

  def boundedMeetSemilattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new RuleSet {
      def name: String = "boundedMeetSemilattice"
      def bases: Seq[(String, RuleSet)] = Nil
      def parents: Seq[RuleSet] = Seq(boundedSemilattice, meetSemilattice)
      def props: Seq[(String, Prop)] = Nil
    }
}

object BoundedMeetSemilatticeTests {
  def apply[A : BoundedMeetSemilattice]: BoundedMeetSemilatticeTests[A] =
    new BoundedMeetSemilatticeTests[A] { def laws: BoundedMeetSemilatticeLaws[A] = BoundedMeetSemilatticeLaws[A] }
}
