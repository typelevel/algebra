package algebra
package std

import scala.annotation.tailrec

package object vector extends VectorInstances

trait VectorInstances {
  implicit def vectorOrder[A: Order]: Order[Vector[A]] = new VectorOrder[A]
  implicit def vectorMonoid[A]: Monoid[Vector[A]] = new VectorMonoid[A]
}

final class VectorOrder[A](implicit ev: Order[A]) extends Order[Vector[A]] {
  def compare(x: Vector[A], y: Vector[A]): Int = {
    val lengthX = x.length
    val lengthY = y.length

    @tailrec
    def cmp(index: Int): Int = {
      if (index == lengthX) {
        lengthX - lengthY
      } else if (index == lengthY) {
        1
      } else {
        val n = ev.compare(x(index), y(index))
        if (n == 0) cmp(index + 1) else n
      }
    }

    cmp(0)
  }
}

class VectorMonoid[A] extends Monoid[Vector[A]] {
  def empty: Vector[A] = Vector.empty[A]

  // This is really slow in 2.10: SI-7725.
  // Unless we go all out with optimization, our implementation would probably
  // be slower in 2.11. Do we want to optimize this?
  def combine(x: Vector[A], y: Vector[A]): Vector[A] = x ++ y
}
