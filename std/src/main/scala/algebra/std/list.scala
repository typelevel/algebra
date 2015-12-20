package algebra
package std

import scala.annotation.tailrec
import scala.collection.mutable

package object list extends ListInstances 

trait ListInstances {
  implicit def listOrder[A: Order] = new ListOrder[A]
  implicit def listMonoid[A] = new ListMonoid[A]
}

class ListOrder[A](implicit ev: Order[A]) extends Order[List[A]] {
  def compare(xs: List[A], ys: List[A]): Int = {
    @tailrec def loop(xs: List[A], ys: List[A]): Int =
      xs match {
        case Nil =>
          if (ys.isEmpty) 0 else -1
        case x :: xs =>
          ys match {
            case Nil => 1
            case y :: ys =>
              val n = ev.compare(x, y)
              if (n != 0) n else loop(xs, ys)
          }
      }
    loop(xs, ys)
  }
}

class ListMonoid[A] extends Monoid[List[A]] {
  def neutral: List[A] = Nil
  def combine(x: List[A], y: List[A]): List[A] = x ::: y

  override def combineN(x: List[A], n: Int): List[A] = {
    val buf = mutable.ListBuffer.empty[A]
    @tailrec def loop(i: Int): List[A] =
      if (i <= 0) buf.toList else {
        buf ++= x
        loop(i - 1)
      }
    loop(n)
  }

  override def combineAll(xs: TraversableOnce[List[A]]): List[A] = {
    val buf = mutable.ListBuffer.empty[A]
    xs.foreach(buf ++= _)
    buf.toList
  }
}
