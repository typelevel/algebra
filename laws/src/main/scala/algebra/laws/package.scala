package algebra

import algebra.ring.AdditiveMonoid

import org.typelevel.discipline.Predicate

package object laws {
  implicit def PredicateFromMonoid[A: Eq](implicit A: AdditiveMonoid[A]): Predicate[A] =
    new Predicate[A] {
      def apply(a: A) = a != A.zero
    }
}
