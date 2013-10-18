package algebra

import scala.{ specialized => sp }

/**
 * A simple type class for numeric types that are a subset of the reals.
 */
trait IsReal[@sp A] extends Order[A] with Signed[A] {
  def ceil(a: A): A
  def floor(a: A): A
  def round(a: A): A
  def isWhole(a: A): Boolean
  def toDouble(a: A): Double
}

trait IsIntegral[@sp(Byte,Short,Int,Long) A] extends IsReal[A] {
  def ceil(a: A): A = a
  def floor(a: A): A = a
  def round(a: A): A = a
  def isWhole(a: A): Boolean = true
}

object IsReal {
  def apply[@sp A](implicit A: IsReal[A]): IsReal[A] = A
}
