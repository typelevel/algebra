package algebra.laws

sealed trait SerializationCheck
case object RunSerialization extends SerializationCheck
case object SkipSerialization extends SerializationCheck

/**
  * ADT that allows users to skip the serialization tests for certain
  * instances. To skip serialization, include
  *
  *   implicit val check = SkipSerialization
  *
  * In your test's scope.
  */
object SerializationCheck {
  implicit val check: SerializationCheck = RunSerialization
}
