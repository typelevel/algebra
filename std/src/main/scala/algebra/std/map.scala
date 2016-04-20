package algebra
package std

import algebra.ring._
import algebra.std.util.StaticMethods.{addMap, initMutableMap, subtractMap, wrapMutableMap}
import scala.collection.mutable

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

class MapEq[K, V](implicit V: Eq[V]) extends Eq[Map[K, V]] {
  def eqv(x: Map[K, V], y: Map[K, V]): Boolean =
    x.size == y.size && x.forall { case (k, v1) =>
      y.get(k) match {
        case Some(v2) => V.eqv(v1, v2)
        case None => false
      }
    }
}

class MapVectorEq[K, V](implicit V0: Eq[V], V1: AdditiveMonoid[V]) extends Eq[Map[K, V]] {
  private[this] def check(x: Map[K, V], y: Map[K, V]): Boolean =
    x.forall { case (k, v1) =>
      y.get(k) match {
        case Some(v2) => V0.eqv(v1, v2)
        case None => V1.isZero(v1)
      }
    }

  def eqv(x: Map[K, V], y: Map[K, V]): Boolean =
    (x eq y) || (check(x, y) && check(y, x))
}

class MapMonoid[K, V](implicit V: Semigroup[V]) extends Monoid[Map[K, V]]  {
  def neutral: Map[K, V] = Map.empty

  def combine(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    addMap(x, y)(V.combine)
}

class MapGroup[K, V](implicit V: Group[V]) extends MapMonoid[K, V] with Group[Map[K, V]] {
  def inverse(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, V.inverse(v)) }

  override def remove(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    subtractMap(x, y)(V.remove)(V.inverse)
}

class MapAdditiveMonoid[K, V](implicit V: AdditiveSemigroup[V]) extends AdditiveMonoid[Map[K, V]]  {
  def zero: Map[K, V] = Map.empty

  def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    addMap(x, y)(V.plus)
}

class MapAdditiveGroup[K, V](implicit V: AdditiveGroup[V]) extends MapAdditiveMonoid[K, V] with AdditiveGroup[Map[K, V]] {
  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, V.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    subtractMap(x, y)(V.minus)(V.negate)
}

class MapSemiring[K, V](implicit V: Semiring[V]) extends MapAdditiveMonoid[K, V] with Semiring[Map[K, V]] {

  override def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    if (y.size < x.size) plus(y, x) else {
      val m = initMutableMap(y)
      x.foreach { case (k, v1) =>
        m(k) = m.get(k) match {
          case Some(v2) => V.plus(v1, v2)
          case None => v1
        }
      }
      wrapMutableMap(m)
    }

  def times(x: Map[K, V], y: Map[K, V]): Map[K, V] = {
    // we can't just flip arguments to times() since
    // V.times is not guaranteed to be commutative.
    val (small, big, f) =
      if (x.size <= y.size) (x, y, V.times _)
      else (y, x, (v1: V, v2: V) => V.times(v2, v1))

    val m = mutable.Map.empty[K, V]
    small.foreach { case (k, v1) =>
      big.get(k) match {
        case Some(v2) => m(k) = f(v1, v2)
        case None => ()
      }
    }
    wrapMutableMap(m)
  }

  override def pow(x: Map[K, V], n: Int): Map[K, V] =
    x.map { case (k, v) => (k, V.pow(v, n)) }
}

class MapRng[K, V](implicit V: Rng[V]) extends MapSemiring[K, V] with Rng[Map[K, V]] {
  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, V.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    subtractMap(x, y)(V.minus)(V.negate)
}
