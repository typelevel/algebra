package algebra
package std

import scala.collection.mutable

object list {

  implicit def listOrder[A: Order]: Order[List[A]] = new ListOrder[A]

  class ListOrder[A](implicit ev: Order[A]) extends Order[List[A]] {
    def compare(x: List[A], y: List[A]): Int =
      (x, y) match {
        case (Nil, Nil) => 0
        case (Nil, _) => -1
        case (_, Nil) => 1
        case (x :: xs, y :: ys) =>
          val n = ev.compare(x, y)
          if (n != 0) n else compare(xs, ys)
      }
  }

  implicit def listMonoid[A] = new ListMonoid[A]

  class ListMonoid[A] extends Monoid[List[A]] {
    def empty: List[A] = Nil
    def combine(x: List[A], y: List[A]): List[A] = x ::: y

    override def combineN(x: List[A], n: Int): List[A] = {
      val buf = mutable.ListBuffer.empty[A]
      (0 until n).foreach(_ => x.foreach(buf += _))
      buf.toList
    }

    override def combineAll(xs: TraversableOnce[List[A]]): List[A] = {
      val buf = mutable.ListBuffer.empty[A]
      xs.foreach(_.foreach(buf += _))
      buf.toList
    }
  }
}
