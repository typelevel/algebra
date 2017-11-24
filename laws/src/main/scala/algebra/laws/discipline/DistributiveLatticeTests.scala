package algebra
package laws
package discipline

import algebra.lattice.DistributiveLattice
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import cats.kernel.instances.boolean._

trait DistributiveLatticeTests[A] extends LatticeTests[A] {
  def laws: DistributiveLatticeLaws[A]

  def distributiveLattice(implicit arbA: Arbitrary[A], eqA: Eq[A]): RuleSet =
    new DefaultRuleSet(
      "distributiveLattice",
      Some(lattice),
      "distributiveLattice distribute" -> forAll(laws.distributive _)
    )
}

object DistributiveLatticeTests {
  def apply[A : DistributiveLattice]: DistributiveLatticeTests[A] =
    new DistributiveLatticeTests[A] { def laws: DistributiveLatticeLaws[A] = DistributiveLatticeLaws[A] }
}
