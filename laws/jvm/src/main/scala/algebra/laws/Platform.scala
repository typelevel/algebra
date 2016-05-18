package algebra
package laws

import org.scalacheck.Prop
import org.scalacheck.Prop._
import scala.util.control.NonFatal

private[laws] object Platform {

  final val isJvm = true
  final val isJs = false

  // Scala-js does not implement the Serializable interface, so the
  // real test is for JVM only.
  @inline
  def serializable[M](m: M): (String, Prop) =
    "serializable" -> (IsSerializable() match {
      case false =>
        Prop(_ => Result(status = Proof))
      case true =>
        Prop { _ =>
          import java.io._
          val baos = new ByteArrayOutputStream()
          val oos = new ObjectOutputStream(baos)
          var ois: ObjectInputStream = null
          try {
            oos.writeObject(m)
            oos.close()
            val bais = new ByteArrayInputStream(baos.toByteArray())
            ois = new ObjectInputStream(bais)
            val m2 = ois.readObject() // just ensure we can read it back
            ois.close()
            Result(status = Proof)
          } catch { case NonFatal(t) =>
              Result(status = Exception(t))
          } finally {
            oos.close()
            if (ois != null) ois.close()
          }
        }
    })
}
