package com.seanshubin.jvmspec.rules

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JsonRuleLoader : RuleLoader {
    private val objectMapper: ObjectMapper = RuleMapperFactory.createObjectMapper()

    override fun load(json: String): CategoryRuleSet {
        return objectMapper.readValue(json)
    }
}
