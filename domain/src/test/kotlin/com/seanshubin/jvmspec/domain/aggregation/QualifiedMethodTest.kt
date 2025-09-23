package com.seanshubin.jvmspec.domain.aggregation

import kotlin.test.Test
import kotlin.test.assertEquals

class QualifiedMethodTest {
    @Test
    fun typical() {
        val input = QualifiedMethod(
            className = "java/lang/System",
            methodName = "getProperty",
            methodDescriptor = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
        )
        val expected = "String System.getProperty(String, String)"

        val actual = input.javaSignature()
        assertEquals(expected, actual)
    }
}
/*
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