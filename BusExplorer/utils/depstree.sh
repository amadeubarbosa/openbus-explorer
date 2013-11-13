#!/bin/bash

declare -A tested
declare -A outersrc

fullpath=`readlink -f $0`
basedir=`dirname ${fullpath}`
basesrc=`readlink -f ${basedir}/../src` 
outersrc=(
  [logistic]='/common/src/logistic/trunk/src'
  [planref]='/common/src/planref/trunk/src'
)

deps() {
  local files=$@
  sed -n -e 's:\r::' -e 's:^import \(\(logistic\|planref\|reuse\).*\)\;:\1:p' ${files} | sort | uniq
}

resolvepath() {
   local package=${1//\./\/}
   case ${package} in
   logistic*)
     echo ${outersrc[logistic]}/${package}.java
     ;;
   planref*)
     echo ${outersrc[planref]}/${package}.java
     ;;
   reuse*)
     echo ${basesrc}/${package}.java
     ;;
   esac
}

depstree_iter() {
  local ind="$1"
  local p=$2
  local file=`resolvepath ${p}`
  tested[${p}]=true
  printf "%s\n" "${ind}"${p}
  for dep in `deps ${file}`; do
    if [[ ! ${tested[${dep}]} ]]; then
      depstree_iter "    ${ind}" ${dep}
    else
      printf "%s\n" "    ${ind}"${dep}\(...\)
    fi
  done
}

depstree() {
  local files=`find ${basesrc} -name \*.java`
  for p in `deps ${files}`; do
      depstree_iter "" ${p}
  done
}

depstree 2> /dev/null
