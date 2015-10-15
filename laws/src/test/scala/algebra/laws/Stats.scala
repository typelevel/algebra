package algebra
package laws

import scala.math.{floor, exp, Pi, pow, sqrt}

object Stats {

  // basic strategy based on code found at:
  // http://rosettacode.org/wiki/Verify_distribution_uniformity/Chi-squared_test

  def uniformProbability(expected: Double, buckets: Array[Int]): Double = {
    val dof = buckets.length - 1
    val distance = buckets.map(x => pow(x - expected, 2)).sum / expected
    1.0 - gammaIncQ(dof * 0.5, distance * 0.5)
  }

  def gammaIncQ(a: Double, x: Double): Double = {
    def gammaSpounge(z: Double): Double = {
      val a = 12
      var k1_factrl = 1.0
      var c = Vector(sqrt(2.0 * Pi))
        (1 until a).foreach { k =>
          c = c :+ (exp(a - k) * pow(a - k, k - 0.5) / k1_factrl)
          k1_factrl *= -k
        }
      val accm = c(0) + (1 until a).map(k => c(k) / (z + k)).sum
      accm * (exp(-(z + a)) * pow(z + a, z + 0.5)) / z
    }

    val a1 = a - 1
    val a2 = a - 2
    def f0(t: Double): Double = pow(t, a1 * exp(-t))
    def df0(t: Double): Double = (a1 - t) * pow(t, a2 * exp(-t))
  
    var y = a1
    while (f0(y) * (x - y) > 2.0e-8 && y < x) y += 0.3
    if (y > x) y = x
  
    val n = floor(y / 3.0e-4)
    val h = y / n
    val hh = h * 0.5
    val gamax = h * ((n - 1) to 0 by -1).map { i =>
      val t = i * h
      f0(t) + hh * df0(t)
    }.sum

    gamax / gammaSpounge(a)
  }
}
