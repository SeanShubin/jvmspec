package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

sealed class StackMapFrame {
    abstract val frameType: Int

    data class SameFrame(override val frameType: Int) : StackMapFrame() // 0-63

    data class SameLocals1StackItemFrame(
        override val frameType: Int, // 64-127
        val stack: VerificationTypeInfo
    ) : StackMapFrame()

    data class SameLocals1StackItemFrameExtended(
        override val frameType: Int = 247,
        val offsetDelta: UShort,
        val stack: VerificationTypeInfo
    ) : StackMapFrame()

    data class ChopFrame(
        override val frameType: Int, // 248-250
        val offsetDelta: UShort
    ) : StackMapFrame()

    data class SameFrameExtended(
        override val frameType: Int = 251,
        val offsetDelta: UShort
    ) : StackMapFrame()

    data class AppendFrame(
        override val frameType: Int, // 252-254
        val offsetDelta: UShort,
        val locals: List<VerificationTypeInfo>
    ) : StackMapFrame()

    data class FullFrame(
        override val frameType: Int = 255,
        val offsetDelta: UShort,
        val numberOfLocals: UShort,
        val locals: List<VerificationTypeInfo>,
        val numberOfStackItems: UShort,
        val stack: List<VerificationTypeInfo>
    ) : StackMapFrame()

    companion object {
        private fun appendFrameLocalCount(frameType: Int): Int = frameType - 251

        fun fromDataInput(input: DataInput): StackMapFrame {
            val frameType = input.readUnsignedByte()
            return when (frameType) {
                in 0..63 -> SameFrame(frameType)
                in 64..127 -> {
                    val stack = VerificationTypeInfo.fromDataInput(input)
                    SameLocals1StackItemFrame(frameType, stack)
                }

                in 128..246 -> throw IllegalArgumentException("Reserved frame type: $frameType")
                247 -> {
                    val offsetDelta = input.readUnsignedShort().toUShort()
                    val stack = VerificationTypeInfo.fromDataInput(input)
                    SameLocals1StackItemFrameExtended(offsetDelta = offsetDelta, stack = stack)
                }

                in 248..250 -> {
                    val offsetDelta = input.readUnsignedShort().toUShort()
                    ChopFrame(frameType, offsetDelta)
                }

                251 -> {
                    val offsetDelta = input.readUnsignedShort().toUShort()
                    SameFrameExtended(offsetDelta = offsetDelta)
                }

                in 252..254 -> {
                    val offsetDelta = input.readUnsignedShort().toUShort()
                    val numLocals = appendFrameLocalCount(frameType)
                    val locals = (0 until numLocals).map {
                        VerificationTypeInfo.fromDataInput(input)
                    }
                    AppendFrame(frameType, offsetDelta, locals)
                }

                255 -> {
                    val offsetDelta = input.readUnsignedShort().toUShort()
                    val numberOfLocals = input.readUnsignedShort().toUShort()
                    val locals = (0 until numberOfLocals.toInt()).map {
                        VerificationTypeInfo.fromDataInput(input)
                    }
                    val numberOfStackItems = input.readUnsignedShort().toUShort()
                    val stack = (0 until numberOfStackItems.toInt()).map {
                        VerificationTypeInfo.fromDataInput(input)
                    }
                    FullFrame(
                        offsetDelta = offsetDelta,
                        numberOfLocals = numberOfLocals,
                        locals = locals,
                        numberOfStackItems = numberOfStackItems,
                        stack = stack
                    )
                }

                else -> throw IllegalArgumentException("Invalid frame type: $frameType")
            }
        }
    }
}
