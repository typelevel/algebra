package algebra
package std

object string {

  implicit val stringOrder: Order[String] = new StringOrder

  class StringOrder extends Order[String] {
    def compare(x: String, y: String): Int = x compare y
  }

  implicit val stringMonoid = new StringMonoid

  class StringMonoid extends Monoid[String] {
    def empty: String = ""
    def combine(x: String, y: String): String = x + y

    override def combineAll(xs: TraversableOnce[String]): String = {
      val sb = new StringBuilder
      xs.foreach(sb.append)
      sb.toString
    }
  }
}
