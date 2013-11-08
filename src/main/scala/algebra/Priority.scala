package algebra

sealed trait Priority[+F, +P] {
  def fold[B](f1: F => B)(f2: P => B): B =
    this match {
      case Fallback(x) => f1(x)
      case Preferred(y) => f2(y)
    }

  def join[U >: F with P]: U = fold(_.asInstanceOf[U])(_.asInstanceOf[U])

  def bimap[F2, P2](f1: F => F2)(f2: P => P2): Priority[F2, P2] =
    this match {
      case Fallback(x) => Fallback(f1(x))
      case Preferred(y) => Preferred(f2(y))
    }

  def toEither: Either[F, P] = fold[Either[F, P]](f => Left(f))(p => Right(p))

  def isPreferred: Boolean = fold(_ => false)(_ => true)
  def isFallback: Boolean = fold(_ => true)(_ => false)

  def getPreferred: Option[P] = fold[Option[P]](_ => None)(p => Some(p))
  def getFallback: Option[F] = fold[Option[F]](f => Some(f))(_ => None)
}

case class Preferred[P](get: P) extends Priority[Nothing, P]
case class Fallback[F](get: F) extends Priority[F, Nothing]

object Priority extends FindPreferred {
  def apply[F, P](implicit ev: Priority[F, P]): Priority[F, P] = ev
}

trait FindPreferred extends FindFallback {
  implicit def preferred[P](implicit ev: P): Priority[Nothing, P] = Preferred(ev)
}

trait FindFallback {
  implicit def fallback[F](implicit ev: F): Priority[F, Nothing] = Fallback(ev)
}
