package algebra.laws

import algebra._
import algebra.number._

import org.typelevel.discipline.Laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._

import algebra.std.int._

object BaseLaws {
  def apply[A : Eq : Arbitrary] = new BaseLaws[A] {
    def Equ = Eq[A]
    def Arb = implicitly[Arbitrary[A]]
  }
}

trait BaseLaws[A] extends Laws {

  implicit def Equ: Eq[A]
  implicit def Arb: Arbitrary[A]

  def signed(implicit A: Signed[A]) = new BaseRuleSet(
    name = "signed",
    parent = None,
    bases = Seq.empty,
    "abs non-negative" -> forAll { (x: A) =>
      A.sign(A.abs(x)) ?!= Sign.Negative
    },
    "signum returns -1/0/1" -> forAll { (x: A) =>
      A.signum(x).abs ?<= 1
    },
    "signum is sign.toInt" -> forAll { (x: A) =>
      A.signum(x) ?== A.sign(x).toInt
    }
  )

  // uncomfortably, we can't test floor/found/ceil more closely
  // without requiring some idea of subtraction/distance.
  def isReal(implicit A: IsReal[A]) = new BaseRuleSet(
    name = "isReal",
    parent = Some(signed),
    bases = Seq("order" -> OrderLaws[A].order),
    "ceil(x) >= x" -> forAll { (x: A) =>
      val z = A.ceil(x)
      (z ?>= x) && A.isWhole(z)
    },
    "floor(x) <= x" -> forAll { (x: A) =>
      val z = A.floor(x)
      (z ?<= x) && A.isWhole(z)
    },
    "ceil(x) >= round(x) >= floor(x)" -> forAll { (x: A) =>
      val z = A.round(x)
      (z ?== A.floor(x)) || (z ?== A.ceil(x))
    },
    "isWhole" -> forAll { (x: A) =>
      !A.isWhole(x) ?|| A.eqv(x, A.round(x))
    }
  )

  class BaseRuleSet(
    val name: String,
    val parent: Option[RuleSet],
    val bases: Seq[(String, Laws#RuleSet)],
    val props: (String, Prop)*
  ) extends RuleSet with HasOneParent
}
