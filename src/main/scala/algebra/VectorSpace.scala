package algebra
import algebra.ring._

import scala.{ specialized => sp }

/**
 * A vector space is a group `V` that can be multiplied by scalars in `F` that
 * lie in a field. Scalar multiplication must distribute over vector addition
 * (`x *: (v + w) === x *: v + x *: w`) and scalar addition
 * (`(x + y) *: v === x *: v + y *: v`). Scalar multiplication by 1 in `F`
 * is an identity function (`1 *: v === v`). Scalar multiplication is
 * "associative" (`x *: y *: v === (x * y) *: v`).
 */
trait VectorSpace[V, @sp(Int, Long, Float, Double) F] extends Module[V, F] {
  implicit def scalar: Field[F]

  def divr(v: V, f: F): V = timesl(scalar.reciprocal(f), v)
}

trait VectorSpaceFunctions extends ModuleFunctions {
  def scalar[V, @sp(Int,Long,Float,Double) F](implicit ev: VectorSpace[V, F]): Field[F] =
    ev.scalar
  def divr[V, @sp(Int,Long,Float,Double) F](v: V, f: F)(implicit ev: VectorSpace[V, F]): V =
    ev.divr(v, f)
}

object VectorSpace extends VectorSpaceFunctions {
  @inline final def apply[V, @sp(Int,Long,Float,Double) R](implicit V: VectorSpace[V, R]) = V
}
