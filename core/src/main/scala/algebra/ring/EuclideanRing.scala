package algebra
package ring

import scala.{specialized => sp}

/**
 * EuclideanRing implements a Euclidean domain.
 * 
 * The formal definition says that every euclidean domain A has (at
 * least one) euclidean function f: A -> N (the natural numbers) where:
 * 
 *   (for every x and non-zero y) x = yq + r, and r = 0 or f(r) < f(y).
 * 
 * The idea is that f represents a measure of length (or absolute
 * value), and the previous equation represents finding the quotient
 * and remainder of x and y. So:
 * 
 *   quot(x, y) = q
 *   mod(x, y) = r
 * 
 * This type does not provide access to the Euclidean function, but
 * only provides the quot, mod, and quotmod operators.
 * 
 * The Euclidean function is available in the `EuclideanFunction` type.
 */
trait EuclideanRing[@sp(Int, Long, Float, Double) A] extends Any with CommutativeRing[A] {
  def mod(a: A, b: A): A
  def quot(a: A, b: A): A
  def quotmod(a: A, b: A): (A, A) = (quot(a, b), mod(a, b))
}

trait EuclideanRingFunctions[R[T] <: EuclideanRing[T]] extends RingFunctions[R] {
  def quot[@sp(Int, Long, Float, Double) A](x: A, y: A)(implicit ev: R[A]): A =
    ev.quot(x, y)
  def mod[@sp(Int, Long, Float, Double) A](x: A, y: A)(implicit ev: R[A]): A =
    ev.mod(x, y)
  def quotmod[@sp(Int, Long, Float, Double) A](x: A, y: A)(implicit ev: R[A]): (A, A) =
    ev.quotmod(x, y)
}

object EuclideanRing extends EuclideanRingFunctions[EuclideanRing] {
  @inline final def apply[A](implicit ev: EuclideanRing[A]): EuclideanRing[A] = ev
}

/**
  * Extends EuclideanRing with a (submultiplicative) Euclidean function.
  * 
  * On an EuclideanRing, we can require the Euclidean function to be submultiplicative
  * without loss of generality:
  * 
  *   if x, y are not zero, then f(a) <= f(ab)
  */
trait EuclideanFunction[@sp(Int, Long, Float, Double) A] extends Any with EuclideanRing[A] {
  def euclideanFunction(a: A): BigInt
}

trait EuclideanFunctionFunctions[R[T] <: EuclideanFunction[T]] extends EuclideanRingFunctions[R] {
  def euclideanFunction[@sp(Int, Long, Float, Double) A](a: A)(implicit ev: R[A]): BigInt =
    ev.euclideanFunction(a)
}

object EuclideanFunction extends EuclideanFunctionFunctions[EuclideanFunction] {
  @inline final def apply[A](implicit ev: EuclideanFunction[A]): EuclideanFunction[A] = ev
}
