package algebra
package ring

import scala.{specialized => sp}

// Temporary type class to defines convenience methods for the law tests in ordered rings
trait OrderedRing[@sp(Int, Long, Float, Double) A] extends Any with Ring[A] with Order[A] {
  def isWhole(x: A): Boolean 
  def abs(x: A): A = if (lt(x, zero)) negate(x) else x
  def sign(x: A): Int = {
    val c = compare(x, zero)
    c.signum
  }
}

/**
 * Division and modulus for computer scientists
 * taken from https://www.microsoft.com/en-us/research/wp-content/uploads/2016/02/divmodnote-letter.pdf
 * 
 * For two numbers x (dividend) and y (divisor) on an ordered ring with y != 0,
 * there exists a pair of numbers q (quotient) and r (remainder)
 * such that these laws are satisfied:
 * 
 * (1) q is an integer
 * (2) x = y * q + r (division rule)
 * (3) |r| < |y|,
 * (4) r = 0 or sign(r) = sign(x),
 * 
 * where sign is the sign function, and the absolute value 
 * function |x| is defined as |x| = x if x >=0, and |x| = -x otherwise.
 * 
 * We define functions tmod and tdiv, such that:
 * q = tdiv(x, y) and r = tmod(x, y)
 * 
 * Law (4) corresponds to ISO C99 and Haskell's quot/rem.
 */
trait TDivMod[@sp(Int, Long, Float, Double) A] extends Any with OrderedRing[A] {
  def tmod(x: A, y: A): A
  def tdiv(x: A, y: A): A
  def tdivmod(x: A, y: A): (A, A) = (tdiv(x, y), tmod(x, y))
}

// missing TDivModFunctions and TDivMod companion object

/**
 * Variant of TDivMod where law (4) is replaced by:
 * 
 * (4) r = 0 or sign(r) = sign(y).
 * See https://www.microsoft.com/en-us/research/wp-content/uploads/2016/02/divmodnote-letter.pdf
 * 
 * This convention is described by Knuth and used by Haskell,
 * and fmod corresponds to the REM function of the IEEE floating-point
 * standard.
 */
trait FDivMod[@sp(Int, Long, Float, Double) A] extends Any with OrderedRing[A] {
  def fmod(x: A, y: A): A
  def fdiv(x: A, y: A): A
  def fdivmod(x: A, y: A): (A, A) = (fdiv(x, y), fmod(x, y))
}

// missing FDivModFunctions and FDivMod companion object
