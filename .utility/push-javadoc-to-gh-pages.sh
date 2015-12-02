#!/bin/bash

if [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing javadoc...\n"

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet https://${JIVE_OS_TOKEN}@github.com/jivesoftware/mlogger.wiki.git > /dev/null

  cd mlogger.wiki
  git rm -rf ./apidocs
  cp -Rf $HOME/apidocs ./apidocs
  git add -f .
  git commit -m "Lastest apidocs on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to wiki"
  git push -fq origin master > /dev/null

  echo -e "Published apidocs to wiki.\n"
  
fi
