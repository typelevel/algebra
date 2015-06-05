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

  implicit val intLattice: Lattice[Int] = IntMinMaxLattice

  checkAll("Boolean", OrderLaws[Boolean].order)
  checkAll("Boolean", LogicLaws[Boolean].bool)
  checkAll("Boolean", LatticePartialOrderLaws[Boolean].boundedLatticePartialOrder)

  checkAll("String", OrderLaws[String].order)
  checkAll("String", GroupLaws[String].monoid)

  checkAll("List[Int]", OrderLaws[List[Int]].order)
  checkAll("List[Int]", GroupLaws[List[Int]].monoid)
  checkAll("List[String]", OrderLaws[List[String]].order)
  checkAll("List[String]", GroupLaws[List[String]].monoid)

  checkAll("Set[Int]", RingLaws[Set[Int]].semiring)
  checkAll("Set[String]", RingLaws[Set[String]].semiring)

  checkAll("Map[String, Int]", RingLaws[Map[String, Int]].rng)
  checkAll("Map[Int, BigInt]", RingLaws[Map[Int, BigInt]].rng)

  checkAll("Int", OrderLaws[Int].order)
  checkAll("Int", RingLaws[Int].euclideanRing)
  checkAll("Int", LatticeLaws[Int].lattice)

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
