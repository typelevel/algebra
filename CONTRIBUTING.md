---
layout: page
title:  "Contributing"
section: "contributing"
position: 2
---

# Contributing to Algebra

This document is a guide to how to get started contributing to Algebra.

## Contributing Documentation

Often the biggest issue facing open-source projects is a lack of good documentation, and Algebra is no exception here. If you have ideas for specific pieces of documentation which are absent, feel free to open a specific issue for that.

The documentation for Algebra's website is stored in the `docs/src/main/tut` directory of the [docs subproject](https://github.com/typelevel/algebra/tree/master/docs).

Algebra's documentation is powered by [sbt-microsites](https://47deg.github.io/sbt-microsites/) and [tut](https://github.com/tpolecat/tut). `tut` compiles any code that appears in the documentation, ensuring that snippets and examples won't go out of date.

We also gladly accept patches for documentation. Each page has a link that will allow you to edit and submit a PR for a documentation change, right from the Github UI. Anything from fixing a typo to writing a full tutorial is a great way to help the project.

### Generating the Site

run `sbt docs/makeMicrosite` to generate a local copy of the microsite.

### Previewing the site

1. Install jekyll locally, depending on your platform, you might do this with any of the following commands:

```
yum install jekyll
apt-get install jekyll
gem install jekyll
```

2. In a shell, navigate to the generated site directory in `docs/target/site`
3. Start jekyll with `jekyll serve --incremental`
4. Navigate to http://127.0.0.1:4000/algebird/ in your browser
5. Make changes to your site, and run `sbt docs/makeMicrosite` to regenerate the site. The changes should be reflected as soon as `sbt docs/makeMicrosite` completes.

## Post-release

After the release occurs, you will need to update the documentation. Here is a list of the places that will definitely need to be updated:

 * `README.md`: update version numbers
 * `CHANGES.md`: summarize changes since last release

(Other changes may be necessary, especially for large releases.)

You can get a list of changes between release tags `v0.6.0` and `v0.6.1` via `git log v0.6.0..v0.6.1`. Scanning this list of commit messages is a good way to get a summary of what happened, although it does not account for conversations that occured on Github. (You can see the same view on the Github UI by navigating to <https://github.com/twitter/algebird/compare/v0.6.0...v0.6.1>.)

Once the relevant documentation changes have been committed, you should add new [release notes](https://github.com/typelevel/algebra/releases). You can add a release by clicking the "Draft a new release" button on that page, or if the relevant release already exists, you can click "Edit release".

Finally, update the website via `sbt docs/publishMicrosite`.

## Reporting bugs, issues, or unexpected behavior

If you encounter anything that is broken, confusing, or could be better, you should [open an issue](https://github.com/typelevel/algebra/issues). You don't have to know *why* the error is occuring, or even that an error happens at all.

If you are trying to do something with Algebra, and are having a hard time, it could be any of the following issues:

 * an actual bug or error
 * an omission or problem with the API
 * a confusing edge case
 * a documentation problem

Feel free to open a bug before you're sure which of these is happening.  You can also ask questions on the [Gitter channel](https://gitter.im/non/algebra?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) to get other people's opinions.

## Creating or improving tests

Algebra uses [ScalaTest](www.scalatest.org) and [ScalaCheck](http://scalacheck.org/) to test our code. The tests fulfill a number of important functions:

 * ensure our algorithms return correct results
 * check the visibility of our type class instances
 * confirm that the API works as we expect
 * test edge cases which might otherwise be missed

If you find a bug you are also encouraged to submit a test case (the code you tried that failed). Adding these failing cases to Algebra's tests provides a good way to ensure the bug is really fixed, and is also a good opportunity to start contributing.

ALso, when you notice places that lack tests (or where the tests are sparse, incomplete, or just ugly) feel free to submit a pull request with improvements!

### What need testing the most?

The best way to figure out where we need tests is to look at our [Codecov coverage report](https://codecov.io/github/typelevel/algebra). Codecov has an incredible [browser extension](http://docs.codecov.io/docs/browser-extension) that will embed coverage information right into the GitHub file browsing UI.

Once you've got the extension installed, navigate to the any of the folders in the [algebra package index](https://github.com/typelevel/algebra/tree/master/core/src/main/scala/algebra) to see file-by-file coverage percentages. Inside each file, lines that aren't covered by tests will be highlighted red.

## Submitting patches or code

Algebra is on Github to make it easy to fork the code and change it.  There are very few requirements but here are some suggestions for what makes a good pull request.

Please make pull requests against the `master` branch. If you're writing a small amount of code to fix a bug, feel free to just open a pull request immediately. You can even attach some code snippets to the issue if that's easier.

For adding new code to Algebra, it's often smart to create a topic branch that can be used to collaborate on the design. Features that require a lot of code, or which represent a big change to Algebra, tend not to get merged to master as quickly. For this kind of work, you should submit a pull request from your branch, but we will probably leave the PR open for awhile while commenting on it.

You can always email the list, or visit the [Gitter channel](https://gitter.im/non/algebra?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) to get a second opinion on your idea or design.

## Ask questions and make suggestions

Algebra strives to be an excellent part of the Scala ecosystem. We welcome your questions about how Algebra works now, and your ideas for how to make it even better!
