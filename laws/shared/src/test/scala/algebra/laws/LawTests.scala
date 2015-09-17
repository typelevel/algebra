package algebra
package laws

import algebra.lattice._
import algebra.macros._
import algebra.std.all._
import algebra.std.Rat

import org.typelevel.discipline.{Laws, Predicate}
import org.typelevel.discipline.scalatest.Discipline
import org.scalacheck.Arbitrary
import org.scalatest.FunSuite

trait LawTestsBase extends FunSuite with Discipline {

  implicit val byteLattice: Lattice[Byte] = ByteMinMaxLattice
  implicit val shortLattice: Lattice[Short] = ShortMinMaxLattice
  implicit val intLattice: Lattice[Int] = IntMinMaxLattice
  implicit val longLattice: Lattice[Long] = LongMinMaxLattice

  implicit def orderLaws[A: Eq: Arbitrary] = OrderLaws[A]
  implicit def groupLaws[A: Eq: Arbitrary] = GroupLaws[A]
  implicit def logicLaws[A: Eq: Arbitrary] = LogicLaws[A]


  implicit def latticeLaws[A: Eq: Arbitrary] = LatticeLaws[A]
  implicit def ringLaws[A: Eq: Arbitrary: Predicate] = RingLaws[A]
  implicit def baseLaws[A: Eq: Arbitrary] = BaseLaws[A]
  implicit def latticePartialOrderLaws[A: Eq: Arbitrary] = LatticePartialOrderLaws[A]

  case class LawChecker[L <: Laws](name: String, laws: L) {
    def check(f: L => L#RuleSet): Unit = checkAll(name, f(laws))
  }

  private[laws] def laws[L[_] <: Laws, A](implicit
      laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.tpe.toString + "]", laws)

  laws[OrderLaws, Boolean].check(_.order)
  laws[LogicLaws, Boolean].check(_.bool)
  laws[LatticePartialOrderLaws, Boolean].check(_.boundedLatticePartialOrder)

  laws[OrderLaws, String].check(_.order)
  laws[GroupLaws, String].check(_.monoid)

  {
    // TODO: test a type that has Eq but not Order
    implicit val g: Group[Int] = Group.additive[Int]
    laws[OrderLaws, Option[Int]].check(_.order)
    laws[GroupLaws, Option[Int]].check(_.monoid)
    laws[OrderLaws, Option[String]].check(_.order)
    laws[GroupLaws, Option[String]].check(_.monoid)
  }

  laws[OrderLaws, List[Int]].check(_.order)
  laws[GroupLaws, List[Int]].check(_.monoid)
  laws[OrderLaws, List[String]].check(_.order)
  laws[GroupLaws, List[String]].check(_.monoid)

  laws[LatticeLaws, Set[Int]].check(_.lattice)
  laws[OrderLaws, Set[Int]].check(_.partialOrder)
  laws[RingLaws, Set[Int]].check(_.semiring)
  laws[RingLaws, Set[String]].check(_.semiring)

  laws[OrderLaws, Map[Char, Int]].check(_.eqv)
  laws[RingLaws, Map[Char, Int]].check(_.rng)
  laws[OrderLaws, Map[Int, BigInt]].check(_.eqv)
  laws[RingLaws, Map[Int, BigInt]].check(_.rng)

  laws[OrderLaws, Byte].check(_.order)
  laws[RingLaws, Byte].check(_.euclideanRing)
  laws[LatticeLaws, Byte].check(_.lattice)

  laws[OrderLaws, Short].check(_.order)
  laws[RingLaws, Short].check(_.euclideanRing)
  laws[LatticeLaws, Short].check(_.lattice)

  laws[OrderLaws, Char].check(_.order)

  laws[OrderLaws, Int].check(_.order)
  laws[RingLaws, Int].check(_.euclideanRing)
  laws[LatticeLaws, Int].check(_.lattice)

  laws[OrderLaws, Long].check(_.order)
  laws[RingLaws, Long].check(_.euclideanRing)
  laws[LatticeLaws, Long].check(_.lattice)

  laws[BaseLaws, BigInt].check(_.isReal)
  laws[RingLaws, BigInt].check(_.euclideanRing)

  {
    checkAll("(String, String) Semigroup", GroupLaws[(String, String)].semigroup)
  }
}

