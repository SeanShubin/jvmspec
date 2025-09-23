package com.seanshubin.jvmspec.domain.aggregation

interface DescriptorBuilderState {
    fun parseCharacter(c: Char): DescriptorBuilderState
    fun build(): SignatureParts

    companion object {
        fun build(descriptor: String): SignatureParts {
            return descriptor.fold(Start(descriptor, 0), ::nextCharacter).build()
        }

        fun nextCharacter(acc: DescriptorBuilderState, c: Char): DescriptorBuilderState = acc.parseCharacter(c)
        class Start(val descriptor: String, val index: Int) : DescriptorBuilderState {
            override fun parseCharacter(c: Char): DescriptorBuilderState {
                return when (c) {
                    '(' -> ParameterList(descriptor, index + 1, emptyList(), 0)
                    else -> ReturnType(descriptor, index + 1, null, 0).parseCharacter(c)
                }
            }

            override fun build(): SignatureParts = throwMustBeInEndStateToBuild(descriptor, index)
        }

        class ParameterList(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>,
            val dimensions: Int
        ) : DescriptorBuilderState {
            override fun parseCharacter(c: Char): DescriptorBuilderState {
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

            override fun build(): SignatureParts {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class ClassParameter(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>,
            val dimensions: Int,
            val soFar: String
        ) : DescriptorBuilderState {
            override fun parseCharacter(c: Char): DescriptorBuilderState {
                return when (c) {
                    '.', '[' -> throwParseError(descriptor, index + 1, c, javaClass.simpleName)
                    ';' -> ParameterList(descriptor, index + 1, parameters + SignatureType(soFar, dimensions), 0)
                    else -> ClassParameter(descriptor, index + 1, parameters, dimensions, soFar + c)
                }
            }

            override fun build(): SignatureParts {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class ReturnType(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>?,
            val dimensions: Int
        ) : DescriptorBuilderState {
            override fun parseCharacter(c: Char): DescriptorBuilderState {
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

            override fun build(): SignatureParts {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class ClassReturnType(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>?,
            val dimensions: Int,
            val soFar: String
        ) : DescriptorBuilderState {
            override fun parseCharacter(c: Char): DescriptorBuilderState {
                return when (c) {
                    '.', '[' -> throwParseError(descriptor, index, c, javaClass.simpleName)
                    ';' -> FullSignature(descriptor, index + 1, parameters, SignatureType(soFar, dimensions))
                    else -> ClassReturnType(descriptor, index + 1, parameters, dimensions, soFar + c)
                }
            }

            override fun build(): SignatureParts {
                throwMustBeInEndStateToBuild(descriptor, index)
            }
        }

        class FullSignature(
            val descriptor: String,
            val index: Int,
            val parameters: List<SignatureType>?,
            val returnType: SignatureType
        ) : DescriptorBuilderState {
            override fun parseCharacter(c: Char): DescriptorBuilderState {
                throwParseError(descriptor, index, c, javaClass.simpleName)
            }

            override fun build(): SignatureParts {
                return SignatureParts(parameters, returnType)
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
/*
'V','Z','C','B','S','I','F','J','D'


(I)V
(J)V
()J
()Ljava/lang/Thread;
()Ljava/net/InetAddress;
Ljava/lang/String;
(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z
(IDLjava/lang/Thread;)Ljava/lang/Object;


java/lang/System:exit:(I)V
java/lang/Thread:sleep:(J)V
java/lang/System:currentTimeMillis:()J
java/lang/Thread:currentThread:()Ljava/lang/Thread;
java/net/InetAddress:getLocalHost:()Ljava/net/InetAddress;
java/io/File:separator:Ljava/lang/String;
java/nio/file/Files:exists:(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z
*/
/*
B	byte	signed byte
C	char	Unicode character code point in the Basic Multilingual Plane, encoded with UTF-16
D	double	double-precision floating-point value
F	float	single-precision floating-point value
I	int	integer
J	long	long integer
L ClassName ;	reference	an instance of class ClassName
S	short	signed short
Z	boolean	true or false
[	reference	one array dimension
 */
/*
(IDLjava/lang/Thread;)Ljava/lang/Object;
Object m(int i, double d, Thread t) {...}
 */