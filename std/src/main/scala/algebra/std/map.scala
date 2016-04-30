package algebra
package std

import cats.kernel.std.util.StaticMethods.addMap
import algebra.std.util.StaticMethods.timesMap
import algebra.ring._

package object map extends MapInstances

trait MapInstances extends cats.kernel.std.MapInstances with MapInstances3

trait MapInstances3 extends MapInstances2 {
}

trait MapInstances2 extends MapInstances1 {
  implicit def mapSemiring[K, V: Semiring]: MapSemiring[K, V] =
    new MapSemiring[K, V]
}

trait MapInstances1 extends MapInstances0 {
}

trait MapInstances0 {
  implicit def mapAdditiveMonoid[K, V: AdditiveSemigroup]: MapAdditiveMonoid[K, V] =
    new MapAdditiveMonoid[K, V]
}

class MapAdditiveMonoid[K, V](implicit V: AdditiveSemigroup[V]) extends AdditiveMonoid[Map[K, V]]  {
  def zero: Map[K, V] = Map.empty

  def plus(xs: Map[K, V], ys: Map[K, V]): Map[K, V] =
    if (xs.size <= ys.size) {
      addMap(xs, ys)((x, y) => V.plus(x, y))
    } else {
      addMap(ys, xs)((y, x) => V.plus(x, y))
    }
}

class MapSemiring[K, V](implicit V: Semiring[V]) extends MapAdditiveMonoid[K, V] with Semiring[Map[K, V]] {

  def times(xs: Map[K, V], ys: Map[K, V]): Map[K, V] =
    if (xs.size <= ys.size) {
      timesMap(xs, ys)((x, y) => V.times(x, y))
    } else {
      timesMap(ys, xs)((y, x) => V.times(x, y))
    }

  override def pow(x: Map[K, V], n: Int): Map[K, V] =
    if (n < 1) throw new IllegalArgumentException(s"non-positive exponent: $n")
    else if (n == 1) x
    else x.map { case (k, v) => (k, V.pow(v, n)) }
}
