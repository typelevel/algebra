package algebra
package std

import algebra.ring.{AdditiveMonoid, Rng}

object map extends MapInstances

trait MapInstances {
  implicit def mapEq[K, V: Eq: AdditiveMonoid]: Eq[Map[K, V]] =
    new MapEq[K, V]
  
  implicit def mapRng[K, V: Rng] =
    new MapRng[K, V]
}

class MapEq[K, V](implicit ev: Eq[V], a: AdditiveMonoid[V]) extends Eq[Map[K, V]] {
  private[this] def check(x: Map[K, V], y: Map[K, V]): Boolean =
    x.forall { case (k, v) =>
      y.get(k) match {
        case Some(w) => ev.eqv(w, v)
        case None => a.isZero(v)
      }
    }

  def eqv(x: Map[K, V], y: Map[K, V]): Boolean =
    check(x, y) && check(y, x)
}

class MapRng[K, V](implicit ev: Rng[V]) extends Rng[Map[K, V]] {
  def zero: Map[K, V] = Map.empty

  def plus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    y.foldLeft(x) { case (m, (k, v)) =>
      m.get(k) match {
        case Some(w) => m.updated(k, ev.plus(w, v))
        case None => m.updated(k, v)
      }
    }

  def negate(x: Map[K, V]): Map[K, V] =
    x.map { case (k, v) => (k, ev.negate(v)) }

  override def minus(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    y.foldLeft(x) { case (m, (k, v)) =>
      m.get(k) match {
        case Some(w) => m.updated(k, ev.minus(w, v))
        case None => m.updated(k, ev.negate(v))
      }
    }

  def times(x: Map[K, V], y: Map[K, V]): Map[K, V] =
    y.foldLeft(Map.empty[K, V]) { case (m, (k, v)) =>
      x.get(k) match {
        case Some(w) => m.updated(k, ev.times(w, v))
        case None => m
      }
    }
}
