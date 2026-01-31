# TikZ Export - Claude Code Reference

This document is a technical reference for future Claude Code sessions working on GeoGebra's TikZ export functionality.

## Key Files

### Primary Files

1. **`source/shared/common/src/main/java/org/geogebra/common/export/pstricks/GeoGebraToPgf.java`**
   - Main TikZ export implementation
   - ~3400 lines
   - Handles all geometry object exports

2. **`source/shared/common/src/main/java/org/geogebra/common/export/pstricks/GeoGebraExport.java`**
   - Base class for exports
   - Contains `format()` method for number formatting
   - Contains `colorCode()` method

## Architecture

### StringBuilder Components

The export builds code in multiple StringBuilders that are assembled at the end:

```java
codePreamble      // LaTeX preamble
codeBeginDoc      // \begin{tikzpicture}...
codeCoordinates   // \coordinate definitions
codeFilledObject  // Circles, filled shapes, angles
code              // Lines, segments, rays
codePoint         // Points and labels
```

**Assembly order (via insert(0,...)):**
```
codePreamble + codeBeginDoc + codeCoordinates + codeFilledObject + code + codePoint + closing tags
```

### Key Methods in GeoGebraToPgf.java

| Method | Purpose |
|--------|---------|
| `generateAllCode()` | Main entry point, assembles all code |
| `drawGeoPoint()` | Draws points (line ~2011) |
| `drawGeoSegment()` | Draws segments |
| `drawCircle()` | Draws circles (line ~1803) |
| `drawAngle()` | Draws angles and right angles (line ~680) |
| `drawArc()` | Draws arcs/semicircles (line ~1303) |
| `colorCode()` | Generates color definitions (line ~3254) |
| `format()` | Number formatting (in GeoGebraExport.java, line ~143) |
| `writePoint()` | Writes (x,y) coordinates |
| `writePointOrName()` | Writes named coordinate or (x,y) |
| `registerPoint()` | Registers point for named coordinates |

## Current Implementation Details

### Named Coordinates System

```java
private HashMap<String, String> pointNames;  // coordKey -> label
private StringBuilder codeCoordinates;        // \coordinate definitions

private String coordKey(double x, double y) {
    return format(x) + "," + format(y);
}

private void registerPoint(String name, double x, double y) {
    // Registers point and adds \coordinate line
}

private void writePointOrName(double x, double y, StringBuilder sb) {
    // Looks up name, writes (Name) or (x,y)
}
```

### Purple Point Detection (Auxiliary Points)

```java
// In drawGeoPoint(), early return for purple points:
int red = dotcolor.getRed();
int green = dotcolor.getGreen();
int blue = dotcolor.getBlue();
if (red == 127 && green == 0 && blue == 255) {
    // Render as label only, no filled circle
    return;
}
```

### Gray Color Detection

```java
// In colorCode():
int maxDiff = Math.max(Math.abs(red - green),
        Math.max(Math.abs(green - blue), Math.abs(red - blue)));
int avg = (red + green + blue) / 3;
if (maxDiff <= 20 && avg >= 80 && avg <= 180) {
    sb.append("gray");
    return;
}
```

### Simple Arc Detection

```java
// In drawArc(), for CONIC_PART_ARC:
boolean isSimpleCircularArc = (m11 == 1 && m22 == 1 && m12 == 0 && m21 == 0
        && Math.abs(r1 - r2) < 0.001);
if (isSimpleCircularArc) {
    // Use: (startDeg:r) arc (startDeg:endDeg:r)
} else {
    // Use parametric plot for elliptical/rotated arcs
}
```

### Point Radius Formatting

```java
private String formatRadius(double r) {
    if (r == (int) r) {
        return String.valueOf((int) r);
    }
    return String.valueOf(r);
}
```

## Section Comments

Added in `generateAllCode()`:

```java
if (codeCoordinates.length() > 0) {
    codeCoordinates.insert(0, "% Coordinates\n");
}
if (codeFilledObject.length() > 0) {
    code.insert(0, "% Circles and curves\n");
}
if (code.length() > 0) {
    code.insert(0, "% Lines and segments\n");
}
if (codePoint.length() > 0) {
    code.append("% Points and labels\n");
}
```

## Angle Comments

```java
// In drawAngle(), for right angles:
String angleLabel = geo.getLabelSimple();
if (angleLabel != null && !angleLabel.isEmpty()) {
    codeFilledObject.append("% Right angle ").append(angleLabel).append("\n");
}

// For regular angles:
codeFilledObject.append("% Angle ").append(angleLabel).append("\n");
```

## GeoGebra Color Constants

| Color | RGB | Usage |
|-------|-----|-------|
| Purple | (127, 0, 255) | Auxiliary/construction points |
| Gray | ~(110, 109, 115) | Midpoints, other auxiliary |
| Black | (0, 0, 0) or (68, 68, 68) | Regular objects |

## Common Tasks

### Adding a New Section Comment
Add in `generateAllCode()` before the assembly section.

### Modifying Point Rendering
Edit `drawGeoPoint()` around line 2011.

### Changing Number Formatting
Edit `format()` in `GeoGebraExport.java` around line 143.

### Adding New Color Detection
Edit `colorCode()` around line 3254.

### Modifying Circle/Arc Output
- Circles: `drawCircle()` around line 1803
- Arcs: `drawArc()` around line 1303

## Testing

Run GeoGebra desktop and use File > Export > Graphics View as PGF/TikZ to test changes.

## Git History

Recent commits related to TikZ export:
- `dd5effbf58d` - TikZ export: skip unnamed points and handle purple points
- `3f0cb8cc2f7` - improve formatting
- Earlier commits for triangle centers and other features

## Potential Future Improvements

1. Combine connected segments into single `\draw` paths
2. Add TikZ styles for repeated formatting
3. Group related objects with comments
4. Simplify repeated transformations
5. Add option for coordinate precision
