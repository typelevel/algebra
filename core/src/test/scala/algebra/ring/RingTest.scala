package algebra.ring

import algebra.instances.bigInt._

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class RingTest extends FunSuite with Matchers with GeneratorDrivenPropertyChecks {
  test("Ring.defaultFromBigInt") {
    forAll { (n: BigInt) =>
      Ring.defaultFromBigInt[BigInt](n) shouldBe n
    }
  }
}
