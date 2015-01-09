package algebra

/**
 * A type class used to determine equality between 2 instances of the same
 * type. Any 2 instances `x` and `y` are equal if `eqv(x, y)` is `true`.
 * Moreover, `eqv` should form an equivalence relation.
 */
trait Eq[@mb @sp A] extends Any { self =>

  /**
   * Returns `true` if `x` and `y` are equivalent, `false` otherwise.
   */
  def eqv(x: A, y: A): Boolean

  /**
   * Returns `false` if `x` and `y` are equivalent, `true` otherwise.
   */
  def neqv(x: A, y: A): Boolean = !eqv(x, y)

  /**
   * Constructs a new `Eq` instance for type `B` where 2 elements are
   * equivalent iff `eqv(f(x), f(y))`.
   */
  def on[@mb @sp B](f: B => A): Eq[B] =
    new Eq[B] {
      def eqv(x: B, y: B): Boolean = self.eqv(f(x), f(x))
    }
}

trait EqFunctions {
  def eqv[A](x: A, y: A)(implicit ev: Eq[A]): Boolean =
    ev.eqv(x, y)

  def neqv[A](x: A, y: A)(implicit ev: Eq[A]): Boolean =
    ev.neqv(x, y)
}

object Eq extends EqFunctions {

  /**
   * Access an implicit `Eq[A]`.
   */
  @inline final def apply[A](implicit ev: Eq[A]): Eq[A] = ev

  /**
   * Convert an implicit `Eq[A]` to an `Eq[B]` using the given
   * function `f`.
   */
  def by[@mb @sp A, @mb @sp B](f: A => B)(implicit ev: Eq[B]): Eq[A] =
    new Eq[A] {
      def eqv(x: A, y: A): Boolean = ev.eqv(f(x), f(y))
    }
}
