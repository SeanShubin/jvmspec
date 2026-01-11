# JvmSpec

    Library to parse JVM class files

## Analysis

Determine if dependency inversion has been applied properly.
Look for static invocations.
If the static invocations are delegating to pure logic, they don't need to be inverted.
If the static invocations are accessing anything slow or environmental, they should be inverted.
For the static invocations that should be inverted, check to see if they actually are inverted.
If there is no logic mixed in with the class containing the static invocations, we take that as strong evidence that the
dependency must have been properly inverted because the logic must be somewhere else.
We determine "no logic" examining the JVM bytecode and comparing each method call to opcode patterns previously
determined to represent simple delegation rather than logic.

## Filter Reports

- filter event properties
    - category (groups related types)
    - type     (text is grouped by type)
    - pattern  (associates text with type)
    - text     (the input)
- the report for each category is in a separate file
- reports are broken down as follows
    - by text
        - text (sorted ascending)
            - quantity, type, pattern (sorted by quantity descending, type ascending, pattern ascending)
    - by pattern
        - type, pattern (sorted by type ascending, then pattern ascending)
            - quantity, text (sorted by quantity descending, text ascending)
    - no matches
        - text (sorted ascending)

