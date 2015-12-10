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

trait LatticeLaws[A] extends GroupLaws[A] {

  implicit def Equ: Eq[A]
  implicit def Arb: Arbitrary[A]

  def joinSemilattice(implicit A: JoinSemilattice[A],
    check: IsSerializable[Semilattice[A]]) =
    new LatticeProperties(
      name = "joinSemilattice",
      parents = Nil,
      join = Some(semilattice(A.joinSemilattice, check)),
      meet = None,
      Rules.serializable(A)
    )

  def meetSemilattice(implicit A: MeetSemilattice[A],
    check: IsSerializable[Semilattice[A]]) =
    new LatticeProperties(
      name = "meetSemilattice",
      parents = Nil,
      join = None,
      meet = Some(semilattice(A.meetSemilattice, check)),
      Rules.serializable(A)
    )

  def lattice(implicit A: Lattice[A],
    check: IsSerializable[Semilattice[A]]) =
    new LatticeProperties(
      name = "lattice",
      parents = Seq(joinSemilattice, meetSemilattice),
      join = Some(semilattice(A.joinSemilattice, check)),
      meet = Some(semilattice(A.meetSemilattice, check)),
      "absorption" -> forAll { (x: A, y: A) =>
        (A.join(x, A.meet(x, y)) ?== x) && (A.meet(x, A.join(x, y)) ?== x)
      }
    )

  def distributiveLattice(implicit A: DistributiveLattice[A],
    check: IsSerializable[Semilattice[A]]) =
    new LatticeProperties(
      name = "distributiveLattice",
      parents = Seq(lattice),
      join = Some(semilattice(A.joinSemilattice, check)),
      meet = Some(semilattice(A.meetSemilattice, check)),
      "distributive" -> forAll { (x: A, y: A, z: A) =>
        (A.join(x, A.meet(y, z)) ?== A.meet(A.join(x, y), A.join(x, z))) &&
        (A.meet(x, A.join(y, z)) ?== A.join(A.meet(x, y), A.meet(x, z)))
      }
    )

  def boundedJoinSemilattice(implicit A: BoundedJoinSemilattice[A],
    check: IsSerializable[BoundedSemilattice[A]]) =
    new LatticeProperties(
      name = "boundedJoinSemilattice",
      parents = Seq(joinSemilattice),
      join = Some(boundedSemilattice(A.joinSemilattice, check)),
      meet = None
    )

  def boundedMeetSemilattice(implicit A: BoundedMeetSemilattice[A],
    check: IsSerializable[BoundedSemilattice[A]]) =
    new LatticeProperties(
      name = "boundedMeetSemilattice",
      parents = Seq(meetSemilattice),
      join = None,
      meet = Some(boundedSemilattice(A.meetSemilattice, check))
    )

  def boundedJoinLattice(implicit A: Lattice[A] with BoundedJoinSemilattice[A],
    check: IsSerializable[BoundedSemilattice[A]]) =
    new LatticeProperties(
      name = "boundedJoinLattice",
      parents = Seq(boundedJoinSemilattice, lattice),
      join = Some(boundedSemilattice(A.joinSemilattice, check)),
      meet = Some(semilattice(A.meetSemilattice, check))
    )

  def boundedMeetLattice(implicit A: Lattice[A] with BoundedMeetSemilattice[A],
    check: IsSerializable[BoundedSemilattice[A]]) =
    new LatticeProperties(
      name = "boundedMeetLattice",
      parents = Seq(boundedMeetSemilattice, lattice),
      join = Some(semilattice(A.joinSemilattice, check)),
      meet = Some(boundedSemilattice(A.meetSemilattice, check))
    )

  def boundedLattice(implicit A: BoundedLattice[A],
    check: IsSerializable[BoundedSemilattice[A]]) = new LatticeProperties(
    name = "boundedLattice",
      parents = Seq(boundedJoinSemilattice, boundedMeetSemilattice, lattice),
      join = Some(boundedSemilattice(A.joinSemilattice, check)),
      meet = Some(boundedSemilattice(A.meetSemilattice, check))
  )

  def boundedDistributiveLattice(implicit A: BoundedDistributiveLattice[A],
    check: IsSerializable[BoundedSemilattice[A]]) =
    new LatticeProperties(
      name = "boundedLattice",
      parents = Seq(boundedLattice, distributiveLattice),
      join = Some(boundedSemilattice(A.joinSemilattice, check)),
      meet = Some(boundedSemilattice(A.meetSemilattice, check))
    )

  class LatticeProperties(
    val name: String,
    val parents: Seq[LatticeProperties],
    val join: Option[GroupProperties],
    val meet: Option[GroupProperties],
    val props: (String, Prop)*
  ) extends RuleSet {
    private val _m = meet map { "meet" -> _ }
    private val _j = join map { "join" -> _ }

    val bases = _m.toList ::: _j.toList
  }

}
