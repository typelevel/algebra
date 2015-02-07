package algebra
package std

import algebra.ring._
import scala.annotation.tailrec
import scala.collection.mutable

package object option extends OptionInstances 

trait OptionInstances {
  implicit def optionOrder[A: Order] = new OptionOrder[A]
  implicit def optionMonoid[A: Semigroup] = new OptionMonoid[A]
}

trait OptionInstances0 {
  implicit def optionEq[A: Eq] = new OptionEq[A]
}

class OptionOrder[A](implicit A: Order[A]) extends Order[Option[A]] {
  def compare(x: Option[A], y: Option[A]): Int =
    x match {
      case None =>
        if (y.isEmpty) 0 else -1
      case Some(a) =>
        y match {
          case None => 1
          case Some(b) => A.compare(a, b)
        }
    }
}

class OptionEq[A](implicit A: Eq[A]) extends Eq[Option[A]] {
  def eqv(x: Option[A], y: Option[A]): Boolean =
    x match {
      case None => y.isEmpty
      case Some(a) =>
        y match {
          case None => false
          case Some(b) => A.eqv(a, b)
        }
    }
}

class OptionMonoid[A](implicit A: Semigroup[A]) extends Monoid[Option[A]] {
  def empty: Option[A] = None
  def combine(x: Option[A], y: Option[A]): Option[A] =
    x match {
      case None => y
      case Some(a) =>
        y match {
          case None => x
          case Some(b) => Some(A.combine(a, b))
        }
    }
}
