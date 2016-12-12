# algebra-benchmark

[jmh](http://openjdk.java.net/projects/code-tools/jmh/)-based Benchmarks for Algebra typeclasses and data structures.

## Usage

Run the following commands from the top-level Algebra directory:

    $ ./sbt   # <<< enter sbt REPL
    > project benchmark

Now you can run the following commands from within the sbt REPL:

    # List available benchmarks
    > jmh:run -l

    # Run a particular benchmark
    > jmh:run -t1 -f1 -wi 2 -i 3 .*ExampleBenchmark.*

    # Run all benchmarks
    > jmh:run .*

These options tell JMH to run the benchmark with 1 thread (`-t1`), 1 fork (`-f1`), 2 warmup iterations and 3 real iterations. You can find further details in the [sbt-jmh](https://github.com/ktoso/sbt-jmh) documentation.

Example output from `jmh:run -t1 -f1 -wi 2 -i 3 .*ExampleBenchmark.*`:

```
[info] # Run complete. Total time: 00:00:11
[info]
[info] Benchmark                      (numElements)   Mode  Cnt      Score       Error  Units
[info] ExampleBenchmark.timeFoldLeft          10000  thrpt    3  20730.607 ±  4294.652  ops/s
[info] ExampleBenchmark.timeReduce            10000  thrpt    3  20032.976 ± 45474.075  ops/s
```

## Writing Benchmarks

Please use `ExampleBenchmark` as a template!
