#!/usr/bin/env bash
# Recorre todos los tags v3-capNN: para cada uno, compila el
# reactor Maven completo de `proyecto/` (`mvn clean package`) y,
# desde que existen tests en algún módulo, los ejecuta también
# (`mvn test`). Al ser un proyecto multi-módulo, cada tag solo
# contiene los módulos creados hasta ese capítulo -- el reactor
# construye los que existan.
#
# Requiere: git, Maven 3.9+ y Java 21 en el PATH. Si tu red
# bloquea Maven Central, este script fallará -- es exactamente
# lo que hay que comprobar antes de dar el capítulo por bueno.
set -uo pipefail
cd "$(dirname "$0")/.."
rama=$(git branch --show-current)
fallos=0

for tag in $(git tag -l 'v3-cap*' | sort); do
  numero=${tag#v3-cap}
  git checkout -q "$tag" || {
    echo "$tag: no se puede hacer checkout"
    fallos=$((fallos + 1))
    continue
  }

  if [ ! -f proyecto/pom.xml ]; then
    echo "$tag: sin código todavía (revisión de texto) -- OK"
    continue
  fi

  ( cd proyecto && mvn -q -B clean package -DskipTests ) \
    > /tmp/verificar-vol3-build.log 2>&1
  rc_build=$?

  if [ $rc_build -ne 0 ]; then
    echo "$tag: FALLO de compilacion (ver /tmp/verificar-vol3-build.log)"
    fallos=$((fallos + 1))
    continue
  fi

  if grep -rq '<artifactId>[^<]*-test' proyecto --include=pom.xml \
     2>/dev/null || grep -rlq 'import org.junit' proyecto/*/src/test \
     2>/dev/null; then
    ( cd proyecto && mvn -q -B test ) \
      > /tmp/verificar-vol3-test.log 2>&1
    rc_test=$?
    if [ $rc_test -ne 0 ]; then
      echo "$tag: compila, pero FALLAN los tests (ver /tmp/verificar-vol3-test.log)"
      fallos=$((fallos + 1))
      continue
    fi
    echo "$tag: OK (compila y tests en verde)"
  else
    echo "$tag: OK (compila)"
  fi

  git checkout -q -- . 2>/dev/null
  git clean -fdq proyecto 2>/dev/null
done

git checkout -q "$rama"
echo "Fallos: $fallos"
exit $fallos
