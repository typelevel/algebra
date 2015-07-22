package algebra
package laws

class LawTests extends LawTestsBase {

 // The scalacheck defaults (100,100) are too high for Scala-js, so we reduce to 10/100. 
 implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfig(maxSize = 10 , minSuccessful = 100)
}
