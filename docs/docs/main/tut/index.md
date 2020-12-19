---
layout: home
title:  "Home"
section: "home"
---

Algebra unifies the basic algebraic type classes from [Spire](http://github.com/non/spire) and [Algebird](http://github.com/twitter/algebird). By targeting just these type classes `algebra` offers a package with no dependencies (except for cats-kernel) that works with Scala 2.11 and 2.12, which can be shared by all Scala libraries interested in abstract algebra.

Algebra also interoperates with the [Cats](http://github.com/typelevel/cats) project. Algebra and Cats interoperate using the *cats-kernel* module.

## getting algebra

Algebra supports Scala 2.11 and 2.12 and is available from Sonatype (and Maven Central). In addition to the JVM, Algebra also supports Scala.js.

To use algebra in your own projects, include this snippet in your `build.sbt` file:

```scala
libraryDependencies += "org.typelevel" %% "algebra" % "1.0.1"
```

If you want to use Algebra's laws, you can include those as well with this snippet:

```scala
libraryDependencies += "org.typelevel" %% "algebra-laws" % "1.0.1"
```

## what we have so far

This repo has been seeded with most of the contents of `spire.algebra`, and has been modified to fit an initially-conservative and shared vision for algebraic type classes.  Discussions over removals and additions should take place on the issue tracker or on relevant pull requests.

## Copyright and License

All code is available to you under the [MIT license](http://opensource.org/licenses/mit-license.php).

Copyright the maintainers, 2015-2016.
