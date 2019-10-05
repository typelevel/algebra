package algebra.ring

import algebra.instances.bigInt._

import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class RingTest extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {
  test("Ring.defaultFromBigInt") {
    forAll { (n: BigInt) =>
      Ring.defaultFromBigInt[BigInt](n) shouldBe n
    }
  }
}
