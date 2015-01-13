package algebra
package number

import scala.{specialized => sp}

/**
 * This is a type class for types with n-roots.
 *
 * For many types, the value returned by `nroot` and `sqrt` are only
 * guaranteed to be approximate answers. Thus, many (most?) instances
 * of NRoot will not be able to ensure that `sqrt(x) * sqrt(x) = x`.
 *
 * Also, generally `nroot`s where `n` is even are not defined for
 * negative numbers. The behaviour is undefined if this is
 * attempted.
 * 
 * It would be nice to ensure an exception is raised, but some types
 * may defer computation and testing if a value is negative may not be
 * ideal. So, do not count on `ArithmeticException`s to save you from
 * bad arithmetic!
 */
trait NRoot[@sp(Double,Float,Int,Long) A] {
  def nroot(a: A, n: Int): A
  def sqrt(a: A): A = nroot(a, 2)
  def fpow(a: A, b: A): A
}

trait NRootFunctions {
  def nroot[@sp(Double,Float,Int,Long) A](a: A, n: Int)(implicit ev: NRoot[A]): A =
    ev.nroot(a, n)
  def sqrt[@sp(Double,Float,Int,Long) A](a: A)(implicit ev: NRoot[A]): A =
    ev.sqrt(a)
  def fpow[@sp(Double,Float,Int,Long) A](a: A, b: A)(implicit ev: NRoot[A]): A =
    ev.fpow(a, b)
}

object NRoot extends NRootFunctions {
  @inline final def apply[@sp(Int,Long,Float,Double) A](implicit ev: NRoot[A]) = ev
}
