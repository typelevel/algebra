package algebra
package laws

import algebra.number._
import algebra.ring._
import algebra.std.int._

import org.typelevel.discipline.scalatest.Discipline
import org.scalatest.FunSuite

class LawTests extends FunSuite with Discipline {

  checkAll("Int",      RingLaws[Int].euclideanRing)
  // checkAll("Long",     RingLaws[Long].euclideanRing)
  // checkAll("BigInt",   RingLaws[BigInt].euclideanRing)
  // checkAll("Rational", RingLaws[Rational].field)
  // checkAll("Real",     RingLaws[Real].field)
  //
  //checkAll("Levenshtein distance", BaseLaws[String].metricSpace)
  //checkAll("BigInt",               BaseLaws[BigInt].metricSpace)
  //
  // checkAll("(Int,Int)",           RingLaws[(Int, Int)].ring)
  // checkAll("(Rational,Rational)", RingLaws[(Rational, Rational)].ring)
  //
  // checkAll("List[Int]",   GroupLaws[List[Int]].monoid)
  // checkAll("Vector[Int]", GroupLaws[Vector[Int]].monoid)
  // checkAll("Set[Int]",    GroupLaws[Set[Int]](spire.optional.genericEq.generic, implicitly).monoid)
  // checkAll("String[Int]", GroupLaws[String].monoid)
  // checkAll("Array[Int]",  GroupLaws[Array[Int]].monoid)
  // 
  // checkAll("Order[Int]", OrderLaws[Int].order)
}
