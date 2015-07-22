package algebra
package laws

import org.scalacheck.{Arbitrary, Prop}
import org.scalacheck.Prop._
import Prop.{False, Proof, Result}

private[laws] object Platform {

  // Scala-js does not implement the Serializable interface, so the real test is for JVM only.
  @inline
  def serializable[M](m: M): (String, Prop) = 
    "serializable" -> Prop { _ =>
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
      } catch { case _: Throwable =>
        Result(status = False)
      } finally {
        oos.close()
        if (ois != null) ois.close()
      }
    }
}
