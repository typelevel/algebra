package algebra
package laws

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import prop._

import algebra.std.long._

/**
 * Generic hashing test.
 *
 * This ensures that for a random collection of elements of size
 * `num`, the hash values produced match a uniform distribution with
 * 95% confidence.
 * 
 * To apply this test to a concrete type, we need to:
 * 
 *   1. Implement a Hash[A] instance.
 *   2. Implement a generate(i: Int): A instance.
 *   3. call runForMods with one or more smallish, positive integers.
 * 
 * Testing hashing of sequential values is possible using the `i`
 * parameter to the generate method, e.g.:
 * 
 *   def generate(i: Int): Long = (i + 1000).toLong
 * 
 * It might seem redundant to use a generate method when ScalaCheck
 * provides Gen and Arbitrary type classes. However, we need to use
 * our own `generate` method to supply random values because
 * ScalaCheck will tend to generate certain specific values more
 * frequently than others, creating "too many" collisions on those
 * particular hash values and throwing off the test.
 */
abstract class HashTest[A: Hash] extends PropSpec with Matchers with GeneratorDrivenPropertyChecks {

  /**
   * Return instances for A, either random or based on the input
   * parameter i.
   * 
   * In order for the test to be valid, it's important that different
   * i values produce different output values.
   */
  def generate(i: Int): A

  /**
   * Test the Hash[A] instance using the given mod values
   * (remainders), and `num` random A values to be hashed.
   */
  def runForMods(mods: List[Int], num: Int = 1000000): Unit =
    mods.foreach { i =>
      property(s"hashing mod $i is ok") {
        testHashModN(i, num)
      }
    }

  def testHashModN(mod: Int, num: Int) {
    val buckets = hashIntoBuckets(mod, num)
    val expected = num.toDouble / mod
    val prob = Stats.uniformProbability(expected, buckets)
    prob should be >= 0.95
  }

  def hashIntoBuckets(mod: Int, num: Int): Array[Int] = {

    // need to be careful to ensure % returns positive value
    val h = Hash[A]
    def hashModN(x: A): Int = ((h.hash(x) & 0xffffffffL) % mod).toInt

    val buckets = new Array[Int](mod)
    var i = 0
    while (i < num) {
      val x = generate(i)
      val k = hashModN(x)
      buckets(k) += 1
      i += 1
    }
    buckets
  }
}

class RandomLongHashTest extends HashTest[Long] {
  def generate(i: Int): Long = scala.util.Random.nextLong
  runForMods(17 :: 31 :: 61 :: 257 :: 511 :: Nil)
}
