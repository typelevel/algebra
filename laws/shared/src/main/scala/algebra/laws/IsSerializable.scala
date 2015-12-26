package algebra.laws

import scala.util.DynamicVariable

/**
  * Object with a dynamic variable that allows users to skip the
  * serialization tests for certain instances.
  */
object IsSerializable {
  val runTests = new DynamicVariable[Boolean](true)
  def apply(): Boolean = runTests.value
}
