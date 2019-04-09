package algebra
package laws

import algebra.lattice._
import algebra.ring._
import algebra.instances.all._
import algebra.instances.BigDecimalAlgebra

import catalysts.Platform
import catalysts.macros.TypeTagM // need this import for implicit macros

import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.Discipline
import org.scalacheck.{Arbitrary, Cogen}
import Arbitrary.arbitrary
import org.scalactic.anyvals.{PosZDouble, PosInt, PosZInt}
import org.scalatest.FunSuite
import org.scalatest.prop.Configuration
import scala.collection.immutable.BitSet
import scala.util.Random

class LawTests extends FunSuite with Configuration with Discipline {

  lazy val checkConfiguration: PropertyCheckConfiguration =
    PropertyCheckConfiguration(
      minSuccessful = if (Platform.isJvm) PosInt(50) else PosInt(5),
      maxDiscardedFactor = if (Platform.isJvm) PosZDouble(5.0) else PosZDouble(50.0),
      minSize = PosZInt(0),
      sizeRange = if (Platform.isJvm) PosZInt(10) else PosZInt(5),
      workers = PosInt(1))

  // The scalacheck defaults (100,100) are too high for Scala-js, so we reduce to 10/100.
  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    if (Platform.isJvm) PropertyCheckConfiguration(sizeRange = 100, minSuccessful = 100)
    else PropertyCheckConfiguration(sizeRange = 10, minSuccessful = 100)

  implicit val byteLattice: Lattice[Byte] = ByteMinMaxLattice
  implicit val shortLattice: Lattice[Short] = ShortMinMaxLattice
  implicit val intLattice: BoundedDistributiveLattice[Int] = IntMinMaxLattice
  implicit val longLattice: BoundedDistributiveLattice[Long] = LongMinMaxLattice

  implicit def orderLaws[A: Cogen: Eq: Arbitrary] = OrderLaws[A]
  implicit def groupLaws[A: Eq: Arbitrary] = GroupLaws[A]
  implicit def logicLaws[A: Eq: Arbitrary] = LogicLaws[A]
  implicit def deMorganLaws[A: Eq: Arbitrary] = DeMorganLaws[A]

  implicit def latticeLaws[A: Eq: Arbitrary] = LatticeLaws[A]
  implicit def ringLaws[A: Eq: Arbitrary: AdditiveMonoid] = RingLaws[A]
  implicit def baseLaws[A: Eq: Arbitrary] = BaseLaws[A]
  implicit def latticePartialOrderLaws[A: Eq: Arbitrary] = LatticePartialOrderLaws[A]

  case class HasEq[A](a: A)

  object HasEq {
    implicit def hasEq[A: Eq]: Eq[HasEq[A]] = Eq.by(_.a)
    implicit def hasEqArbitrary[A: Arbitrary]: Arbitrary[HasEq[A]] =
      Arbitrary(arbitrary[A].map(HasEq(_)))
    implicit def hasEqCogen[A: Cogen]: Cogen[HasEq[A]] =
      Cogen[A].contramap[HasEq[A]](_.a)
  }

  case class HasPartialOrder[A](a: A)

  object HasPartialOrder {
    implicit def hasPartialOrder[A: PartialOrder]: PartialOrder[HasPartialOrder[A]] = PartialOrder.by(_.a)
    implicit def hasPartialOrderArbitrary[A: Arbitrary]: Arbitrary[HasPartialOrder[A]] =
      Arbitrary(arbitrary[A].map(HasPartialOrder(_)))
    implicit def hasPartialOrderCogen[A: Cogen]: Cogen[HasPartialOrder[A]] =
      Cogen[A].contramap[HasPartialOrder[A]](_.a)
  }

