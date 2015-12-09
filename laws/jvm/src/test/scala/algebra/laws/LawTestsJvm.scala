package algebra
package laws

import algebra.macros._

class LawTests extends LawTestsBase {

  // Rational tests do not return on Scala-js, so we make them JVM only.
  laws[RingLaws, Rat].check(_.field)
}
