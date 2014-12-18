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
