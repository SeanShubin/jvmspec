package com.seanshubin.jvmspec.infrastructure.collections

data class TwoKeyMap<KeyType1, KeyType2, ValueType>(val map: Map<KeyType1, Map<KeyType2, ValueType>>) {
    fun put(
        key1: KeyType1,
        key2: KeyType2,
        value: ValueType,
        defaultValue: ValueType,
        transform: (ValueType) -> ValueType
    ): TwoKeyMap<KeyType1, KeyType2, ValueType> {
        val oldMap1 = map[key1] ?: emptyMap()
        val oldValue = oldMap1[key2] ?: defaultValue
        val newValue = transform(oldValue)
        val newMap1 = oldMap1 + (key2 to newValue)
        val newMap = map + (key1 to newMap1)
        return TwoKeyMap(newMap)
    }

    companion object {
        fun <KeyType1, KeyType2, ValueType> empty(): TwoKeyMap<KeyType1, KeyType2, ValueType> = TwoKeyMap(emptyMap())
    }
}