package algebra
package laws

import org.scalacheck.Prop
import org.scalacheck.Prop._
import Prop.{ Proof, Result }

private[laws] object Platform {

  // Scala-js does not implement the Serializable interface, so we just retuen true.
  @inline
  def serializable[M](m: M): (String, Prop) = "serializable" -> Prop { _ =>
    Result(status = Proof)
  }
}
