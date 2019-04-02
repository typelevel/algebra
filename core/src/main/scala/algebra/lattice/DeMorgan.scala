package algebra
package lattice

import scala.{specialized => sp}

/**
  * De Morgan algebras are bounded lattices that are also equipped with
  * a De Morgan involution.
  *
  * De Morgan involution obeys the following laws:
  *
  *  - ¬¬a = a
  *  - ¬(x∧y) = ¬x∨¬y
  *
  * However, in De Morgan algebras this involution does not necessarily
  * provide the law of the excluded middle. This means that there is no
  * guarantee that (a ∨ ¬a) = 1. De Morgan algebra do not not necessarily
  * provide the law of non contradiction either. This means that there is
  * no guarantee that (a ∧ ¬a) = 0.
  *
  * De Morgan algebras are useful to model fuzzy logic. For a model of
  * classical logic, see the boolean algebra type class implemented as
  * [[Bool]].
  */
trait DeMorgan[@sp(Int, Long) A] extends Any with BoundedDistributiveLattice[A] with Logic[A] { self =>
  def meet(a: A, b: A): A = and(a, b)

  def join(a: A, b: A): A = or(a, b)

  def imp(a: A, b: A): A = or(complement(a), b)
}

trait DeMorganGenBoolOverlap[H[A] <: DeMorgan[A]] {
  def and[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.and(x, y)
  def or[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.or(x, y)
  def xor[@sp(Int, Long) A](x: A, y: A)(implicit ev: H[A]): A =
    ev.xor(x, y)
}

trait DeMorganFunctions[H[A] <: DeMorgan[A]] extends
  BoundedMeetSemilatticeFunctions[H] with
  BoundedJoinSemilatticeFunctions[H] with
  LogicFunctions[H]


object DeMorgan extends DeMorganFunctions[DeMorgan] with DeMorganGenBoolOverlap[DeMorgan] {

  /**
    * Access an implicit `DeMorgan[A]`.
    */
  @inline final def apply[@sp(Int, Long) A](implicit ev: DeMorgan[A]): DeMorgan[A] = ev
}
