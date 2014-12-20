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

  def signed(implicit A: Signed[A]) = new SimpleRuleSet(
    name = "signed",
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
}
