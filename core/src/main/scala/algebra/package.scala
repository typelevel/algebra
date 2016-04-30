package object algebra {

  type Eq[A] = cats.kernel.Eq[A]
  val Eq = cats.kernel.Eq

  type PartialOrder[A] = cats.kernel.PartialOrder[A]
  val PartialOrder = cats.kernel.PartialOrder

  type Order[A] = cats.kernel.Order[A]
  val Order = cats.kernel.Order

  type Semigroup[A] = cats.kernel.Semigroup[A]
  val Semigroup = cats.kernel.Semigroup

  type CommutativeSemigroup[A] = cats.kernel.CommutativeSemigroup[A]
  val CommutativeSemigroup = cats.kernel.CommutativeSemigroup

  type Monoid[A] = cats.kernel.Monoid[A]
  val Monoid = cats.kernel.Monoid

  type CommutativeMonoid[A] = cats.kernel.CommutativeMonoid[A]
  val CommutativeMonoid = cats.kernel.CommutativeMonoid

  type Group[A] = cats.kernel.Group[A]
  val Group = cats.kernel.Group

  type CommutativeGroup[A] = cats.kernel.CommutativeGroup[A]
  val CommutativeGroup = cats.kernel.CommutativeGroup

  type Band[A] = cats.kernel.Band[A]
  val Band = cats.kernel.Band

  type Semilattice[A] = cats.kernel.Semilattice[A]
  val Semilattice = cats.kernel.Semilattice

  type BoundedSemilattice[A] = cats.kernel.BoundedSemilattice[A]
  val BoundedSemilattice = cats.kernel.BoundedSemilattice
}
