package com.seanshubin.jvmspec.domain.io

import java.io.DataInput

class IndexedDataInput(private val delegate:DataInput): DataInput {
    var index:Int = 0
    override fun readFully(b: ByteArray) {
        delegate.readFully(b)
        index += b.size
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        delegate.readFully(b, off, len)
        index += len
    }

    override fun skipBytes(n: Int): Int {
        return delegate.skipBytes(n).also{
            index += n
        }
    }

    override fun readBoolean(): Boolean {
        return delegate.readBoolean().also {
            index +=1
        }
    }

    override fun readByte(): Byte {
        return delegate.readByte().also {
            index += 1
        }
    }

    override fun readUnsignedByte(): Int {
        return delegate.readUnsignedByte().also {
            index += 1
        }
    }

    override fun readShort(): Short {
        return delegate.readShort().also {
            index += 2
        }
    }

    override fun readUnsignedShort(): Int {
        return delegate.readUnsignedShort().also {
            index += 2
        }
    }

    override fun readChar(): Char {
        return delegate.readChar().also {
            index += 2
        }
    }

    override fun readInt(): Int {
        return delegate.readInt().also {
            index += 4
        }
    }

    override fun readLong(): Long {
        return delegate.readLong().also {
            index += 8
        }
    }

    override fun readFloat(): Float {
        return delegate.readFloat().also {
            index += 4
        }
    }

    override fun readDouble(): Double {
        return delegate.readDouble().also {
            index += 8
        }
    }

    override fun readLine(): String? {
        throw UnsupportedOperationException("Not sure how to compute this, but probably not needed")
    }

    override fun readUTF(): String {
        throw UnsupportedOperationException("Not sure how to compute this, but probably not needed")
    }
}