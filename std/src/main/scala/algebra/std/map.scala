package algebra
package std

import algebra.ring._
import algebra.std.util.StaticMethods
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

class MapEq[K, V](implicit ev: Eq[V], a: AdditiveMonoid[V]) extends Eq[Map[K, V]] {
  def eqv(x: Map[K, V], y: Map[K, V]): Boolean =
    x.size == y.size && x.forall { case (k, v1) =>
      y.get(k) match {
        case Some(v2) => ev.eqv(v1, v2)
        case None => false
      }
    }
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

class MapMonoid[K, V](implicit sg: Semigroup[V]) extends Monoid[Map[K, V]]  {
  def empty: Map[K, V] = Map.empty

  def combine(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.addMap(x, y)(sg.combine)
}

class MapGroup[K, V](implicit g: Group[V]) extends MapMonoid[K, V] with Group[Map[K, V]] {
  def inverse(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, g.inverse(v)) }

  override def remove(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.subtractMap(x, y)(g.remove)(g.inverse)
}

class MapAdditiveMonoid[K, V](implicit sg: AdditiveSemigroup[V]) extends AdditiveMonoid[Map[K, V]]  {
  def zero: Map[K, V] = Map.empty

  def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.addMap(x, y)(sg.plus)
}

class MapAdditiveGroup[K, V](implicit g: AdditiveGroup[V]) extends MapAdditiveMonoid[K, V] with AdditiveGroup[Map[K, V]] {
  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, g.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.subtractMap(x, y)(g.minus)(g.negate)
}

class MapSemiring[K, V](implicit val sr: Semiring[V]) extends MapAdditiveMonoid[K, V] with Semiring[Map[K, V]] {

  override def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    if (y.size < x.size) plus(y, x) else {
      val m = StaticMethods.initMutableMap(y)
      x.foreach { case (k, v1) =>
        m(k) = m.get(k) match {
          case Some(v2) => sr.plus(v1, v2)
          case None => v1
        }
      }
      m.toMap
    }

  def times(x: Map[K, V], y: Map[K, V]): Map[K, V] = {
    // we can't just flip arguments to times() since
    // sr.times is not guaranteed to be commutative.
    val (small, big, f) =
      if (x.size <= y.size) (x, y, sr.times _)
      else (y, x, (v1: V, v2: V) => sr.times(v2, v1))

    val m = mutable.Map.empty[K, V]
    small.foreach { case (k, v1) =>
      big.get(k) match {
        case Some(v2) => m(k) = f(v1, v2)
        case None => ()
      }
    }
    m.toMap
  }

  override def pow(x: Map[K, V], n: Int): Map[K, V] =
    x.map { case (k, v) => (k, sr.pow(v, n)) }
}

class MapRng[K, V](implicit r: Rng[V]) extends MapSemiring[K, V] with Rng[Map[K, V]] {
  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, r.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    StaticMethods.subtractMap(x, y)(r.minus)(r.negate)
}
