package com.seanshubin.jvmspec.rules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Files
import java.nio.file.Path

class JsonRuleLoader(private val rulesPath: Path) : RuleLoader {
    private val objectMapper: ObjectMapper = RuleMapperFactory.createObjectMapper()

    override fun load(): CategoryRuleSet {
        val json = Files.readString(rulesPath)
        return objectMapper.readValue(json)
    }
}
