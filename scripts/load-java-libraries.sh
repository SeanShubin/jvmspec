#!/usr/bin/env bash

jmod_glob="$HOME/.asdf/installs/java/corretto-21.0.7.6.1/jmods/*.jmod"

echo $jmod_glob

for filepath in $jmod_glob; do
    filename=$(basename "$filepath")
    echo "extracting into $filename from $filepath"
    jmod extract --dir "generated/jmods/$filename" "$filepath"
done
