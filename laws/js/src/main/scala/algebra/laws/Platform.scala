package algebra
package laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._
import Prop.{False, Proof, Result}

private[laws] object Platform {

  // Scala-js does not implement the Serializable interface, so we just return true.
  @inline
  def serializable[M: IsSerializable](m: M): (String, Prop) =
    "serializable" -> Prop { _ =>
      Result(status = Proof)
    }
}
