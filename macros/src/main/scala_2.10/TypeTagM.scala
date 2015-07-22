package algebra
package macros


import scala.language.experimental.macros
import scala.reflect.macros.Context

/**
 * TypeTag is runtime reflection, so is not/will not be in scala.js, hence the macro implementation.
 * 
 * This 2.10 version exists only because the nicer syntax in the 2.11 version is incompatible with 2.10,
 * and for the sake of progress, we have the two versions.
 */
trait TypeTagM[T]{
  def tpe: String
}

object TypeTagM {
  implicit def apply[T]: TypeTagM[T] = macro TypeTagMacros.applyImpl[T]
}

class TypeTagMacros[C <: Context](val c: C ) {
  import c.universe._

  def applyImpl[T](implicit tTag: WeakTypeTag[T]): Tree = {
    val tTpe = weakTypeOf[T]
    q"new TypeTagM[$tTpe]{def tpe = ${tTpe.toString}}"
  }
}

object TypeTagMacros {
  def inst(c: Context) = new TypeTagMacros[c.type](c)

  def applyImpl[T: c.WeakTypeTag](c: Context): c.Expr[TypeTagM[T]] =
   c.Expr[TypeTagM[T]](inst(c).applyImpl[T])
}
