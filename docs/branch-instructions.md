Conditional Branch Instructions

- ifeq - branch if int comparison with zero succeeds (==)
- ifne - branch if int comparison with zero succeeds (!=)
- iflt - branch if int comparison with zero succeeds (<)
- ifge - branch if int comparison with zero succeeds (>=)
- ifgt - branch if int comparison with zero succeeds (>)
- ifle - branch if int comparison with zero succeeds (<=)

Two-Value Conditional Branches

- if_icmpeq - branch if int comparison succeeds (==)
- if_icmpne - branch if int comparison succeeds (!=)
- if_icmplt - branch if int comparison succeeds (<)
- if_icmpge - branch if int comparison succeeds (>=)
- if_icmpgt - branch if int comparison succeeds (>)
- if_icmple - branch if int comparison succeeds (<=)
- if_acmpeq - branch if reference comparison succeeds (==)
- if_acmpne - branch if reference comparison succeeds (!=)

Reference Conditional Branches

- ifnull - branch if reference is null
- ifnonnull - branch if reference not null

Switch Instructions

- tableswitch - access jump table by index and jump
- lookupswitch - access jump table by key match and jump

Unconditional Branches

- goto - branch unconditionally
- goto_w - branch unconditionally (wide index)

Subroutine Instructions (deprecated but still in spec)

- jsr - jump subroutine
- jsr_w - jump subroutine (wide index)
- ret - return from subroutine

Exception Handling (implicit branching)

While not opcodes per se, exception table entries in the class file also represent branching paths that affect
cyclomatic
complexity.

For cyclomatic complexity calculation, each conditional branch instruction adds +1 to the complexity, and
tableswitch/lookupswitch add +1 for each case target (not including the default).
