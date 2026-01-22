# JvmSpec

The idea here is to identify static invocations that should be inverted.

## Reports

- There are 3 report types to suit 3 different needs.
- All quality reports need to be archived by the build system.

### Quality Metrics

- For maintaining a count of quality violations
- quality-metrics.json
- a single map containing the whole numbers representing the quality metrics this tool detects
- the closer this number is to 0, the better
- this tool only has one quality metric, staticInvocationsThatShouldBeInverted
- the quality metric names need to be unique across all tools so that they can be composed
- each tool should use the file name quality-metrics.json to allow for composition

### Details for a particular quality metric

- For tracking individual instances of quality metric violations
- this tool only has one
    - quality-metrics-staticInvocationsThatShouldBeInverted.json
- a list of the individual quality metric violations in a predictable sort order
- this is used for tracking when individual problems were created and when they were resolved
- you can use a text diffing tool to see what changed

### Browsable Report

- For exploring the state of quality violations in the code base
- Useful for identifying what to fix next
- Contains in-depth details useful for understanding particular instances of quality violations