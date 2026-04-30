# Supermarket Billing System - Packaging Script
# This script creates a self-contained Windows application (App Image)

Write-Host "--- Packaging Supermarket POS System ---" -ForegroundColor Cyan

# 1. Build the project and gather dependencies
Write-Host "[1/3] Building project and gathering dependencies..."
C:\apache-maven\bin\mvnd clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed! Please check the errors above." -ForegroundColor Red
    exit
}

# 2. Prepare the input directory for jpackage
Write-Host "[2/3] Preparing packaging directory..."
$inputDir = "target/packaging"
if (Test-Path $inputDir) { Remove-Item -Recurse -Force $inputDir }
New-Item -ItemType Directory -Path $inputDir | Out-Null

# Copy the main jar and all dependencies to the packaging folder
Copy-Item "target/supermarket-pos-system-1.0.0.jar" -Destination $inputDir
Copy-Item "target/libs/*.jar" -Destination $inputDir

# 3. Create the App Image (Executable + Bundled JRE)
Write-Host "[3/3] Creating executable app image..."
$outputDir = "dist"
if (Test-Path $outputDir) { Remove-Item -Recurse -Force $outputDir }

jpackage `
    --name "SupermarketPOS" `
    --description "Supermarket POS & Billing System" `
    --input $inputDir `
    --main-jar "supermarket-pos-system-1.0.0.jar" `
    --main-class com.supermarket.pos.Launcher `
    --type app-image `
    --win-console `
    --dest $outputDir

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nSUCCESS! Executable created in: $outputDir\SupermarketPOS" -ForegroundColor Green
    Write-Host "You can run it by launching: $outputDir\SupermarketPOS\SupermarketPOS.exe" -ForegroundColor Yellow
} else {
    Write-Host "`nPackaging failed." -ForegroundColor Red
}
