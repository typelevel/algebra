[![Build Status](https://api.travis-ci.org/typelevel/algebra.png)](https://travis-ci.org/typelevel/algebra/)
[![Coverage status](https://img.shields.io/codecov/c/github/typelevel/algebra/master.svg)](https://codecov.io/github/typelevel/algebra)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/non/algebra?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.typelevel/algebra_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.typelevel/algebra_2.11)

# algebra

## the vision

This repo represents an attempt to unify the basic algebraic type
classes from [Spire](http://github.com/non/spire) and
[Algebird](http://github.com/twitter/algebird). By targeting just
these type classes, we will distribute an `algebra` package with no
dependencies that works with Scala 2.10, 2.11, and 2.12, which can be
shared by all Scala libraries interested in abstract algebra.

Since the creation of Algebra, we have also decided to interoperate
with the [Cats](http://github.com/typelevel/cats) project. Algebra and
Cats interoperate using the *cats-kernel* module.

See the [Algebra website](https://typelevel.org/algebra) for more information. The latest API docs are hosted at Algebra's [ScalaDoc index](https://typelevel.org/algebra/api/).

## getting algebra

Algebra supports Scala 2.10, 2.11, and 2.12, and is available from
Sonatype (and Maven Central). In addition to the JVM, Algebra also
supports Scala.js.

To use algebra in your own projects, include this snippet in your
`build.sbt` file:

```scala
libraryDependencies += "org.typelevel" %% "algebra" % "1.0.0"
```

If you want to use Algebra's laws, you can include those as well with
this snippet:

```scala
libraryDependencies += "org.typelevel" %% "algebra-laws" % "1.0.0"
```

## what we have so far

This repo has been seeded with most of the contents of
`spire.algebra`, and has been modified to fit an
initially-conservative and shared vision for algebraic type classes.
Discussions over removals and additions should take place on the issue
tracker or on relevant pull requests.

## Projects using Algebra

- [Algebird](http://github.com/twitter/algebird)
- [Spire](https://github.com/non/spire)

## participants

Anyone who wants to participate should feel free to open issues or
send pull requests to this repo.

The following people are Algebra maintainers (i.e. have push access):

* [Oscar Boykin](https://github.com/johnynek)
* [Avi Bryant](https://github.com/avibryant)
* [Lars Hupel](https://github.com/larsrh)
* [Rüdiger Klaehn](https://github.com/rklaehn)
* [Tomas Mikula](https://github.com/tomasmikula)
* [Erik Osheim](https://github.com/non)
* [Tom Switzer](https://github.com/tixxit)

## development process

Please make a pull request against the `master` branch. For those who
can merge pull requests, we follow these rules:

1. Do not merge your own PR unless *N* people have signed-off on the
   PR (e.g. given a thumbs-up, +1, shipit, etc) and Travis is green.

2. If you are not the author, and you see *(N - 1)* sign-offs and
   Travis is green, just click merge and delete the branch.

3. Currently, *N* = *2*.

## algebra overview

Algebra uses type classes to represent algebraic structures. You can
use these type classes to represent the abstract capabilities (and
requirements) you want generic parameters to possess.

For a tour of the Algebraic structures that `algebra` offers, see the
extensive
[overview on the Algebra website](https://typelevel.org/algebra/typeclasses/overview.html).

### Code of Conduct

See the [Code of Conduct](CODE_OF_CONDUCT.md)

### Copyright and License

All code is available to you under the MIT license, available at
http://opensource.org/licenses/mit-license.php and also in the
[COPYING](COPYING) file.

Copyright the maintainers, 2015-2016.
