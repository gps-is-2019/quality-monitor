[![Build Status](https://travis-ci.com/gps-is-2019/amigo.svg?branch=develop)](https://travis-ci.com/gps-is-2019/amigo)

# Amigo

Platform for monitoring and controlling Quality Assurance of a University.

This webapp is the outcome of a the joint project of **Software Engineering** (CS Bachelor's Degree) and **Software Project Management** (CS Master Degree) courses at University of Salerno.

The webapp is up and running on...(TBA)

## Clone and run the project in localhost

Follow these step:

1. Clone this repo: `git clone https://github.com/gps-is-2019/amigo.git` 
2. Go into amigo directory: `cd amigo`
3. ...

## Working with the project

- Master branch contains release-ready only features.  
- Develop branch contains multiple stable features.

### Add a feature

If you want to add another feature:

1. `git checkout origin/develop`
2. `git checkout -b feature/nameOfYourFeature`
3. Code!
4. `git add -A`
5. `git commit -m "Commit message that is good"`
6. `git push` (going to `origin/feature/nameOfYourFeature` as default)
7. Open a PR in GitHub to merge `origin/feature/nameOfYourFeature` to `origin/develop`
8. If you feature is complete, notify that the branch can be safely deleted. Otherwise, go back to 3.

### Get new data from my branch

If you and another *amigo* are working on the same feature branch but you want to get the changes he pushed yesterday:

1. Make sure you are in YOUR (already existing) feature branch. If not: `git checkout feature/nameOfYourFeature`
2. `git pull`
3. Resolve possible conflicts. Ask if you are in trouble. A common solution is:
  - `git stash` to save your local changes in a local secure stack
  - `git pull`
  - `git stash pop` to reapply your local changes again

### Get new data from another branch

If you are working with your feature branch but you want to get something new from *develop* branch:

1. Make sure you are in YOUR (already existing) feature branch. If not: `git checkout feature/nameOfYourFeature`
2. `git merge origin/develop`
3. Resolve possible conflicts. Ask if you are in trouble.

### How to write a good commit message

Follow this: `https://chris.beams.io/posts/git-commit/`, especially points 2, 3, 4, 5.

### How to run checkstyle

Checkstyle is enabled as a Maven plugin, so in order to launch it there are two ways:

- Run directly its Maven goal: `mvn checkstyle:check`
- Run during the *validate* lifecycle: `mvn validate`. Remember that validate is executed BEFORE `mvn compile`, so the check is done before compiling the source code with *javac*; however, violations will not stop the build