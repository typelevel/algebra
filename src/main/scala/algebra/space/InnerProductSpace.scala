package algebra
package space

import algebra.ring.Field
import algebra.number.NRoot

import scala.{ specialized => sp }
import scala.annotation.tailrec

trait InnerProductSpace[V, @sp(Int, Long, Float, Double) F] extends VectorSpace[V, F] { self =>
  def dot(v: V, w: V): F

  def normed(implicit ev: NRoot[F]): NormedVectorSpace[V, F] = new NormedInnerProductSpace[V, F] {
    def space = self
    def nroot = ev
  }
}

trait InnerProductSpaceFunctions extends VectorSpaceFunctions {
  def dot[V, @sp(Int, Long, Float, Double) F](v: V, w: V)(implicit ev: InnerProductSpace[V, F]): F =
    ev.dot(v, w)
  def normed[V, @sp(Int, Long, Float, Double) F](implicit ev0: InnerProductSpace[V, F], ev1: NRoot[F]): NormedVectorSpace[V, F] =
    ev0.normed
}

object InnerProductSpace extends InnerProductSpaceFunctions {
  @inline final def apply[V, @sp(Int,Long,Float,Double) R](implicit V: InnerProductSpace[V, R]) = V
}

private[algebra] trait NormedInnerProductSpace[V, @sp(Float, Double) F] extends NormedVectorSpace[V, F] {
  def space: InnerProductSpace[V, F]
  def scalar: Field[F] = space.scalar
  def nroot: NRoot[F]

  def zero: V = space.zero
  def plus(v: V, w: V): V = space.plus(v, w)
  def negate(v: V): V = space.negate(v)
  override def minus(v: V, w: V): V = space.minus(v, w)
  def timesl(f: F, v: V): V = space.timesl(f, v)
  override def divr(v: V, f: F): V = space.divr(v, f)
  def norm(v: V): F = nroot.sqrt(space.dot(v, v))
}
