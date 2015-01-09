package algebra
package number

/**
 * A simple type class for numeric types that are a subset of the reals.
 */
trait IsReal[@mb @sp(Byte,Short,Int,Long,Float,Double) A] extends Any with Order[A] with Signed[A] {
  def ceil(a: A): A
  def floor(a: A): A
  def round(a: A): A
  def isWhole(a: A): Boolean
  def toDouble(a: A): Double
}

trait IsIntegral[@mb @sp(Byte,Short,Int,Long) A] extends Any with IsReal[A] {
  def ceil(a: A): A = a
  def floor(a: A): A = a
  def round(a: A): A = a
  def isWhole(a: A): Boolean = true
}

trait IsRealFunctions {
  def ceil[@mb @sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.ceil(a)
  def floor[@mb @sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.floor(a)
  def round[@mb @sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): A =
    ev.round(a)
  def isWhole[@mb @sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): Boolean =
    ev.isWhole(a)
  def toDouble[@mb @sp(Byte,Short,Int,Long,Float,Double) A](a: A)(implicit ev: IsReal[A]): Double =
    ev.toDouble(a)
}

object IsReal extends IsRealFunctions {
  def apply[A](implicit ev: IsReal[A]): IsReal[A] = ev
}
