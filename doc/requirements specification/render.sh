#!/bin/bash

file="requirements specification"

# fail the script if any command fails
set -e 

# Copy everything to a new folder that we can savely delete
tmpdir=`mktemp -d`
cp -r * "$tmpdir"
cd "$tmpdir"

# generate .tex
lyx -e pdflatex "$file.lyx"
# generate all the other stuff
pdflatex "$file.tex"
# make the glossary
makeglossaries "$file"
# render the pdf (we need at least two times. And you know… just to be sure ;))
pdflatex "$file.tex"
pdflatex "$file.tex"
pdflatex "$file.tex"
pdflatex "$file.tex"

# Go back up
cd "$OLDPWD"

# copy the rendered pdf to save it
cp "$tmpdir/$file.pdf" .
# Delete tmp-render as it contains tons of files we don’t want.
rm -rf "$tmpdir"
