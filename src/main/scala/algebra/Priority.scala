package algebra

sealed trait Priority[+P, +F] {
  def fold[B](f1: P => B)(f2: F => B): B =
    this match {
      case Preferred(x) => f1(x)
      case Fallback(y) => f2(y)
    }

  def join[U >: P with F]: U = fold(_.asInstanceOf[U])(_.asInstanceOf[U])

  def bimap[P2, F2](f1: P => P2)(f2: F => F2): Priority[P2, F2] =
    this match {
      case Preferred(x) => Preferred(f1(x))
      case Fallback(y) => Fallback(f2(y))
    }

  def toEither: Either[P, F] = fold[Either[P, F]](p => Left(p))(f => Right(f))

  def isPreferred: Boolean = fold(_ => true)(_ => false)
  def isFallback: Boolean = fold(_ => false)(_ => true)

  def getPreferred: Option[P] = fold[Option[P]](p => Some(p))(_ => None)
  def getFallback: Option[F] = fold[Option[F]](_ => None)(f => Some(f))
}

case class Preferred[P](get: P) extends Priority[P, Nothing]
case class Fallback[F](get: F) extends Priority[Nothing, F]

object Priority extends FindPreferred {
  def apply[P, F](implicit ev: Priority[P, F]): Priority[P, F] = ev
}

trait FindPreferred extends FindFallback {
  implicit def preferred[P](implicit ev: P): Priority[P, Nothing] = Preferred(ev)
}

trait FindFallback {
  implicit def fallback[F](implicit ev: F): Priority[Nothing, F] = Fallback(ev)
}
