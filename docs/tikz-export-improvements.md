# TikZ Export Improvements

This document outlines the changes made to GeoGebra's TikZ/PGF export functionality to produce cleaner, more human-readable code.

## Overview

The TikZ export has been significantly improved to generate code that is easier to read, edit, and understand. The changes focus on readability, proper formatting, and following TikZ best practices.

## Changes Made

### 1. Coordinate Rounding (3 Decimal Places)

**Before:**
```tikz
\coordinate (A) at (-3.5555555555555562,3.9752319599996278);
```

**After:**
```tikz
\coordinate (A) at (-3.556,3.975);
```

The `format()` method in `GeoGebraExport.java` now rounds all coordinates to 3 decimal places and removes trailing zeros.

### 2. Named Coordinates

Points are now registered with named coordinates at the beginning of the output, and referenced by name throughout:

```tikz
% Coordinates
\coordinate (A) at (-3.556,3.975);
\coordinate (B) at (5.333,2.981);

% Lines and segments
\draw (A) -- (B);
```

### 3. Section Comments

The output is now organized with section comments for better readability:

- `% Coordinates` - Named coordinate definitions
- `% Circles and curves` - Circle and curve drawings
- `% Lines and segments` - Line and segment drawings
- `% Points and labels` - Point markers and labels

### 4. Purple Points (Auxiliary/Construction Points)

Points with color RGB(127, 0, 255) are treated as auxiliary construction points and rendered as labels only (no filled circle):

```tikz
\draw (A) node [above] {$A$};
```

This check now applies to ALL point styles, not just the default style.

### 5. Unnamed Intersection Points Skipped

Points with empty or null labels (typically unnamed intersection points from commands like `Intersect(curve, curve)`) are now skipped entirely to avoid duplicate drawings.

### 6. Gray Color Simplification

Gray-ish colors (where R, G, B values are within 20 of each other and average brightness is 80-180) now use the built-in `gray` color instead of defining custom colors:

**Before:**
```tikz
\definecolor{wewdxt}{rgb}{0.431,0.427,0.451}
\draw [fill=wewdxt] (I) circle (2pt);
```

**After:**
```tikz
\draw [fill=gray] (I) circle (2pt);
```

### 7. Point Size Formatting

Point sizes are now formatted without unnecessary decimals:

**Before:** `circle (2.0pt)`
**After:** `circle (2pt)`

### 8. Removed Clipping Rectangle

The `\clip` command that was automatically added has been removed as it's typically not needed.

### 9. Removed `\begin{scriptsize}` Wrapper

The `\begin{scriptsize}...\end{scriptsize}` wrapper around points has been removed.

### 10. Right Angle Markers

Right angles now draw only the two lines forming the corner marker, not a full square:

**Before:**
```tikz
\draw (-3.489,3.539) -- (-3.069,3.478) -- (-3.009,3.898) -- (-3.429,3.959) -- cycle;
```

**After:**
```tikz
% Right angle α
\draw (-3.489,3.539) -- (-3.069,3.478) -- (-3.009,3.898);
```

### 11. Angle Labels in Comments

Angles now include a comment with their label:

```tikz
% Right angle α
\draw ...

% Angle β
\draw ...
```

### 12. Cleaner Arc/Semicircle Syntax

Simple circular arcs now use the cleaner TikZ arc syntax:

**Before:**
```tikz
\draw [shift={(0,0)}] plot[domain=0:3.142,variable=\t]({1*3*cos(\t r)+0*3*sin(\t r)},{0*3*cos(\t r)+1*3*sin(\t r)});
```

**After:**
```tikz
\draw [shift={(0,0)}] (0:3) arc (0:180:3);
```

The parametric form is still used for elliptical or rotated arcs.

### 13. Proper Spacing

Fixed missing space after `\draw` for circles and ellipses:

**Before:** `\draw(-4,0) circle (4cm);`
**After:** `\draw (-4,0) circle (4cm);`

## Files Modified

- `source/shared/common/src/main/java/org/geogebra/common/export/pstricks/GeoGebraExport.java`
  - Modified `format()` method for 3dp rounding

- `source/shared/common/src/main/java/org/geogebra/common/export/pstricks/GeoGebraToPgf.java`
  - Named coordinates system
  - Section comments
  - Purple point handling
  - Unnamed point skipping
  - Gray color detection
  - Point size formatting
  - Removed clip and scriptsize
  - Right angle improvements
  - Arc syntax improvements
  - Spacing fixes

## Example Output

```tikz
\documentclass[10pt]{article}
\usepackage{tikz}
\begin{document}
\begin{tikzpicture}[line cap=round,line join=round,>=triangle 45,x=1cm,y=1cm]
% Coordinates
\coordinate (A) at (-3.556,3.975);
\coordinate (B) at (5.333,2.981);
\coordinate (C) at (-3.556,-3.975);

% Circles and curves
\draw (-4,0) circle (4cm);
\draw (5,0) circle (3cm);

% Lines and segments
\draw (A) -- (B);
\draw (C) -- (B);

% Points and labels
\draw (A) node [above] {$A$};
\draw (B) node [above right] {$B$};
\draw [fill=gray] (I) circle (2pt) node [above right] {$I$};
\end{tikzpicture}
\end{document}
```
