# Adding New Commands to GeoGebra

This guide explains how to add a new command to GeoGebra, using `Excenter` as an example.

## Overview

Adding a new command requires modifications to 5 files:

1. **Commands.java** - Register the command enum
2. **AlgoXxx.java** - Create the algorithm (computation logic)
3. **CmdXxx.java** - Create the command processor (argument parsing)
4. **DiscreteCommandProcessorFactory.java** - Register the command processor
5. **CommandDispatcher.java** - Route the command to the correct factory
6. **command.properties** - Add syntax for autocomplete

## File Locations

```
source/shared/common/src/main/java/org/geogebra/common/kernel/
├── commands/
│   ├── Commands.java                         # Command enum
│   ├── CommandDispatcher.java                # Command routing
│   └── DiscreteCommandProcessorFactory.java  # Factory registration
└── barycentric/                              # Package for geometry algorithms
    ├── AlgoExcenter.java                     # Algorithm
    └── CmdExcenter.java                      # Command processor

source/shared/common-jre/src/main/resources/org/geogebra/common/jre/properties/
└── command.properties                        # Autocomplete syntax
```

## Step-by-Step Guide

### 1. Add Command to `Commands.java`

Location: `source/shared/common/src/main/java/org/geogebra/common/kernel/commands/Commands.java`

Add the new command enum value in the appropriate section (geometry, algebra, etc.):

```java
Incenter(TABLE_GEOMETRY),

Excenter(TABLE_GEOMETRY),  // <-- Add new command

Circumcenter(TABLE_GEOMETRY),
```

The `TABLE_*` constant determines which category the command appears in for help/documentation.

### 2. Create the Algorithm Class (`AlgoXxx.java`)

Location: Choose an appropriate package under `kernel/` (e.g., `barycentric/` for triangle-related commands)

The algorithm class contains the mathematical computation. Key elements:

```java
public class AlgoExcenter extends AlgoElement {

    // Input fields
    private GeoPointND A, B, C;
    private GeoNumberValue index;

    // Output field
    private GeoPointND excenter;

    // Constructor: initialize inputs, create output, set dependencies
    public AlgoExcenter(Construction cons, String label, GeoPointND A,
            GeoPointND B, GeoPointND C, GeoNumberValue index) {
        super(cons);
        this.A = A;
        this.B = B;
        this.C = C;
        this.index = index;

        // Create output object
        excenter = kernel.getGeoFactory().newPoint(dim, cons);

        setInputOutput();
        compute();
        excenter.setLabel(label);
    }

    @Override
    public Commands getClassName() {
        return Commands.Excenter;  // Return the command enum
    }

    @Override
    protected void setInputOutput() {
        input = new GeoElement[4];
        input[0] = A.toGeoElement();
        input[1] = B.toGeoElement();
        input[2] = C.toGeoElement();
        input[3] = index.toGeoElement();

        setOnlyOutput(excenter);
        setDependencies();
    }

    public GeoPointND getResult() {
        return excenter;
    }

    @Override
    public final void compute() {
        // Mathematical computation here
        // Set excenter.setUndefined() for invalid inputs
    }
}
```

### 3. Create the Command Processor (`CmdXxx.java`)

Location: Same package as the algorithm

The command processor parses arguments and creates the algorithm:

```java
public class CmdExcenter extends CommandProcessor {

    public CmdExcenter(Kernel kernel) {
        super(kernel);
    }

    @Override
    final public GeoElement[] process(Command c, EvalInfo info) throws MyError {
        int n = c.getArgumentNumber();
        boolean[] ok = new boolean[n];
        GeoElement[] arg;

        switch (n) {
        case 4:  // Expected number of arguments
            arg = resArgs(c, info);

            // Validate argument types
            if ((ok[0] = arg[0].isGeoPoint())
                    && (ok[1] = arg[1].isGeoPoint())
                    && (ok[2] = arg[2].isGeoPoint())
                    && (ok[3] = arg[3] instanceof GeoNumberValue)) {

                // Create algorithm
                AlgoExcenter algo = new AlgoExcenter(cons, c.getLabel(),
                        (GeoPointND) arg[0], (GeoPointND) arg[1],
                        (GeoPointND) arg[2], (GeoNumberValue) arg[3]);

                GeoElement[] ret = { algo.getResult().toGeoElement() };
                return ret;
            }
            throw argErr(c, getBadArg(ok, arg));

        default:
            throw argNumErr(c);
        }
    }
}
```

