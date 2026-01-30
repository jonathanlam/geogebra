# Compiling GeoGebra Desktop

## Prerequisites

- **Java 25** (required for building and running)
  ```powershell
  winget install EclipseAdoptium.Temurin.25.JDK
  ```
  Restart your terminal after installation. Verify with:
  ```powershell
  java --version
  ```

## Running from Source

To run GeoGebra directly without creating a distributable:

```powershell
.\gradlew.bat -p source\desktop :desktop:run
```

## Creating Distributable Packages

### Option 1: Distribution ZIP (requires Java on target machine)

Creates a ZIP with launch scripts and all JARs:

```powershell
.\gradlew.bat -p source\desktop :desktop:distZip
```

Output: `source\desktop\desktop\build\distributions\desktop.zip`

### Option 2: Standalone App Folder (no Java required on target)

Creates a self-contained folder with bundled JRE:

```powershell
# First, build and collect all dependencies
.\gradlew.bat -p source\desktop :desktop:installDist

# Then package with jpackage
jpackage --input source\desktop\desktop\build\install\desktop\lib --main-jar desktop.jar --main-class org.geogebra.desktop.GeoGebra3D --name GeoGebra --type app-image
```

Output: `GeoGebra\` folder in current directory containing:
- `GeoGebra.exe` - the launcher
- Bundled Java runtime
- All dependencies

You can zip this folder and share it. Recipients just run `GeoGebra.exe`.

### Option 3: Windows Installer (no Java required on target)

First install WiX Toolset:
```powershell
winget install WiXToolset.WiXToolset
```

Restart your terminal, then:
```powershell
.\gradlew.bat -p source\desktop :desktop:installDist

# For .exe installer:
jpackage --input source\desktop\desktop\build\install\desktop\lib --main-jar desktop.jar --main-class org.geogebra.desktop.GeoGebra3D --name GeoGebra --type exe --win-shortcut --win-menu

# For .msi installer:
jpackage --input source\desktop\desktop\build\install\desktop\lib --main-jar desktop.jar --main-class org.geogebra.desktop.GeoGebra3D --name GeoGebra --type msi --win-shortcut --win-menu
```

## Web Version

To run the web version locally:

```powershell
.\gradlew.bat -p source\web :web:run
```

Then open http://localhost:8888 in your browser.