  case class LawChecker[L <: Laws](name: String, laws: L) {
    def check(f: L => L#RuleSet): Unit = checkAll(name, f(laws))
  }

  private[laws] def laws[L[_] <: Laws, A](implicit
      lws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] = laws[L, A]("")

  private[laws] def laws[L[_] <: Laws, A](extraTag: String)(implicit
      laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.name.toString + (if(extraTag != "") "@@" + extraTag else "") + "]", laws)

  laws[OrderLaws, Boolean].check(_.order)
  laws[LogicLaws, Boolean].check(_.bool)
  laws[DeMorganLaws, SimpleHeyting].check(_.logic(Logic.fromHeyting(Heyting[SimpleHeyting])))
  laws[LogicLaws, SimpleHeyting].check(_.heyting)
  laws[DeMorganLaws, SimpleDeMorgan].check(_.deMorgan)
  laws[DeMorganLaws, Boolean].check(_.deMorgan(DeMorgan.fromBool(Bool[Boolean])))
  laws[LatticePartialOrderLaws, Boolean].check(_.boundedLatticePartialOrder)
  laws[RingLaws, Boolean].check(_.boolRing(booleanRing))

  // ensure that Bool[A].asBoolRing is a valid BoolRing
  laws[RingLaws, Boolean]("ring-from-bool").check(_.boolRing(Bool[Boolean].asBoolRing))

  // ensure that BoolRing[A].asBool is a valid Bool
  laws[LogicLaws, Boolean]("bool-from-ring").check(_.bool(new BoolFromBoolRing(booleanRing)))

  laws[OrderLaws, String].check(_.order)
  laws[GroupLaws, String].check(_.monoid)

  {
    laws[OrderLaws, Option[HasEq[Int]]].check(_.eqv)
    laws[OrderLaws, Option[HasPartialOrder[Int]]].check(_.partialOrder)
    laws[OrderLaws, Option[Int]].check(_.order)
    laws[GroupLaws, Option[Int]].check(_.monoid)
    laws[OrderLaws, Option[HasEq[String]]].check(_.eqv)
    laws[OrderLaws, Option[HasPartialOrder[String]]].check(_.partialOrder)
    laws[OrderLaws, Option[String]].check(_.order)
    laws[GroupLaws, Option[String]].check(_.monoid)
  }

  laws[OrderLaws, List[HasEq[Int]]].check(_.eqv)
  laws[OrderLaws, List[HasPartialOrder[Int]]].check(_.partialOrder)
  laws[OrderLaws, List[Int]].check(_.order)
  laws[GroupLaws, List[Int]].check(_.monoid)
  laws[OrderLaws, List[HasEq[String]]].check(_.eqv)
  laws[OrderLaws, List[HasPartialOrder[String]]].check(_.partialOrder)
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
  laws[RingLaws, Map[Char, Int]].check(_.semiring)
  laws[OrderLaws, Map[Int, BigInt]].check(_.eqv)
  laws[RingLaws, Map[Int, BigInt]].check(_.semiring)

  laws[OrderLaws, Byte].check(_.order)
  laws[RingLaws, Byte].check(_.commutativeRing)
  laws[LatticeLaws, Byte].check(_.lattice)

  laws[OrderLaws, Short].check(_.order)
  laws[RingLaws, Short].check(_.commutativeRing)
  laws[LatticeLaws, Short].check(_.lattice)

  laws[OrderLaws, Char].check(_.order)

  laws[OrderLaws, Int].check(_.order)
  laws[RingLaws, Int].check(_.commutativeRing)
  laws[LatticeLaws, Int].check(_.boundedDistributiveLattice)

  {
    laws[RingLaws, Int].check(_.commutativeRig)
  }

  laws[OrderLaws, Long].check(_.order)
  laws[RingLaws, Long].check(_.commutativeRing)
  laws[LatticeLaws, Long].check(_.boundedDistributiveLattice)

  laws[RingLaws, BigInt].check(_.commutativeRing)

  laws[RingLaws, FPApprox[Float]].check(_.field)
  laws[RingLaws, FPApprox[Double]].check(_.field)

  // let's limit our BigDecimal-related tests to the JVM for now.
  if (Platform.isJvm) {

    {
      // we need a less intense arbitrary big decimal implementation.
      // this keeps the values relatively small/simple and avoids some
      // of the numerical errors we might hit.
      implicit val arbBigDecimal: Arbitrary[BigDecimal] =
        Arbitrary(arbitrary[Int].map(x => BigDecimal(x, java.math.MathContext.UNLIMITED)))

      // BigDecimal does have numerical errors, so we can't pass all of
      // the field laws.
      laws[RingLaws, BigDecimal].check(_.ring)
    }

    {
      // We check the full field laws using a FPApprox.
      val mc = java.math.MathContext.DECIMAL32
      implicit val arbBigDecimal: Arbitrary[BigDecimal] =
        Arbitrary(arbitrary[Double].map(x => BigDecimal(x, mc)))
      implicit val epsBigDecimal = FPApprox.Epsilon.bigDecimalEpsilon(mc)
      implicit val algebra = FPApprox.fpApproxAlgebra(new BigDecimalAlgebra(mc), Order[BigDecimal], epsBigDecimal)
      laws[RingLaws, FPApprox[BigDecimal]].check(_.field(algebra))
    }
  } else ()

  {
    implicit val arbBitSet: Arbitrary[BitSet] =
      Arbitrary(arbitrary[List[Byte]].map(s => BitSet(s.map(_ & 0xff): _*)))
    laws[LogicLaws, BitSet].check(_.generalizedBool)
  }

  laws[RingLaws, (Int, Int)].check(_.ring)

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

  // Rational tests do not return on Scala-js, so we make them JVM only.
  if (Platform.isJvm) laws[RingLaws, Rat].check(_.field)
  else ()

  test("Field.fromDouble with subnormal") {
    val n = 1.9726888167225064E-308
    val bd = new java.math.BigDecimal(n)
    val unscaledValue = new BigInt(bd.unscaledValue)
    val expected =
      if (bd.scale > 0) {
        Ring[Rat].fromBigInt(unscaledValue) / Ring[Rat].fromBigInt(BigInt(10).pow(bd.scale))
      } else {
        Ring[Rat].fromBigInt(unscaledValue * BigInt(10).pow(-bd.scale))
      }
    assert(Field.fromDouble[Rat](n) == expected)
  }
}
