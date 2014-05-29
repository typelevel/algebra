package algebra

object Instances {

  def t2HasSemigroup[A, B](implicit eva: Semigroup[A], evb: Semigroup[B]) =
    new Semigroup[(A, B)] {
      def combine(x: (A, B), y: (A, B)): (A, B) =
        (eva.combine(x._1, y._1), evb.combine(x._2, y._2))
    }

  val stringHasMonoid =
    new Monoid[String] {
      def empty: String = ""
      def combine(x: String, y: String): String = x + y
    }

  def f1ComposeMonoid[A] =
    new Monoid[A => A] {
      def empty: A => A =
        a => a
      def combine(x: A => A, y: A => A): A => A =
        a => y(x(a))
    }

  def f1HomomorphismMonoid[A, B](implicit ev: Monoid[B]) =
    new Monoid[A => B] {
      def empty: A => B =
        _ => ev.empty
      def combine(x: A => B, y: A => B): A => B =
        a => ev.combine(x(a), y(a))
    }

  val charHasRig =
    new ring.Rig[Char] {
      def zero: Char = '\u0000'
      def one: Char = '\u0001'

      def plus(x: Char, y: Char): Char = (x + y).toChar
      def times(x: Char, y: Char): Char = (x * y).toChar
    }

  val longHasEuclideanRing =
    new ring.EuclideanRing[Long] {
      def zero: Long = 0L
      def one: Long = 1L

      def negate(x: Long): Long = -x
      def plus(x: Long, y: Long): Long = x + y
      def times(x: Long, y: Long): Long = x * y
      def quot(x: Long, y: Long): Long = x / y
      def mod(x: Long, y: Long): Long = x % y

      override def minus(x: Long, y: Long): Long = x - y

      override def fromInt(x: Int): Long = x.toLong
    }

  val doubleHasField =
    new ring.Field[Double] {
      def zero: Double = 0.0
      def one: Double = 1.0

      def negate(x: Double): Double = -x
      def plus(x: Double, y: Double): Double = x + y
      def times(x: Double, y: Double): Double = x * y
      def div(x: Double, y: Double): Double = x / y

      override def minus(x: Double, y: Double): Double = x - y
      override def mod(x: Double, y: Double): Double = x % y
      override def quot(x: Double, y: Double): Double = (x - (x % y)) / y

      override def fromInt(x: Int): Double = x.toDouble
      override def fromDouble(x: Double): Double = x
    }
}
