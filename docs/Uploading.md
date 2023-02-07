More on mkdocs
========

How to upload the docs into the gh-pages branch

The Script
-----------

The mkdocs provides a handy build and deploy function.  The
trick is that the files are all contained in the <b>"gh-pages"</b>
branch.   If you simply do the command in Idea or Android Studio,
the tool will want to check all these normally "invisible" files
into Git in the master branch.

The working solution, after looking at OkHttp for example, seems to
be to clone the repo into a temp folder and do all the building there.


