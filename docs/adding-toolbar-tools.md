# How GeoGebra Toolbar Tools Work

## Overview

This document summarizes how toolbar tools are added and function in GeoGebra, with the goal of understanding how to add custom tools activated via buttons rather than text commands.

## Key Finding: Commands vs Toolbar Tools

**Commands** (like `Excenter`, `Altitude`) and **Toolbar Tools** are **separate systems**:

| Aspect | Commands | Toolbar Tools |
|--------|----------|---------------|
| Triggered by | Typing in input bar | Clicking toolbar button + canvas interaction |
| Execution | Immediate, scripted | Interactive, multi-step |
| Definition | `Commands.java` enum | `EuclidianConstants.java` MODE_* constants |
| Handler | `CmdXxx.java` processors | `EuclidianController.java` switch cases |
| User interaction | Arguments in parentheses | Mouse clicks/drags on canvas |

## Architecture Summary

### 1. Tool Mode Constants
**File:** `source/shared/common/src/main/java/org/geogebra/common/euclidian/EuclidianConstants.java`

```java
public static final int MODE_MOVE = 0;
public static final int MODE_POINT = 1;
public static final int MODE_JOIN = 2;           // Line through Two Points
public static final int MODE_SEGMENT = 15;
public static final int MODE_CIRCLE_TWO_POINTS = 10;
// ... 100+ tool modes
public static final int MACRO_MODE_ID_OFFSET = 100001;  // Custom user tools
```

### 2. Toolbar Definition Strings
**File:** `source/shared/common/src/main/java/org/geogebra/common/gui/toolbar/ToolBar.java`

Toolbars are defined as space-separated mode numbers:
- Space (` `) = tools in same submenu
- Pipe (`|`) = new button/menu
- Double pipe (`||`) = separator before macro tools

Example: `"0 39 | 1 501 67 5 19 | 2 15 45 18"` means:
- Button 1: MODE_MOVE (0), with submenu containing 39
- Button 2: MODE_POINT (1), with submenu [501, 67, 5, 19]
- Button 3: MODE_JOIN (2), with submenu [15, 45, 18]

### 3. Mode Change Flow

```
Toolbar button click
    ↓
App.setMode(int mode, ModeSetter.TOOLBAR)
    ↓
EuclidianView.setMode(mode)
    ↓
EuclidianController.setMode(newMode)
    ↓
initNewMode(newMode) → stores mode, clears selections
    ↓
[User clicks on canvas]
    ↓
EuclidianController.wrapMousePressed()
    ↓
processMode(hits) → switchModeForProcessMode()
    ↓
switch(mode) { case MODE_POINT: point(hits); ... }
    ↓
AlgoDispatcher creates geometry objects
```

### 4. Tool Handler Implementation
**File:** `source/shared/common/src/main/java/org/geogebra/common/euclidian/EuclidianController.java`

The `switchModeForProcessMode()` method (~line 5022) contains a giant switch statement:

```java
switch (mode) {
case EuclidianConstants.MODE_POINT:
    point(hits, selectionPreview);
    break;
case EuclidianConstants.MODE_JOIN:
    ret = join(hits, selectionPreview);  // Collects 2 points, then creates line
    break;
case EuclidianConstants.MODE_SEGMENT:
    ret = segment(hits, selectionPreview);
    break;
// ... 100+ cases
}
```

## How to Add a New Toolbar Tool

### Required Steps:

1. **Add MODE constant** to `EuclidianConstants.java`:
   ```java
   public static final int MODE_ALTITUDE = 63;  // Pick unused number
   ```

2. **Add mode text mapping** in `EuclidianConstants.getModeText()`:
   ```java
   case MODE_ALTITUDE:
       return "Altitude.Tool";
   ```

3. **Add handler** in `EuclidianController.switchModeForProcessMode()`:
   ```java
   case EuclidianConstants.MODE_ALTITUDE:
       ret = altitude(hits, selectionPreview);
       break;
   ```

4. **Implement tool method** in `EuclidianController`:
   ```java
   protected GeoElement[] altitude(Hits hits, boolean selPreview) {
       // Collect point + line, then create altitude segment
   }
   ```

5. **Add to toolbar definition** in `ToolBar.java` default string

6. **Add translation keys** to `command.properties`:
   - `Altitude.Tool = Altitude`
   - `Altitude.Tool.Help = Altitude from point to line`

### Key Files to Modify:

| File | Change |
|------|--------|
| `EuclidianConstants.java` | Add MODE_* constant |
| `EuclidianConstants.java` | Add getModeText() case |
| `EuclidianController.java` | Add switchModeForProcessMode() case |
| `EuclidianController.java` | Implement tool handler method |
| `ToolBar.java` | Add to default toolbar string |
| `command.properties` | Add translation keys |

## Alternative: Macro Tools

Users can create custom tools without code changes using GeoGebra's macro system:
- Tools → Create New Tool
- Define input/output objects
- Automatically assigned MODE ID >= 100001
- Appears in toolbar under custom tools section
