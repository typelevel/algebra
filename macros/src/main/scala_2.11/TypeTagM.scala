package algebra
package macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
 * TypeTag is runtime reflection, so is not/will not be in scala.js, hence the macro implementation.
 */ 
case class TypeTagM[T](tpe: String)

object TypeTagM {
  implicit def apply[T]: TypeTagM[T] = macro TypeTagMacros.applyImpl[T]
}

class TypeTagMacros(val c: blackbox.Context) {
  import c.universe._

  def applyImpl[T](implicit tTag: WeakTypeTag[T]): c.Expr[TypeTagM[T]] = {
    val tTpe = weakTypeOf[T]
    c.Expr[TypeTagM[T]](q"TypeTagM[$tTpe](${tTpe.toString})")
  }
}
