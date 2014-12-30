package algebra
package laws

import algebra.number._
import algebra.ring._
import algebra.std.all._
import algebra.std.Rat

import CheckSupport._

import org.typelevel.discipline.scalatest.Discipline
import org.scalatest.FunSuite

class LawTests extends FunSuite with Discipline {
  checkAll("Boolean", OrderLaws[Boolean].order)
  checkAll("Boolean", LogicLaws[Boolean].bool)
  checkAll("Boolean", LatticePartialOrderLaws[Boolean].boundedLatticePartialOrder)

  checkAll("String", OrderLaws[String].order)
  checkAll("String", GroupLaws[String].monoid)

  checkAll("List[Int]", OrderLaws[List[Int]].order)
  checkAll("List[Int]", GroupLaws[List[Int]].monoid)
  checkAll("List[String]", OrderLaws[List[Int]].order)
  checkAll("List[String]", GroupLaws[List[Int]].monoid)

  checkAll("Set[Int]", RingLaws[Set[Int]].semiring)
  checkAll("Set[String]", RingLaws[Set[Int]].semiring)

  checkAll("Map[Char, Rat]", RingLaws[Map[String, Int]].rng)
  checkAll("Map[Int, BigInt]", RingLaws[Map[String, Int]].rng)

  checkAll("Int", OrderLaws[Int].order)
  checkAll("Int", RingLaws[Int].euclideanRing)

  checkAll("BigInt", BaseLaws[BigInt].isReal)
  checkAll("BigInt", RingLaws[BigInt].euclideanRing)

  checkAll("Rat", BaseLaws[Rat].isReal)
  checkAll("Rat", RingLaws[Rat].field)
}
