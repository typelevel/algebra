package algebra

import scala.{specialized => sp}

import simulacrum._


/**
 * Bands are semigroups whose operation
 * (i.e. combine) is also idempotent.
 */
@typeclass trait Band[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] extends Any with Semigroup[A]
