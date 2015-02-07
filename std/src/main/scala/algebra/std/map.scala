package algebra
package std

import algebra.ring._
import algebra.std.util.StaticMethods

package object map extends MapInstances

trait MapInstances extends MapInstances3

trait MapInstances3 extends MapInstances2 {
  implicit def mapRng[K, V: Rng] =
    new MapRng[K, V]
}

trait MapInstances2 extends MapInstances1 {
  implicit def mapSemiring[K, V: Semiring]: MapSemiring[K, V] =
    new MapSemiring[K, V]
}

trait MapInstances1 extends MapInstances0 {
  implicit def mapOrder[K: Order, V: Order: AdditiveMonoid]: Order[Map[K, V]] =
    new MapVectorOrder[K, V]

  implicit def mapGroup[K, V: Group]: MapGroup[K, V] =
    new MapGroup[K, V]
  implicit def mapAdditiveGroup[K, V: AdditiveGroup]: MapAdditiveGroup[K, V] =
    new MapAdditiveGroup[K, V]
}

trait MapInstances0 {
  implicit def mapEq[K, V: Eq: AdditiveMonoid]: Eq[Map[K, V]] =
    new MapVectorEq[K, V]
  implicit def mapMonoid[K, V: Semigroup]: MapMonoid[K, V] =
    new MapMonoid[K, V]
  implicit def mapAdditiveMonoid[K, V: AdditiveSemigroup]: MapAdditiveMonoid[K, V] =
    new MapAdditiveMonoid[K, V]
}

class MapVectorEq[K, V](implicit ev: Eq[V], a: AdditiveMonoid[V]) extends Eq[Map[K, V]] {
  private[this] def check(x: Map[K, V], y: Map[K, V]): Boolean =
    x.forall { case (k, v1) =>
      y.get(k) match {
        case Some(v2) => ev.eqv(v1, v2)
        case None => a.isZero(v1)
      }
    }

  def eqv(x: Map[K, V], y: Map[K, V]): Boolean =
    check(x, y) && check(y, x)
}

class MapVectorOrder[K, V](implicit K: Order[K], V: Order[V], V0: AdditiveMonoid[V]) extends Order[Map[K, V]] {
  def compare(x: Map[K, V], y: Map[K, V]): Int = {
    val zero = V0.zero
    def check(progress: (K, Int), x: Map[K, V], y: Map[K, V], sign: Int): (K, Int) = {
      val it = x.iterator
      var mink: K = progress._1
      var outv: Int = progress._2
      while (it.hasNext) {
        val (k, v1) = it.next
        if (outv == 0 || K.lt(k, mink)) {
          val v2 = y.getOrElse(k, zero)
          val r = V.compare(v1, v2)
          if (r != 0) {
            mink = k
            outv = r * sign
          }
        }
      }
      (mink, outv)
    }

    val init = (null.asInstanceOf[K], 0)
    val progress = check(init, x, y, 1)
    val (_, result) = check(progress, y, x, -1)
    result
  }
}

class MapMonoid[K, V](implicit sg: Semigroup[V]) extends Monoid[Map[K, V]]  {
  def empty: Map[K, V] = Map.empty

  def combine(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.combineMap(x, y)(sg.combine)(v => v)
}

class MapGroup[K, V](implicit g: Group[V]) extends MapMonoid[K, V] with Group[Map[K, V]] {
  def inverse(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, g.inverse(v)) }

  override def remove(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.combineMap(x, y)(g.remove)(g.inverse)
}

class MapAdditiveMonoid[K, V](implicit sg: AdditiveSemigroup[V]) extends AdditiveMonoid[Map[K, V]]  {
  def zero: Map[K, V] = Map.empty

  def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.combineMap(x, y)(sg.plus)(v => v)
}

class MapAdditiveGroup[K, V](implicit g: AdditiveGroup[V]) extends MapAdditiveMonoid[K, V] with AdditiveGroup[Map[K, V]] {
  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, g.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.combineMap(x, y)(g.minus)(g.negate)
}

class MapSemiring[K, V](implicit val sr: Semiring[V]) extends MapAdditiveMonoid[K, V] with Semiring[Map[K, V]] {

  override def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.combineMapCommutative(x, y)(sr.plus)

  def times(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.zipMap(x, y)(sr.times)

  override def pow(x: Map[K, V], n: Int): Map[K, V] =
    x.map { case (k, v) => (k, sr.pow(v, n)) }
}

class MapRng[K, V](implicit r: Rng[V]) extends MapSemiring[K, V] with Rng[Map[K, V]] {
  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, r.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.combineMap(x, y)(r.minus)(r.negate)
}
