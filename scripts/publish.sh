#!/bin/bash
set -e

git config --global user.email "bot@typelevel.org"
git config --global user.name "Typelevel Bot"
git config --global push.default simple

./sbt ++$TRAVIS_SCALA_VERSION docs/publishMicrosite
