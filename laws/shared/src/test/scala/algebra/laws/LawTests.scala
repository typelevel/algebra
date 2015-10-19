package algebra
package laws

import algebra.lattice._
import algebra.ring._
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
  implicit val intLattice: BoundedDistributiveLattice[Int] = IntMinMaxLattice
  implicit val longLattice: BoundedDistributiveLattice[Long] = LongMinMaxLattice

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
  laws[LogicLaws, SimpleHeyting].check(_.heyting)
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

  laws[LatticeLaws, Set[Int]].check(_.distributiveLattice)
  laws[LatticeLaws, Set[Int]].check(_.boundedJoinLattice)
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
  laws[LatticeLaws, Int].check(_.boundedDistributiveLattice)

  {
    implicit val comrig: CommutativeRig[Int] = IntMinMaxLattice.asCommutativeRig
    laws[RingLaws, Int].check(_.commutativeRig)
  }

  laws[OrderLaws, Long].check(_.order)
  laws[RingLaws, Long].check(_.euclideanRing)
  laws[LatticeLaws, Long].check(_.boundedDistributiveLattice)

  laws[BaseLaws, BigInt].check(_.isReal)
  laws[RingLaws, BigInt].check(_.euclideanRing)

  laws[RingLaws, (Int, Int)].check(_.euclideanRing)

  {
    implicit val band = new Band[(Int, Int)] {
      def combine(a: (Int, Int), b: (Int, Int)) = (a._1, b._2)
    }
    checkAll("(Int, Int) Band", GroupLaws[(Int, Int)].band)
  }

  laws[OrderLaws, Unit].check(_.order)
  laws[RingLaws, Unit].check(_.ring)
  laws[RingLaws, Unit].check(_.multiplicativeMonoid)
  laws[LatticeLaws, Unit].check(_.boundedSemilattice)

  {
    /**
     *  Here is a more complex Semilattice, which is roughly: if one of the first items is bigger
     *  take that, else combine pairwise.
     */
    def lexicographicSemilattice[A: Semilattice: Eq, B: Semilattice]: Semilattice[(A, B)] =
      new Semilattice[(A, B)] {
      def combine(left: (A, B), right: (A, B)) =
        if (Eq.eqv(left._1, right._1)) {
          (left._1, Semilattice[B].combine(left._2, right._2))
        }
        else {
          val a = Semilattice[A].combine(left._1, right._1)
          if (Eq.eqv(a, left._1)) left
          else if (Eq.eqv(a, right._1)) right
          else (a, Semilattice[B].combine(left._2, right._2))
        }
    }
    implicit val setSemilattice: Semilattice[Set[Int]] = setLattice[Int].joinSemilattice
    implicit val longSemilattice: Semilattice[Long] = LongMinMaxLattice.joinSemilattice
    laws[GroupLaws, (Set[Int], Long)](groupLaws, implicitly).check(_.semilattice(lexicographicSemilattice))
  }
}

