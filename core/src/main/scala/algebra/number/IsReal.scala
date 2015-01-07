package algebra
package number

import scala.{ specialized => sp }

/**
 * A simple type class for numeric types that are a subset of the reals.
 */
trait IsReal[@sp(Byte,Short,Int,Long,Float,Double) A] extends Any with Order[A] with Signed[A] {
  def ceil(a: A): A
  def floor(a: A): A
  def round(a: A): A
  def isWhole(a: A): Boolean
  def toDouble(a: A): Double
}

trait IsIntegral[@sp(Byte,Short,Int,Long) A] extends Any with IsReal[A] {
  def ceil(a: A): A = a
  def floor(a: A): A = a
  def round(a: A): A = a
  def isWhole(a: A): Boolean = true
}

trait IsRealFunctions {
  def ceil[@sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.ceil(a)
  def floor[@sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.floor(a)
  def round[@sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.round(a)
  def isWhole[@sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): Boolean =
    ev.isWhole(a)
  def toDouble[@sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): Double =
    ev.toDouble(a)
}

object IsReal extends IsRealFunctions {
  def apply[A](implicit ev: IsReal[A]): IsReal[A] = ev
}
