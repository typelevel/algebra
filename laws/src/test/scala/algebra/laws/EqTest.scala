package algebra.laws

import java.util

import algebra.Eq
import org.scalatest._
import algebra.std.all._

/**
 * @author Frank Raiser <raiser.frank@gmail.com>
 */
class EqTest extends FlatSpec with Matchers {

  case class Foo(a: Int, b: String, c: Boolean)

  implicit val fooEq : Eq[Foo] = Eq.fromAll[Foo](
    (x,y) => x.a == y.a,
    (x,y) => x.b == y.b
  )

  "Eq.fromAll" should "recognize equality" in {
    assert(fooEq.eqv(Foo(1, "ab", true), Foo(1, "ab", false)))
  }

  it should "recognize inequality" in {
    assert(!fooEq.eqv(Foo(1, "ab", true), Foo(1, "abc", true)))
    assert(!fooEq.eqv(Foo(2, "ab", true), Foo(1, "ab", true)))
  }
}
