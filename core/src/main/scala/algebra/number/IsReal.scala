package algebra
package number

import scala.{ specialized => sp }

import simulacrum._

/**
 * A simple type class for numeric types that are a subset of the reals.
 */
@typeclass trait IsReal[@sp(Int, Long, Float, Double) A] extends Any with Order[A] with Signed[A] {
  def ceil(a: A): A
  def floor(a: A): A
  def round(a: A): A
  def isWhole(a: A): Boolean
  def toDouble(a: A): Double
}

@typeclass trait IsIntegral[@sp(Int, Long) A] extends Any with IsReal[A] {
  override def ceil(a: A): A = a
  override def floor(a: A): A = a
  override def round(a: A): A = a
  override def isWhole(a: A): Boolean = true
}

trait IsRealFunctions {
  def ceil[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.ceil(a)
  def floor[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.floor(a)
  def round[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.round(a)
  def isWhole[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: IsReal[A]): Boolean =
    ev.isWhole(a)
  def toDouble[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: IsReal[A]): Double =
    ev.toDouble(a)
}

object IsReal extends IsRealFunctions
