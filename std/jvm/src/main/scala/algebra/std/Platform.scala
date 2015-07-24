package algebra
package std

import java.lang.Math

private[std] object Platform {
  
  // Math.rint is not in Scala-js. See https://github.com/scala-js/scala-js/issues/1797
  @inline
  def rint(x: Double): Double = Math.rint(x)
}
