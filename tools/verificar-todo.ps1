# Recorre todos los tags v3-capNN: para cada uno, compila el
# reactor Maven completo de proyecto/ (mvn clean package) y,
# si algun modulo ya tiene tests, ejecuta tambien mvn test.
# Al ser multi-modulo, cada tag solo trae los modulos creados
# hasta ese capitulo -- el reactor construye los que existan.
#
# Requiere: git, Maven 3.9+ y Java 21 en el PATH.
# Uso: powershell -ExecutionPolicy Bypass -File tools\verificar-todo.ps1

Set-Location (Join-Path $PSScriptRoot "..")
$rama = git branch --show-current
$fallos = 0

$tags = git tag -l "v3-cap*" | Sort-Object

foreach ($tag in $tags) {
    git checkout -q $tag
    if ($LASTEXITCODE -ne 0) {
        Write-Host "$tag`: no se puede hacer checkout"
        $fallos++
        continue
    }

    if (-not (Test-Path "proyecto\pom.xml")) {
        Write-Host "$tag`: sin codigo todavia (revision de texto) -- OK"
        continue
    }

    Push-Location proyecto
    mvn -q -B clean package -DskipTests *> "$env:TEMP\verificar-vol3-build.log"
    $rcBuild = $LASTEXITCODE
    Pop-Location

    if ($rcBuild -ne 0) {
        Write-Host "$tag`: FALLO de compilacion (ver $env:TEMP\verificar-vol3-build.log)"
        $fallos++
        continue
    }

    $hayTests = Get-ChildItem -Path proyecto -Recurse -Filter "*Test.java" `
        -ErrorAction SilentlyContinue | Select-Object -First 1

    if ($hayTests) {
        Push-Location proyecto
        mvn -q -B test *> "$env:TEMP\verificar-vol3-test.log"
        $rcTest = $LASTEXITCODE
        Pop-Location

        if ($rcTest -ne 0) {
            Write-Host "$tag`: compila, pero FALLAN los tests (ver $env:TEMP\verificar-vol3-test.log)"
            $fallos++
            continue
        }
        Write-Host "$tag`: OK (compila y tests en verde)"
    } else {
        Write-Host "$tag`: OK (compila)"
    }

    git checkout -q -- .
    git clean -fdq proyecto
}

git checkout -q $rama
Write-Host "Fallos: $fallos"
exit $fallos
