package algebra.ring

import algebra.instances.bigInt._

import org.scalatest.Matchers
import org.scalatest.check.ScalaCheckDrivenPropertyChecks
import org.scalatest.funsuite.AnyFunSuite

class RingTest extends AnyFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {
  test("Ring.defaultFromBigInt") {
    forAll { (n: BigInt) =>
      Ring.defaultFromBigInt[BigInt](n) shouldBe n
    }
  }
}
