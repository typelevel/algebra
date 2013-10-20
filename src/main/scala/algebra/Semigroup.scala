package algebra

import scala.{ specialized => sp }
import scala.annotation.{ switch, tailrec }

/**
 * A semigroup is any set `A` with an associative operation (`op`).
 */
trait Semigroup[@sp(Boolean, Byte, Short, Int, Long, Float, Double) A] {
  def op(x: A, y: A): A

  /**
   * Return `a` appended to itself `n` times.
   */
  def sumn(a: A, n: Int): A =
    if (n <= 0) throw new IllegalArgumentException("Repeated summation for semigroups must have reptitions > 0")
    else positiveSumn(a, n)

  protected def positiveSumn(a: A, n: Int): A = {
    @tailrec def loop(b: A, k: Int, extra: A): A =
      (k: @annotation.switch) match {
        case 0 => b
        case 1 => op(b, extra)
        case n => loop(op(b, b), k >>> 1, if ((k & 1) == 1) op(b, extra) else extra)
      }
    loop(a, n - 1, a)
  }

  /**
   * Given a sequence of `as`, sum them using the semigroup and return the total.
   * 
   * If the sequence is empty, returns None. Otherwise, returns Some(total).
   */
  def sumOption(as: TraversableOnce[A]): Option[A] = as.reduceOption(op)
}

trait SemigroupFunctions {
  def op[@sp(Boolean,Byte,Short,Int,Long,Float,Double) A](x: A, y: A)(implicit ev: Semigroup[A]): A =
    ev.op(x, y)

  def maybeOp[@sp(Boolean,Byte,Short,Int,Long,Float,Double) A](ox: Option[A], y: A)(implicit ev: Semigroup[A]): A =
    ox.map(x => ev.op(x, y)).getOrElse(y)

  def sumn[@sp(Boolean,Byte,Short,Int,Long,Float,Double) A](a: A, n: Int)(implicit ev: Semigroup[A]): A =
    ev.sumn(a, n)

  def sumOption[@sp(Boolean,Byte,Short,Int,Long,Float,Double) A](as: TraversableOnce[A])(implicit ev: Semigroup[A]): Option[A] =
    ev.sumOption(as)
}

object Semigroup extends SemigroupFunctions {
  @inline final def apply[A](implicit s: Semigroup[A]) = s
}
