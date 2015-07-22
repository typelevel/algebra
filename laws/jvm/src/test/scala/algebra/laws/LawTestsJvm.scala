package algebra
package laws

import algebra.macros._
import algebra.std.Rat
import CheckSupport._

class LawTests extends LawTestsBase {

  // Rational tests do not return oin Scala-js, so we make them JVM only.
  laws[BaseLaws, Rat].check(_.isReal)
  laws[RingLaws, Rat].check(_.field)
}
