package com.seanshubin.jvmspec.classfile.descriptor

interface DescriptorParser {
    fun parseCharacter(c: Char): DescriptorParser
    fun build(): Descriptor

    companion object {
        fun build(descriptorString: String): Descriptor {
            return descriptorString.fold(Start(descriptorString, 0), ::nextCharacter).build()
        }

        fun nextCharacter(acc: DescriptorParser, c: Char): DescriptorParser = acc.parseCharacter(c)
        class Start(val descriptorString: String, val index: Int) : DescriptorParser {
            override fun parseCharacter(c: Char): DescriptorParser {
                return when (c) {
                    '(' -> ParameterList(descriptorString, index + 1, emptyList(), 0)
                    else -> ReturnType(descriptorString, index + 1, null, 0).parseCharacter(c)
                }
            }

            override fun build(): Descriptor = throwMustBeInEndStateToBuild(descriptorString, index)
        }

        class ParameterList(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>,
            val dimensions: Int
        ) : DescriptorParser {
            override fun parseCharacter(c: Char): DescriptorParser {
                return when (c) {
                    'L' -> ClassParameter(descriptor, index + 1, parameters, dimensions, "")
                    '[' -> ParameterList(descriptor, index + 1, parameters, dimensions + 1)
                    'V', 'Z', 'C', 'B', 'S', 'I', 'F', 'J', 'D' -> ParameterList(
                        descriptor,
                        index + 1,
                        parameters + SignatureType(primitiveTypes.getValue(c), 0),
                        dimensions
                    )

                    ')' -> ReturnType(descriptor, index + 1, parameters, 0)
                    else -> throwParseError(descriptor, index, c, javaClass.simpleName)
                }
            }

            override fun build(): Descriptor {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class ClassParameter(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>,
            val dimensions: Int,
            val soFar: String
        ) : DescriptorParser {
            override fun parseCharacter(c: Char): DescriptorParser {
                return when (c) {
                    '.', '[' -> throwParseError(descriptor, index + 1, c, javaClass.simpleName)
                    ';' -> ParameterList(descriptor, index + 1, parameters + SignatureType(soFar, dimensions), 0)
                    else -> ClassParameter(descriptor, index + 1, parameters, dimensions, soFar + c)
                }
            }

            override fun build(): Descriptor {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class ReturnType(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>?,
            val dimensions: Int
        ) : DescriptorParser {
            override fun parseCharacter(c: Char): DescriptorParser {
                return when (c) {
                    'L' -> ClassReturnType(descriptor, index + 1, parameters, dimensions, "")
                    '[' -> ReturnType(descriptor, index + 1, parameters, dimensions + 1)
                    'V', 'Z', 'C', 'B', 'S', 'I', 'F', 'J', 'D' -> FullSignature(
                        descriptor,
                        index + 1,
                        parameters,
                        SignatureType(primitiveTypes.getValue(c), 0)
                    )

                    else -> throwParseError(descriptor, index, c, javaClass.simpleName)
                }
            }

            override fun build(): Descriptor {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class ClassReturnType(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>?,
            val dimensions: Int,
            val soFar: String
        ) : DescriptorParser {
            override fun parseCharacter(c: Char): DescriptorParser {
                return when (c) {
                    '.', '[' -> throwParseError(descriptor, index, c, javaClass.simpleName)
                    ';' -> FullSignature(descriptor, index + 1, parameters, SignatureType(soFar, dimensions))
                    else -> ClassReturnType(descriptor, index + 1, parameters, dimensions, soFar + c)
                }
            }

            override fun build(): Descriptor {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class FullSignature(
            val descriptorString: String,
            val index: Int,
            val parameters: List<SignatureType>?,
            val returnType: SignatureType
        ) : DescriptorParser {
            override fun parseCharacter(c: Char): DescriptorParser {
                throwParseError(descriptorString, index, c, javaClass.simpleName)
            }

            override fun build(): Descriptor {
                return Descriptor(descriptorString, parameters, returnType)
            }
        }

        fun throwParseError(descriptor: String, index: Int, c: Char, stateName: String): Nothing =
            throw IllegalArgumentException("For descriptor '$descriptor', can't parse character '$c' at index $index with state ${stateName}")

        fun throwMustBeInEndStateToBuild(descriptor: String, index: Int): Nothing =
            throw IllegalArgumentException("Must be in end state to build: descriptor '$descriptor', index $index")

        val primitiveTypes = mapOf(
            'V' to "void",
            'Z' to "boolean",
            'C' to "char",
            'B' to "byte",
            'S' to "short",
            'I' to "int",
            'F' to "float",
            'J' to "long",
            'D' to "double"
        )
    }
}