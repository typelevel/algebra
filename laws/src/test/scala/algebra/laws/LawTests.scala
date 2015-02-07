package algebra
package laws

import algebra.lattice._
import algebra.number._
import algebra.ring._
import algebra.std.all._
import algebra.std.Rat

import CheckSupport._

import org.typelevel.discipline.scalatest.Discipline
import org.scalatest.FunSuite

class LawTests extends FunSuite with Discipline {

  implicit val byteLattice: Lattice[Byte] = ByteMinMaxLattice
  implicit val shortLattice: Lattice[Short] = ShortMinMaxLattice
  implicit val intLattice: Lattice[Int] = IntMinMaxLattice
  implicit val longLattice: Lattice[Long] = LongMinMaxLattice

  checkAll("Boolean", OrderLaws[Boolean].order)
  checkAll("Boolean", LogicLaws[Boolean].bool)
  checkAll("Boolean", LatticePartialOrderLaws[Boolean].boundedLatticePartialOrder)

  checkAll("String", OrderLaws[String].order)
  checkAll("String", GroupLaws[String].monoid)

  {
    // TODO: test a type that has Eq but not Order
    implicit val g: Group[Int] = Group.additive[Int]
    checkAll("Option[Int]", OrderLaws[Option[Int]].order)
    checkAll("Option[Int]", GroupLaws[Option[Int]].monoid)
    checkAll("Option[String]", OrderLaws[Option[Int]].order)
    checkAll("Option[String]", GroupLaws[Option[Int]].monoid)
  }

  checkAll("List[Int]", OrderLaws[List[Int]].order)
  checkAll("List[Int]", GroupLaws[List[Int]].monoid)
  checkAll("List[String]", OrderLaws[List[Int]].order)
  checkAll("List[String]", GroupLaws[List[Int]].monoid)

  checkAll("Set[Int]", RingLaws[Set[Int]].semiring)
  checkAll("Set[String]", RingLaws[Set[Int]].semiring)

  checkAll("Map[Char, Int]", OrderLaws[Map[Char, Int]].order)
  checkAll("Map[Char, Int]", RingLaws[Map[Char, Int]].rng)
  checkAll("Map[Int, BigInt]", OrderLaws[Map[Int, BigInt]].order)
  checkAll("Map[Int, BigInt]", RingLaws[Map[Int, BigInt]].rng)

  checkAll("Byte", OrderLaws[Byte].order)
  checkAll("Byte", RingLaws[Byte].euclideanRing)
  checkAll("Byte", LatticeLaws[Byte].lattice)

  checkAll("Short", OrderLaws[Short].order)
  checkAll("Short", RingLaws[Short].euclideanRing)
  checkAll("Short", LatticeLaws[Short].lattice)

  checkAll("Char", OrderLaws[Char].order)

  checkAll("Int", OrderLaws[Int].order)
  checkAll("Int", RingLaws[Int].euclideanRing)
  checkAll("Int", LatticeLaws[Int].lattice)

  checkAll("Long", OrderLaws[Long].order)
  checkAll("Long", RingLaws[Long].euclideanRing)
  checkAll("Long", LatticeLaws[Long].lattice)

  checkAll("BigInt", BaseLaws[BigInt].isReal)
  checkAll("BigInt", RingLaws[BigInt].euclideanRing)

  checkAll("Rat", BaseLaws[Rat].isReal)
  checkAll("Rat", RingLaws[Rat].field)

  {
    implicit val tupEq: Eq[(Int, Int)] = new Eq[(Int, Int)] {
      def eqv(a: (Int, Int), b: (Int, Int)) = a == b
    }
    implicit val band = new Band[(Int, Int)] {
      def combine(a: (Int, Int), b: (Int, Int)) = (a._1, b._2)
    }
    checkAll("(Int, Int) Band", GroupLaws[(Int, Int)].band)
  }
}
