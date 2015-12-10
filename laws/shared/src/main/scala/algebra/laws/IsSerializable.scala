package algebra.laws

sealed trait IsSerializable[+T]
case class Is[+T]() extends IsSerializable[T]
case class IsNot[+T]() extends IsSerializable[T]

/**
  * ADT that allows users to skip the serialization tests for certain
  * instances. To skip serialization, include
  *
  *   implicit val check = IsNot[MyClass]
  *
  * In your test's scope or your class's companion object.
  */
object IsSerializable {
  // implicit def default[T]: IsSerializable[T] = Is()
}
