package algebra
package laws

import org.typelevel.discipline.Laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._

object OrderLaws {
  def apply[A: Eq: Arbitrary] = new OrderLaws[A] {
    def Equ = Eq[A]
    def Arb = implicitly[Arbitrary[A]]
  }
}

trait OrderLaws[A] extends Laws {

  implicit def Equ: Eq[A]
  implicit def Arb: Arbitrary[A]

  def partialOrder(implicit A: PartialOrder[A]) = new OrderProperties(
    name = "partialOrder",
    parent = None,
    "reflexitivity" -> forAll { (x: A) =>
      A.lteqv(x, x)
    },
    "antisymmetry" -> forAll { (x: A, y: A) =>
      !(A.lteqv(x, y) && A.lteqv(y, x)) || (x == y)
    },
    "transitivity" -> forAll { (x: A, y: A, z: A) =>
      !(A.lteqv(x, y) && A.lteqv(y, z)) || A.lteqv(x, z)
    },
    "gteqv" -> forAll { (x: A, y: A) =>
      A.lteqv(x, y) == A.gteqv(y, x)
    },
    "lt" -> forAll { (x: A, y: A) =>
      A.lt(x, y) == A.lteqv(x, y) && A.neqv(x, y)
    },
    "gt" -> forAll { (x: A, y: A) =>
      A.lt(x, y) == A.gt(y, x)
    }
  )

  def order(implicit A: Order[A]) = new OrderProperties(
    name = "order",
    parent = Some(partialOrder),
    "totality" -> forAll { (x: A, y: A) =>
      A.lteqv(x, y) || A.lteqv(y, x)
    }
  )

  class OrderProperties(
    name: String,
    parent: Option[OrderProperties],
    props: (String, Prop)*
  ) extends DefaultRuleSet(name, parent, props: _*)
}
