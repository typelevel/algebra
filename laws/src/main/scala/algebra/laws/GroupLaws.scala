package algebra.laws

import algebra._
import algebra.ring._

import org.typelevel.discipline.Laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._

object GroupLaws {
  def apply[A : Eq : Arbitrary] = new GroupLaws[A] {
    def Equ = Eq[A]
    def Arb = implicitly[Arbitrary[A]]
  }
}

trait GroupLaws[A] extends Laws {

  implicit def Equ: Eq[A]
  implicit def Arb: Arbitrary[A]

  // groups

  def semigroup(implicit A: Semigroup[A]) = new GroupProperties(
    name = "semigroup",
    parent = None,
    Rules.associativity(A.combine),
    Rules.repeat1("combineN")(A.combineN),
    Rules.repeat2("combineN", "|+|")(A.combineN)(A.combine)
  )

  def monoid(implicit A: Monoid[A]) = new GroupProperties(
    name = "monoid",
    parent = Some(semigroup),
    Rules.leftIdentity(A.empty)(A.combine),
    Rules.rightIdentity(A.empty)(A.combine),
    Rules.repeat0("combineN", "id", A.empty)(A.combineN),
    Rules.collect0("combineAll", "id", A.empty)(A.combineAll),
    Rules.isId("isEmpty", A.empty)(A.isEmpty)
  )

  def group(implicit A: Group[A]) = new GroupProperties(
    name = "group",
    parent = Some(monoid),
    Rules.leftInverse(A.empty)(A.combine)(A.inverse),
    Rules.rightInverse(A.empty)(A.combine)(A.inverse),
    Rules.consistentInverse("remove")(A.remove)(A.combine)(A.inverse)
  )

  def commutativeGroup(implicit A: CommutativeGroup[A]) = new GroupProperties(
    name = "abelian group",
    parent = Some(group),
    Rules.commutative(A.combine)
  )

  // additive groups

  def additiveSemigroup(implicit A: AdditiveSemigroup[A]) = new AdditiveProperties(
    base = semigroup(A.additive),
    parent = None,
    Rules.repeat1("sumN")(A.sumN),
    Rules.repeat2("sumN", "+")(A.sumN)(A.plus)
  )

  def additiveMonoid(implicit A: AdditiveMonoid[A]) = new AdditiveProperties(
    base = monoid(A.additive),
    parent = Some(additiveSemigroup),
    Rules.repeat0("sumN", "zero", A.zero)(A.sumN),
    Rules.collect0("sum", "zero", A.zero)(A.sum)
  )

  def additiveGroup(implicit A: AdditiveGroup[A]) = new AdditiveProperties(
    base = group(A.additive),
    parent = Some(additiveMonoid),
    Rules.consistentInverse("subtract")(A.minus)(A.plus)(A.negate)
  )

  def additiveCommutativeGroup(implicit A: AdditiveCommutativeGroup[A]) = new AdditiveProperties(
    base = commutativeGroup(A.additive),
    parent = Some(additiveGroup)
  )


  // property classes

  class GroupProperties(
    name: String,
    parent: Option[GroupProperties],
    props: (String, Prop)*
  ) extends DefaultRuleSet(name, parent, props: _*)

  class AdditiveProperties(
    val base: GroupProperties,
    val parent: Option[AdditiveProperties],
    val props: (String, Prop)*
  ) extends RuleSet with HasOneParent {
    val name = base.name
    val bases = Seq("base" -> base)
  }
}
