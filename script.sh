#!/bin/sh
clear
echo "borrado de archivos .svn"
rm -rf `find . -type d -name .svn`
echo "====> ok"
