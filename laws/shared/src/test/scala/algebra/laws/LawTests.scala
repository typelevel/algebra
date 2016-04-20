package algebra
package laws

import algebra.lattice._
import algebra.ring._
import algebra.macros._
import algebra.std.all._

import org.typelevel.discipline.{Laws, Predicate}
import org.typelevel.discipline.scalatest.Discipline
import org.scalacheck.{Arbitrary, Cogen, Gen}, Arbitrary.arbitrary
import org.scalatest.FunSuite
import scala.util.Random

trait LawTestsBase extends FunSuite with Discipline {
  // Pending https://github.com/rickynils/scalacheck/issues/226
  implicit val cogenBigInt: Cogen[BigInt] = Cogen[Long].contramap(_.toLong)

  implicit val byteLattice: Lattice[Byte] = ByteMinMaxLattice
  implicit val shortLattice: Lattice[Short] = ShortMinMaxLattice
  implicit val intLattice: BoundedDistributiveLattice[Int] = IntMinMaxLattice
  implicit val longLattice: BoundedDistributiveLattice[Long] = LongMinMaxLattice

  implicit def orderLaws[A: Eq: Arbitrary: Cogen] = OrderLaws[A]
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
      lws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] = laws[L, A]("")

  private[laws] def laws[L[_] <: Laws, A](extraTag: String)(implicit
      laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.tpe.toString + (if(extraTag != "") "@@" + extraTag else "") + "]", laws)

  laws[OrderLaws, Boolean].check(_.order)
  laws[LogicLaws, Boolean].check(_.bool)
  laws[LogicLaws, SimpleHeyting].check(_.heyting)
  laws[LatticePartialOrderLaws, Boolean].check(_.boundedLatticePartialOrder)
  laws[RingLaws, Boolean].check(_.boolRing(booleanRing))

  // ensure that Bool[A].asBoolRing is a valid BoolRing
  laws[RingLaws, Boolean]("ring-from-bool").check(_.boolRing(Bool[Boolean].asBoolRing))

  // ensure that BoolRing[A].asBool is a valid Bool
  laws[LogicLaws, Boolean]("bool-from-ring").check(_.bool(new BoolFromBoolRing(booleanRing)))

  laws[OrderLaws, String].check(_.order)
  laws[GroupLaws, String].check(_.monoid)

  {
    // TODO: test a type that has Eq but not Order
    implicit val g: Group[Int] = AdditiveGroup.additive[Int]
    laws[OrderLaws, Option[Int]].check(_.order)
    laws[GroupLaws, Option[Int]].check(_.monoid)
    laws[OrderLaws, Option[String]].check(_.order)
    laws[GroupLaws, Option[String]].check(_.monoid)
  }

  laws[OrderLaws, List[Int]].check(_.order)
  laws[GroupLaws, List[Int]].check(_.monoid)
  laws[OrderLaws, List[String]].check(_.order)
  laws[GroupLaws, List[String]].check(_.monoid)

  laws[LogicLaws, Set[Byte]].check(_.generalizedBool)
  laws[RingLaws, Set[Byte]].check(_.boolRng(setBoolRng[Byte]))
  laws[LogicLaws, Set[Byte]]("bool-from-rng").check(_.generalizedBool(new GenBoolFromBoolRng(setBoolRng)))
  laws[RingLaws, Set[Byte]]("rng-from-bool").check(_.boolRng(GenBool[Set[Byte]].asBoolRing))
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

  laws[RingLaws, BigInt].check(_.euclideanRing)

  laws[RingLaws, (Int, Int)].check(_.euclideanRing)

  {
    implicit val band = new Band[(Int, Int)] {
      def combine(a: (Int, Int), b: (Int, Int)) = (a._1, b._2)
    }
    checkAll("(Int, Int) Band", GroupLaws[(Int, Int)].band)
  }

  laws[OrderLaws, Unit].check(_.order)
  laws[RingLaws, Unit].check(_.commutativeRing)
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
    laws[LatticeLaws, (Set[Int], Long)].check(_.semilattice(lexicographicSemilattice))
  }

  {
    // In order to check the monoid laws for `Order[N]`, we need
    // `Arbitrary[Order[N]]` and `Eq[Order[N]]` instances.
    // Here we have a bit of a hack to create these instances.
    val nMax: Int = 13
    final case class N(n: Int) { require(n >= 0 && n < nMax) }
    // The arbitrary `Order[N]` values are created by mapping N values to random
    // integers.
    implicit val arbNOrder: Arbitrary[Order[N]] = Arbitrary(arbitrary[Int].map { seed =>
      val order = new Random(seed).shuffle(Vector.range(0, nMax))
      Order.by { (n: N) => order(n.n) }
    })
    // The arbitrary `Eq[N]` values are created by mapping N values to random
    // integers.
    implicit val arbNEq: Arbitrary[Eq[N]] = Arbitrary(arbitrary[Int].map { seed =>
      val mapping = new Random(seed).shuffle(Vector.range(0, nMax))
      Eq.by { (n: N) => mapping(n.n) }
    })
    // needed because currently we don't have Vector instances
    implicit val vectorNEq: Eq[Vector[N]] = Eq.fromUniversalEquals
    // The `Eq[Order[N]]` instance enumerates all possible `N` values in a
    // `Vector` and considers two `Order[N]` instances to be equal if they
    // result in the same sorting of that vector.
    implicit val NOrderEq: Eq[Order[N]] = Eq.by { order: Order[N] =>
      Vector.tabulate(nMax)(N).sorted(order.toOrdering)
    }
    implicit val NEqEq: Eq[Eq[N]] = new Eq[Eq[N]] {
      def eqv(a: Eq[N], b: Eq[N]) =
        Iterator.tabulate(nMax)(N)
          .flatMap { x => Iterator.tabulate(nMax)(N).map((x, _)) }
          .forall { case (x, y) => a.eqv(x, y) == b.eqv(x, y) }
    }

    implicit val monoidOrderN: Monoid[Order[N]] = Order.whenEqualMonoid[N]
    laws[GroupLaws, Order[N]].check(_.monoid)

    {
      implicit val bsEqN: BoundedSemilattice[Eq[N]] = Eq.allEqualBoundedSemilattice[N]
      laws[GroupLaws, Eq[N]].check(_.boundedSemilattice)
    }
    {
      implicit val sEqN: Semilattice[Eq[N]] = Eq.anyEqualSemilattice[N]
      laws[GroupLaws, Eq[N]].check(_.semilattice)
    }
  }

  laws[OrderLaws, Int]("fromOrdering").check(_.order(Order.fromOrdering[Int]))
  laws[OrderLaws, Array[Int]].check(_.order)
  laws[OrderLaws, Array[Int]].check(_.partialOrder)
}

