[![Build Status](https://api.travis-ci.org/non/algebra.png)](https://travis-ci.org/non/algebra/)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/non/algebra?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.spire-math/algebra_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.spire-math/algebra_2.11)

# algebra

## the vision

This repo represents an attempt to unify the basic algebraic type
classes from [Spire](http://github.com/non/spire) and
[Algebird](http://github.com/twitter/algebird). By targeting just
these type classes, we will distribute an `algebra` package with no
dependencies that works with Scala 2.10 and 2.11, and which can be
shared by all Scala libraries interested in abstract algebra.

## getting algebra

Algebra supports Scala 2.10 and 2.11, and is available from Sonatype
(and Maven Central). To use algebra in your own projects, include this
snippet in your `build.sbt` file:

```scala
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.spire-math" %% "algebra" % "0.3.1"
```

As of 0.3.1 algebra also supports scala.js!

## what we have so far

This repo has been seeded with most of the contents of
`spire.algebra`, and has been modified to fit an
initially-conservative and shared vision for algebraic type classes.
Discussions over removals and additions should take place on the issue
tracker or on relevant pull requests.

## participants

Anyone who wants to participate should feel free to send pull requests
to this repo. The following people have push access:

* Oscar Boykin
* Avi Bryant
* Lars Hupel
* Erik Osheim
* Tom Switzer

## development process

Please make a pull request against the master branch. For those that
have merge access to the repo, we follow these rules:

1. Do not merge your own PR unless *N* people have signed-off on the
   PR (e.g. given a thumbs-up, +1, shipit, etc) and Travis is green.

2. If you are not the author, and you see *N-1* sign-offs and Travis
   is green, just click merge and delete the branch.

3. Currently, *N = 1*.

## algebra overview

Algebra uses type classes to represent algebraic structures. You can
use these type classes to represent the abstract capabilities (and
requirements) you want generic parameters to possess.

This section will explain the structures available.

### algebraic properties and terminology

We will be talking about properties like *associativity* and
*commutativity*. Here is a quick explanation of what those properties
mean:

|Name         |Description                                                                     |
|-------------|--------------------------------------------------------------------------------|
|Associative  | If `⊕` is associative, then `a ⊕ (b ⊕ c)` = `(a ⊕ b) ⊕ c`.                    |
|Commutative  | If `⊕` is commutative, then `a ⊕ b` = `b ⊕ a`.                                 |
|Identity     | If `id` is an identity for `⊕`, then `a ⊕ id` = `id ⊕ a` = `a`.                |
|Inverse      | If `¬` is an inverse for `⊕` and `id`, then `a ⊕ ¬a` = `¬a ⊕ a` = `id`.        |
|Distributive | If `⊕` and `⊙` distribute, then `a ⊙ (b ⊕ c)` = `(a ⊙ b) ⊕ (a ⊙ c)` and `(a ⊕ b) ⊙ c` = `(a ⊙ c) ⊕ (b ⊙ c)`. |
|Idempotent   | If `⊕` is idempotent, then `a ⊕ a` = `a`                                       |

Though these properties are illustrated with symbolic operators, they
work equally-well with functions. When you see `a ⊕ b` that is
equivalent to `f(a, b)`: `⊕` is an infix representation of the binary
function `f`, and `a` and `b` are values (of some type `A`).

Similarly, when you see `¬a` that is equivalent to `g(a)`: `¬` is a
prefix representation of the unary function `g`, and `a` is a value
(of some type `A`).

### basic algebraic structures

The most basic structures can be found in the `algebra` package. They
all implement a method called `combine`, which is associative. The
identity element (if present) will be called `empty`, and the inverse
method (if present) will be called `inverse`.

|Name                |Associative?|Commutative?|Identity?|Inverse?|Idempotent?|
|--------------------|------------|------------|---------|--------|-----------|
|Semigroup           |           ✓|            |         |        |           |
|CommutativeSemigroup|           ✓|           ✓|         |        |           |
|Monoid              |           ✓|            |        ✓|        |           |
|Band                |           ✓|            |         |        |          ✓|
|Semilattice         |           ✓|           ✓|         |        |          ✓|
|Group               |           ✓|            |        ✓|       ✓|           |
|CommutativeMonoid   |           ✓|           ✓|        ✓|        |           |
|CommutativeGroup    |           ✓|           ✓|        ✓|       ✓|           |
|BoundedSemilattice  |           ✓|           ✓|        ✓|        |          ✓|

(For a description of what each column means, see [§algebraic
properties and terminology](#algebraic-properties-and-terminology).)

### ring-like structures

The `algebra.ring` contains more sophisticated structures which
combine an *additive* operation (called `plus`) and a *multiplicative*
operation (called `times`). Additive identity and inverses will be
called `zero` and `negate` (respectively); multiplicative identity and
inverses will be called `one` and `reciprocal` (respectively).

Additionally some structures support a quotient operation, called
`quot`.

All ring-like structures are associative for both `+` and `*`, have
commutative `+`, and have a `zero` element (an identity for `+`).

|Name                |Has `negate`?|Has `1`?|Has `reciprocal`?|Has `quot`?|Commutative `*`?|
|--------------------|-------------|--------|-----------------|-----------|----------------|
|Semiring            |             |        |                 |           |                |
|Rng                 |            ✓|        |                 |           |                |
|Rig                 |             |       ✓|                 |           |                |
|CommutativeRig      |             |       ✓|                 |           |               ✓|
|Ring                |            ✓|       ✓|                 |           |                |
|CommutativeRing     |            ✓|       ✓|                 |           |               ✓|
|EuclideanRing       |            ✓|       ✓|                 |          ✓|               ✓|
|Field               |            ✓|       ✓|                ✓|          ✓|               ✓|

With the exception of `CommutativeRig` and `Rng`, every lower
structure is also an instance of the structures above it. For example,
every `Ring` is a `Rig`, every `Field` is a `CommutativeRing`, and so
on.

(For a description of what the terminology in each column means, see
[§algebraic properties and terminology](#algebraic-properties-and-terminology).)

### lattice-like structures

The `algebra.lattice` contains more structures that can be somewhat ring-like.
Rather than `plus` and `times` we have `meet` and `join` both of which are always
associative, commutative and idempotent, and as such each can be viewed as a
semilattice. Meet can be thought of as the greatest lower bound of two items while
join can be thought of as the least upper bound between two items.

When zero is present, join(a, zero) = a. When one is present meet(a, one) = a.

When meet and join are both present, they obey the absorption law:

 -  meet(a, join(a, b)) = join(a, meet(a, b)) = a

Sometimes meet and join distribute, we say it is distributive in this case:
 - meet(a, join(b, c)) = join(meet(a, b), meet(a, c))
 - join(a, meet(b, c)) = meet(join(a, b), join(a, c))

Sometimes an additional binary operation `imp` (for impliciation, also
written as →, meet written as ∧) is present.  Implication obeys the following laws:

 - a → a = 1
 - a ∧ (a → b) = a ∧ b
 - b ∧ (a → b) = b
 - a → (b ∧ c) = (a → b) ∧ (a → c)

The law of the excluded middle can be expressed as:

 - (a ∨ (a → 0)) = 1

|Name                      |Has `join`?|Has `meet`?|Has `zero`?|Has `one`?|Distributive|Has `imp`?|Excludes middle?|
|--------------------------|-----------|-----------|-----------|----------|------------|----------|----------------|
|JoinSemilattice           |          ✓|           |           |          |            |          |                |
|MeetSemilattice           |           |          ✓|           |          |            |          |                |
|BoundedJoinSemilattice    |          ✓|           |          ✓|          |            |          |                |
|BoundedMeetSemilattice    |           |          ✓|           |         ✓|            |          |                |
|Lattice                   |          ✓|          ✓|           |          |            |          |                |
|DistributiveLattice       |          ✓|          ✓|           |          |           ✓|          |                |
|BoundedLattice            |          ✓|          ✓|          ✓|         ✓|            |          |                |
|BoundedDistributiveLattice|          ✓|          ✓|          ✓|         ✓|           ✓|          |                |
|Heyting                   |          ✓|          ✓|          ✓|         ✓|           ✓|         ✓|                |
|Bool                      |          ✓|          ✓|          ✓|         ✓|           ✓|         ✓|               ✓|

Note that a BoundedDistributiveLattice gives you a CommutativeRig, but not the other way around
(since rigs aren't distributive with `a + (b * c) = (a + b) * (a + c)`). Also, a Bool gives rise to
a BoolRing, since each element can be defined as its own negation. Note, Bool's
asBoolRing is not an extension of the asCommutativeRig as the `plus` operations are defined
differently.