### 4. Register in `DiscreteCommandProcessorFactory.java`

Location: `source/shared/common/src/main/java/org/geogebra/common/kernel/commands/DiscreteCommandProcessorFactory.java`

Add the import and switch case:

```java
import org.geogebra.common.kernel.barycentric.CmdExcenter;

// In getProcessor() method:
case Excenter:
    return new CmdExcenter(kernel);
```

### 5. Add Routing in `CommandDispatcher.java`

Location: `source/shared/common/src/main/java/org/geogebra/common/kernel/commands/CommandDispatcher.java`

Find the switch block that routes to your factory and add the case:

```java
case TriangleCenter:
case Incenter:
case Excenter:  // <-- Add here
case Circumcenter:
// ...
    return getDiscreteCommandProcessorFactory().getProcessor(command, kernel);
```

**Important**: Commands are grouped by which factory handles them. Add your command to the correct group based on the factory you registered it in.

### 6. Add Autocomplete Syntax in `command.properties`

Location: `source/shared/common-jre/src/main/resources/org/geogebra/common/jre/properties/command.properties`

Add two lines for the command name and syntax:

```properties
Excenter=Excenter
Excenter.Syntax=[ <Point>, <Point>, <Point>, <Number> ]
```

For commands with multiple syntax variants, use `\n` to separate:

```properties
SomeCommand.Syntax=[ <Point>, <Point> ]\n[ <Line>, <Line> ]
```

## Command Factory Types

Different command types are handled by different factories:

| Factory | Command Types |
|---------|---------------|
| `DiscreteCommandProcessorFactory` | Triangle centers, Voronoi, convex hull, graph algorithms |
| `BasicCommandProcessorFactory` | Basic geometry, lists, text, transformations |
| `AdvancedCommandProcessorFactory` | Calculus, matrices, optimization |
| `StatsCommandProcessorFactory` | Statistics, probability distributions |
| `CASCommandProcessorFactory` | Computer algebra system commands |
| `ScriptingCommandProcessorFactory` | UI scripting, turtle graphics |
| `ProverCommandProcessorFactory` | Geometric theorem proving |

## Common Patterns

### Commands with Variable Arguments

Use multiple cases in the switch statement:

```java
switch (n) {
case 2:
    // Handle 2-argument version
case 3:
    // Handle 3-argument version
default:
    throw argNumErr(c);
}
```

### Commands Returning Multiple Objects

Return an array with multiple elements:

```java
GeoElement[] ret = { algo.getResult1(), algo.getResult2() };
return ret;
```

### Handling Undefined Results

In the algorithm's `compute()` method:

```java
if (invalidCondition) {
    result.setUndefined();
    return;
}
```

## Testing

1. Build the project
2. Run GeoGebra desktop
3. Create necessary input objects (e.g., points A, B, C)
4. Test the command: `Excenter[A, B, C, 1]`
5. Verify autocomplete works by typing the command name

## Checklist

- [ ] Added enum to `Commands.java`
- [ ] Created `AlgoXxx.java` with computation logic
- [ ] Created `CmdXxx.java` with argument parsing
- [ ] Registered in appropriate `*CommandProcessorFactory.java`
- [ ] Added case in `CommandDispatcher.java` switch statement
- [ ] Added syntax to `command.properties`
- [ ] Tested command execution
- [ ] Tested autocomplete
