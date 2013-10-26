package algebra
package ring

import scala.annotation.tailrec
import scala.{specialized => sp}

trait EuclideanRing[@sp(Byte, Short, Int, Long, Float, Double) A] extends CommutativeRing[A] {
  def quot(a: A, b: A): A
  def mod(a: A, b: A): A
  def quotmod(a: A, b: A): (A, A) = (quot(a, b), mod(a, b))

  def gcd(a: A, b: A): A
  def lcm(a: A, b: A): A = times(quot(a, gcd(a, b)), b)

  @tailrec protected[this] final def euclid(a: A, b: A)(implicit eq: Eq[A]): A =
    if (eq.eqv(b, zero)) a else euclid(b, mod(a, b))
}

trait EuclideanRingFunctions extends AdditiveGroupFunctions with MultiplicativeMonoidFunctions {
  def quot[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): A =
    ev.quot(x, y)
  def mod[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): A =
    ev.mod(x, y)
  def quotmod[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): (A, A) =
    ev.quotmod(x, y)
  def gcd[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): A =
    ev.gcd(x, y)
  def lcm[@sp(Byte, Short, Int, Long, Float, Double) A](x: A, y: A)(implicit ev: EuclideanRing[A]): A =
    ev.lcm(x, y)
}

object EuclideanRing extends EuclideanRingFunctions {
  @inline final def apply[A](implicit ev: EuclideanRing[A]): EuclideanRing[A] = ev
}
