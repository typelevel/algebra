package algebra
package ring

import scala.annotation.tailrec
import scala.{specialized => sp}

trait EuclideanRing[@sp(Byte, Short, Int, Long, Float, Double) A] extends CommutativeRing[A] {
  def mod(a: A, b: A): A
  def quot(a: A, b: A): A
  def quotmod(a: A, b: A): (A, A) = (quot(a, b), mod(a, b))
}

trait EuclideanRingFunctions extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  def quot[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): A =
    ev.quot(x, y)
  def mod[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): A =
    ev.mod(x, y)
  def quotmod[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): (A, A) =
    ev.quotmod(x, y)
}

object EuclideanRing extends EuclideanRingFunctions {
  @inline final def apply[A](implicit ev: EuclideanRing[A]): EuclideanRing[A] = ev
}
