package algebra
import algebra.ring._

import scala.{ specialized => sp }

/**
 * A module generalizes a vector space by requiring its scalar need only form
 * a ring, rather than a field.
 */
trait Module[V, @sp(Int,Long,Float,Double) R] extends AdditiveCommutativeGroup[V] {
  implicit def scalar: Rng[R]

  def timesl(r: R, v: V): V
  def timesr(v: V, r: R): V = timesl(r, v)
}

trait ModuleFunctions extends AdditiveGroupFunctions {
  def scalar[V, @sp(Int,Long,Float,Double) R](implicit ev: Module[V, R]): Rng[R] =
    ev.scalar
  def timesl[V, @sp(Int,Long,Float,Double) R](r: R, v: V)(implicit ev: Module[V, R]): V =
    ev.timesl(r, v)
  def timesr[V, @sp(Int,Long,Float,Double) R](v: V, r: R)(implicit ev: Module[V, R]): V =
    ev.timesr(v, r)
}

object Module extends ModuleFunctions {
  @inline final def apply[V, @sp(Int,Long,Float,Double) R](implicit V: Module[V, R]) = V

  implicit def IdentityModule[@sp(Int,Long,Float,Double) V](implicit ring: Ring[V]) =
    new IdentityModule[V] {
      val scalar = ring
    }
}

private[algebra] trait IdentityModule[@sp(Int,Long,Float,Double) V] extends Module[V, V] {
  def zero = scalar.zero
  def negate(v: V) = scalar.negate(v)
  def plus(v: V, w: V): V = scalar.plus(v, w)
  override def minus(v: V, w: V): V = scalar.minus(v, w)

  def timesl(r: V, v: V): V = scalar.times(r, v)
}
