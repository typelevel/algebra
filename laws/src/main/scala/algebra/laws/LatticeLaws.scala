package algebra.laws

import algebra._
import algebra.lattice._

import org.typelevel.discipline.Laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._

object LatticeLaws {
  def apply[A: Eq: Arbitrary] = new LatticeLaws[A] {
    def Equ = Eq[A]
    def Arb = implicitly[Arbitrary[A]]
  }
}

trait LatticeLaws[A] extends Laws {

  implicit def Equ: Eq[A]
  implicit def Arb: Arbitrary[A]

  def joinSemilattice(implicit A: JoinSemilattice[A]) = new LatticeProperties(
    name = "joinSemilattice",
    parents = Nil,
    Rules.associativity(A.join),
    Rules.commutative(A.join),
    Rules.idempotence(A.join)
  )

  def meetSemilattice(implicit A: MeetSemilattice[A]) = new LatticeProperties(
    name = "meetSemilattice",
    parents = Nil,
    Rules.associativity(A.meet),
    Rules.commutative(A.meet),
    Rules.idempotence(A.meet)
  )

  def lattice(implicit A: Lattice[A]) = new LatticeProperties(
    name = "lattice",
    parents = Seq(joinSemilattice, meetSemilattice),
    "absorption" -> forAll { (x: A, y: A) =>
      (A.join(x, A.meet(x, y)) ?== x) && (A.meet(x, A.join(x, y)) ?== x)
    }
  )

  def boundedJoinSemilattice(implicit A: BoundedJoinSemilattice[A]) = new LatticeProperties(
    name = "boundedJoinSemilattice",
    parents = Seq(joinSemilattice),
    Rules.leftIdentity(A.zero)(A.join),
    Rules.rightIdentity(A.zero)(A.join)
  )

  def boundedMeetSemilattice(implicit A: BoundedMeetSemilattice[A]) = new LatticeProperties(
    name = "boundedMeetSemilattice",
    parents = Seq(meetSemilattice),
    Rules.leftIdentity(A.one)(A.meet),
    Rules.rightIdentity(A.one)(A.meet)
  )

  def boundedBelowLattice(implicit A: Lattice[A] with BoundedJoinSemilattice[A]) = new LatticeProperties(
    name = "boundedBelowLattice",
    parents = Seq(boundedJoinSemilattice, lattice)
  )

  def boundedAboveLattice(implicit A: Lattice[A] with BoundedMeetSemilattice[A]) = new LatticeProperties(
    name = "boundedAboveLattice",
    parents = Seq(boundedMeetSemilattice, lattice)
  )

  def boundedLattice(implicit A: BoundedLattice[A]) = new LatticeProperties(
    name = "boundedLattice",
    parents = Seq(boundedJoinSemilattice, boundedMeetSemilattice, lattice)
  )

  class LatticeProperties(
    val name: String,
    val parents: Seq[LatticeProperties],
    val props: (String, Prop)*
  ) extends RuleSet {
    val bases = Seq.empty
  }
}
