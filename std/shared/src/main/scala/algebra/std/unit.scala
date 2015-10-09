package algebra
package std

import algebra.ring.CommutativeRing

package object unit extends UnitInstances

trait UnitInstances {
  implicit val unitAlgebra: Order[Unit] with CommutativeRing[Unit] with BoundedSemilattice[Unit] =
    new UnitAlgebra
}

class UnitAlgebra extends Order[Unit] with CommutativeRing[Unit] with BoundedSemilattice[Unit] {
  def compare(x: Unit, y: Unit): Int = 0

  override def eqv(x: Unit, y: Unit): Boolean = true
  override def neqv(x: Unit, y: Unit): Boolean = false
  override def gt(x: Unit, y: Unit): Boolean = false
  override def lt(x: Unit, y: Unit): Boolean = false
  override def gteqv(x: Unit, y: Unit): Boolean = true
  override def lteqv(x: Unit, y: Unit): Boolean = true

  override def min(x: Unit, y: Unit): Unit = ()
  override def max(x: Unit, y: Unit): Unit = ()

  def zero: Unit = ()
  def one: Unit = ()

  override def isZero(x: Unit)(implicit ev: Eq[Unit]): Boolean = true
  override def isOne(x: Unit)(implicit ev: Eq[Unit]): Boolean = true

  def plus(a: Unit, b: Unit): Unit = ()
  def negate(x: Unit): Unit = ()
  def times(a: Unit, b: Unit): Unit = ()
  override def pow(a: Unit, b: Int): Unit = ()

  def empty: Unit = ()
  def combine(x: Unit, y: Unit): Unit = ()
  override protected[this] def repeatedCombineN(a: Unit, n: Int): Unit = ()
  override def combineAllOption(as: TraversableOnce[Unit]): Option[Unit] =
    if (as.isEmpty) None else Some(())
}
