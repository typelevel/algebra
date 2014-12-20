# algebra

## the vision

This repo represents an attempt to unify the basic algebraic type
classes from [Spire](http://github.com/non/spire) and
[Algebird](http://github.com/twitter/algebird). By targeting just
these type classes, the hope is that we can distribute a package with
no dependencies that works with Scala 2.9 and 2.10+.

## what we have so far

This repo has been seeded with most of the contents of
`spire.algebra`. Over the next week or two we will be removing
"extraneous" type classes and adding additional ones as
needed. Discussions over removals and additions shoudl take place on
pull requests.

## participants

Anyone who wants to participate should feel free to send pull requests
to this repo. The following people have push access:

* Oscar Boykin
* Avi Bryant
* Lars Hupel
* Erik Osheim
* Tom Switzer

## development process
Please make a pull request against the master branch. For those that have merge access to the
repo, we follow these rules:

1. You may not merge your own PR unless `N` people have given you a shipit/+1 etc... and Travis CI is
   green.
2. If you are not the author, and you see `N-1` shipits and Travis CI is green, just click
merge and delete the branch.
3. N = 2.

## algebra overview

Algebra uses type classes to represent algebraic structures. You can use
these type classes to represent the abstract capabilities (and requirements)
you want generic parameters to possess.

This section will explain the structures available.

### basic algebraic structures

The most basic structures can be found in the `algebra` package. They
all implement a method called `combine`, which is associative. The
identity element (if present) will be called `empty`, and the inverse
method (if present) will be called `inverse`.

|Name                |Associative?|Commutative?|Identity?|Inverse?|Idempotent?|
|--------------------|------------|------------|---------|--------|-----------|
|Semigroup           |           ✓|            |         |        |           |
|CommutativeSemigroup|           ✓|           ✓|         |        |           |
|Semilattice         |           ✓|           ✓|         |        |          ✓|
|Monoid              |           ✓|            |        ✓|        |           |
|CommutativeMonoid   |           ✓|           ✓|        ✓|        |           |
|BoundedSemilattice  |           ✓|           ✓|        ✓|        |          ✓|
|Group               |           ✓|            |        ✓|       ✓|           |
|CommutativeGroup    |           ✓|           ✓|        ✓|       ✓|           |
|--------------------|------------|------------|---------|--------|-----------|

(For a description of what each column means, see *§algebraic
properties and terminology*.)

### ring-like structures

The `algebra.ring` contains more sophisticated structures which
combine an *additive* operation (called `plus`) and a *multiplicative*
operation (called `times`). Additive identity and inverses will be
called `zero` and `negate` (respectively); multiplicative identity and
inverses will be called `one` and `reciprocal` (respectively).

Additionally some structures support a quotient operation, called
`quot`.

All ring-like structures are associative for both `+` and `*`, and
have a `zero` element (an identity for `+`).

|Name                |Has `negate`?|Commutative `+`?|Has `1`?|Has `reciprocal`?|Has `quot`?|Commutative `*`?|
|--------------------|-------------|----------------|--------|-----------------|-----------|----------------|
|Semiring            |             |                |        |                 |           |                |
|Rig                 |             |                |       ✓|                 |           |                |
|CommutativeRig      |             |                |       ✓|                 |           |               ✓|
|Rng                 |            ✓|               ✓|        |                 |           |                |
|Ring                |            ✓|               ✓|       ✓|                 |           |                |
|CommutativeRing     |            ✓|               ✓|       ✓|                 |           |               ✓|
|EuclideanRing       |            ✓|               ✓|       ✓|                 |          ✓|               ✓|
|Field               |            ✓|               ✓|       ✓|                ✓|          ✓|               ✓|
|--------------------|-------------|----------------|--------|-----------------|-----------|----------------|

With the exception of `CommutativeRig` and `Rng`, every lower
structure is also an instance of the structures above it. For example,
every `Ring` is a `Rig`, every `Field` is a `CommutativeRing`, and so
on.

(For a description of what the terminology in each column means, see
*§algebraic properties and terminology*.)

### algebraic properties and terminology

|Name         |Description                                                                     |
|-------------|--------------------------------------------------------------------------------|
|Associative  | If `⊕` is associative, then `a ⊕ (b ⊕ c)` = `(a ⊕ b) ⊕ c)`.                    |
|Commutative  | If `f` is commutative, then `a ⊕ b` = `b ⊕ a`.                                 |
|Identity     | If `id` is an identity for `⊕`, then `a ⊕ id` = `id ⊕ a` = `a`.                |
|Inverse      | If `¬` is an inverse for `⊕` and `id`, then `a ⊕ ¬a` = `¬a ⊕ a` = `id`.        |
|Distributive | If `⊕` and `⊗` distribute, then `a ⊗ (b ⊕ c)` = `(a ⊗ b) ⊕ (a ⊗ c)` and `a ⊕ (b ⊗ c)` = `(a ⊕ b) ⊗ (a ⊕ c)`. |
|Idempotent   | If `⊕` is idempotent, then `a ⊕ (a ⊕ b)` = `(a ⊕ b) ⊕ a` = `(a ⊕ b)` and `b ⊕ (a ⊕ b)` = `(a ⊕ b) ⊕ b` = `(a ⊕ b)`. |
|-------------|--------------------------------------------------------------------------------|

(Though these properties are illustrated with infix operators, they
work equally-well with functions. When you see `x ⊕ y` that is
equivalent to `f(x, y)`: `⊕` is an infix representation of the binary
function `f`. Similarly, when you see `¬x` that is equivalent to
`g(x)`: `¬` is a prefix representation of the unary function `g`).
