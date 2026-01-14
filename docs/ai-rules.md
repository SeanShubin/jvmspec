## Risk Mitigation

- I am currently prototyping and have checked in my code to version control
- Unless I am mistaken and have not checked in my code, skip any risk mitigation steps because I can roll back the
  changes entirely if I don't like them.

## Package Hierarchy

- Concept
    - Package hierarchies should represent organizational grouping and namespacing, not functional architecture
- Implementation
    - The grouping of packages should be according to business domain first, only functional area when necessary
    - Super packages should only be used to group related packages according to a supercategory
    - The package hierarchy should generally be flat unless and until there is an organizational reason to make it deep
    - Dependencies should not flow vertically up or down the package hierarchy (no dependencies between direct ancestors
      and descendants)
    - Dependencies may flow across the package hierarchy, such as sibling packages
    - As long as the dependencies don't flow directly up or down the hierarchy, it does not matter how shallow or deep
      the dependencies are in relation to each other
    - Packages with multiple responsibilities should be split
    - There shall be no packages in a cyclic dependency

## Free Floating Functions

- Concept
    - Although stateless functions don't need a class to hold state, classes still provide organizational and
      namespacing utility and make it easier to avoid name collisions
- Implementation
    - Wrap free floating functions in companion objects if there is a single clear class with the same responsibility
    - Wrap free floating functions in objects if there is not a single clear class with the same responsibility
    - Also apply these rules to extension functions

## Level of Detail

- Concept
    - Higher level concerns are never mixed with lower level concerns.
- Implementation
    - Split any mixed concerns into separate code
    - This applies to EVERY level of detail, including package level, class level, and function level
    - This is a special case of the single responsibility principle

## Anonymous Code

- Concept
    - The intent of logic is easier to understand when it has a name
- Implementation
    - No code in parameter lists, assign the result of the code to an identifier with a sensible name, and pass in the
      identifier
    - No long methods, split the code fragments into smaller, named, methods
    - Never use a comment to express something that can be expressed with a sensible name
    - Comments are sometimes appropriate to point to external reference material for further reading on a decision

## Dependency Injection

- Concept
    - Implementations should only know about collaborator contracts orchestrated through composition roots, not other
      implementations
- Implementation
    - All dependencies should be injected from composition root classes
    - Composition root classes are allowed to create instances but should have no logic
    - Factory classes are allowed to create instances, but should be injected via composition root like any other class
    - For values not known at compile time rely on Multi-stage Dependency Injection
    - Concrete classes should be isolated from other concrete classes
