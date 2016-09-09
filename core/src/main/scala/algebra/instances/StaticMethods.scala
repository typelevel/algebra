package algebra.instances

import java.lang.Double.{ longBitsToDouble, doubleToLongBits }
import java.lang.Float.{ intBitsToFloat, floatToIntBits }
import java.lang.Long.{ numberOfTrailingZeros, numberOfLeadingZeros }
import java.lang.Math
import scala.annotation.tailrec

object StaticMethods {

  /**
   * Implementation of the binary GCD algorithm.
   */
  final def gcd(x0: Long, y0: Long): Long = {
    // if either argument is 0, just return the other.
    if (x0 == 0L) return y0
    if (y0 == 0L) return x0

    val xz = numberOfTrailingZeros(x0)
    val yz = numberOfTrailingZeros(y0)

    // Math.abs is safe because Long.MinValue (0x8000000000000000)
    // will be shifted over 63 bits (due to trailing zeros).
    var x = Math.abs(x0 >> xz)
    var y = Math.abs(y0 >> yz)

    while (x != y) {
      if (x > y) {
        x -= y
        x >>= numberOfTrailingZeros(x)
      } else {
        y -= x
        y >>= numberOfTrailingZeros(y)
      }
    }

    // trailing zeros mean powers of two -- if both numbers had
    // trailing zeros, those are part of the common divsor as well.
    if (xz < yz) x << xz else x << yz
  }

  /**
   * GCD for Float values.
   */
  final def gcd(a: Float, b: Float): Float = {
    import java.lang.Integer.{ numberOfTrailingZeros, numberOfLeadingZeros }

    def value(bits: Int): Int = bits & 0x007FFFFF | 0x00800000

    def exp(bits: Int): Int = ((bits >> 23) & 0xFF).toInt

    def gcd0(val0: Int, exp0: Int, val1: Int, exp1: Int): Float = {
      val tz0 = numberOfTrailingZeros(val0)
      val tz1 = numberOfTrailingZeros(val1)
      val tzShared = Math.min(tz0, tz1 + exp1 - exp0)
      val n = gcd(val0.toLong >>> tz0, val1.toLong >>> tz1).toInt << tzShared

      val shift = numberOfLeadingZeros(n) - 8 // Number of bits to move 1 to bit 23
      val mantissa = (n << shift) & 0x007FFFFF
      val exp = (exp0 - shift)
      if (exp < 0) 0F else intBitsToFloat((exp << 23) | mantissa)
    }

    if (a == 0F) b
    else if (b == 0F) a
    else {
      val aBits = floatToIntBits(a)
      val aVal = value(aBits)
      val aExp = exp(aBits)

      val bBits = floatToIntBits(b)
      val bVal = value(bBits)
      val bExp = exp(bBits)

      if (aExp < bExp) gcd0(aVal, aExp, bVal, bExp)
      else gcd0(bVal, bExp, aVal, aExp)
    }
  }

  /**
   * GCD for Double values.
   */
  final def gcd(a: Double, b: Double): Double = {
    def value(bits: Long): Long = bits & 0x000FFFFFFFFFFFFFL | 0x0010000000000000L

    def exp(bits: Long): Int = ((bits >> 52) & 0x7FF).toInt

    def gcd0(val0: Long, exp0: Int, val1: Long, exp1: Int): Double = {
      val tz0 = numberOfTrailingZeros(val0)
      val tz1 = numberOfTrailingZeros(val1)
      val tzShared = Math.min(tz0, tz1 + exp1 - exp0)
      val n = gcd(val0 >>> tz0, val1 >>> tz1) << tzShared

      val shift = numberOfLeadingZeros(n) - 11 // Number of bits to move 1 to bit 52
      val mantissa = (n << shift) & 0x000FFFFFFFFFFFFFL
      val exp = (exp0 - shift).toLong
      if (exp < 0) 0.0 else longBitsToDouble((exp << 52) | mantissa)
    }

    if (a == 0D) b
    else if (b == 0D) a
    else {
      val aBits = doubleToLongBits(a)
      val aVal = value(aBits)
      val aExp = exp(aBits)

      val bBits = doubleToLongBits(b)
      val bVal = value(bBits)
      val bExp = exp(bBits)

      if (aExp < bExp) gcd0(aVal, aExp, bVal, bExp)
      else gcd0(bVal, bExp, aVal, aExp)
    }
  }

  def gcd(a: BigDecimal, b: BigDecimal): BigDecimal = {
    import java.math.BigInteger

    val Two = BigInteger.valueOf(2)
    val Five = BigInteger.valueOf(5)
    val Ten = BigInteger.TEN

    @tailrec
    def reduce0(n: BigInteger, prime: BigInteger, shift: Int = 0): (Int, BigInteger) = {
      val Array(div, rem) = n.divideAndRemainder(prime)
      if (n == BigInteger.ZERO || rem != BigInteger.ZERO) {
        (shift, n)
      } else {
        reduce0(div, prime, shift + 1)
      }
    }

    def reduce(n: BigInteger): (Int, Int, BigInteger) = {
      val (shift10, n0) = reduce0(n, Ten)
      val (shift5, n1) = reduce0(n0, Five)
      val (shift2, n2) = reduce0(n1, Two)
      (shift2 + shift10, shift5 + shift10, n2)
    }

    def gcd0(val0: BigInteger, exp0: Int, val1: BigInteger, exp1: Int): BigDecimal = {
      val (shiftTwo0, shiftFive0, shifted0) = reduce(val0)
      val (shiftTwo1, shiftFive1, shifted1) = reduce(val1)
      val sharedTwo = Math.min(shiftTwo0, shiftTwo1 + exp1 - exp0)
      val sharedFive = Math.min(shiftFive0, shiftFive1 + exp1 - exp0)
      val reshift = Two.pow(sharedTwo).multiply(Five.pow(sharedFive))
      val n = (shifted0 gcd shifted1).multiply(reshift)
      BigDecimal(new java.math.BigDecimal(n, -exp0))
    }

    val aJbd = a.bigDecimal
    val aVal = aJbd.unscaledValue.abs
    val aExp = -aJbd.scale

    val bJbd = b.bigDecimal
    val bVal = bJbd.unscaledValue.abs
    val bExp = -bJbd.scale

    if (aExp < bExp) gcd0(aVal, aExp, bVal, bExp) else gcd0(bVal, bExp, aVal, aExp)
  }

  /**
   * Exponentiation function, e.g. x^y
   *
   * If base^ex doesn't fit in a Long, the result will overflow (unlike
   * Math.pow which will return +/- Infinity).
   */
  final def pow(base: Long, exponent: Long): Long = {
    @tailrec def loop(t: Long, b: Long, e: Long): Long =
      if (e == 0L) t
      else if ((e & 1) == 1) loop(t * b, b * b, e >>> 1L)
      else loop(t, b * b, e >>> 1L)

    if (exponent >= 0L) loop(1L, base, exponent) else {
      if(base == 0L) throw new ArithmeticException("zero can't be raised to negative power")
      else if (base == 1L) 1L
      else if (base == -1L) if ((exponent & 1L) == 0L) -1L else 1L
      else 0L
    }
  }
}
