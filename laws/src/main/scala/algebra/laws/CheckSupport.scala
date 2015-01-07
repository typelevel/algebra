package algebra.laws

import algebra.std.Rat

import org.scalacheck._
import Arbitrary.arbitrary

/**
 * This object contains Arbitrary instances for types defined in
 * algebra.std, as well as anything else we'd like to import to assist
 * in running ScalaCheck tests.
 * 
 * (Since algebra-std has no dependencies, its types can't define
 * Arbitrary instances in companions.)
 */
object CheckSupport {
  implicit val ratArbitrary =
    Arbitrary(for {
      (n, d) <- arbitrary[(BigInt, BigInt)] if d != 0
    } yield Rat(n, d))
}
