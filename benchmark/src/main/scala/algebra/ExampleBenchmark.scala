package algebra
package benchmark

import scala.util.Random
import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

object ExampleBenchmark {
  @State(Scope.Benchmark)
  class ExampleState {
    @Param(Array("10000"))
    var numElements: Int = 0

    var inputData: Seq[Long] = _

    @Setup(Level.Trial)
    def setup(): Unit = {
      inputData = Seq.fill(numElements)(Random.nextInt(1000).toLong)
    }
  }
}

class ExampleBenchmark {
  import ExampleBenchmark._

  @Benchmark
  def timeReduce(state: ExampleState, bh: Blackhole) =
    bh.consume(state.inputData.reduce(_ + _))

  @Benchmark
  def timeFoldLeft(state: ExampleState, bh: Blackhole) =
    bh.consume(state.inputData.foldLeft(0L)(_ + _))
}
