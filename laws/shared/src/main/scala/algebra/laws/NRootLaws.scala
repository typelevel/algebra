package algebra
package laws

import algebra.ring.Field
import algebra.number.NRoot

import org.typelevel.discipline.Laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._

object NRootLaws {
  def apply[A: Eq: Field: Arbitrary] = new NRootLaws[A] {
    def Equ = Eq[A]
    def Arb = implicitly[Arbitrary[A]]
    def F = Field[A]
  }
}

trait NRootLaws[A] extends Laws {

  implicit def Equ: Eq[A]
  implicit def Arb: Arbitrary[A]
  implicit def F: Field[A]

  def nroot(implicit A: NRoot[A]) = new SimpleRuleSet(
    name = "nroot",
    Rules.serializable(A),
    "pow/nroot" -> forAll { (x: A, n0: Byte) =>
      val n = (n0 & 0xff) % 10 + 1
      val z1 = F.pow(A.nroot(x, n), n)
      val z2 = A.nroot(F.pow(x, n), n)
      (x ?== z1) && (x ?== z2)
    },
    "fpow(x, 1/n) = nroot(x, n)" -> forAll { (x: A, n0: Byte) =>
      val n = (n0 & 0xff) % 10 + 1
      A.nroot(x, n) ?== A.fpow(x, F.reciprocal(F.fromInt(n)))
    }
  )
}
