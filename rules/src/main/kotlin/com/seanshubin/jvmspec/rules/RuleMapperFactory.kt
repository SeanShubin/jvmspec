package com.seanshubin.jvmspec.rules

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

object RuleMapperFactory {
    fun createObjectMapper(): ObjectMapper {
        val module = SimpleModule()
        module.addDeserializer(Predicate::class.java, PredicateDeserializer())
        module.addDeserializer(RuleQuantifier::class.java, RuleQuantifierDeserializer())

        return ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .registerModule(module)
    }

    class PredicateDeserializer : JsonDeserializer<Predicate>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Predicate {
            val node: JsonNode = p.codec.readTree(p)
            val op = node.get("op").asText()

            return when (op) {
                "contains" -> {
                    val value = node.get("value").asText()
                    Predicate.Contains(value)
                }

                "equals" -> {
                    val value = node.get("value").asText()
                    Predicate.Equals(value)
                }

                "or" -> {
                    val predicatesNode = node.get("predicates")
                    val predicates = predicatesNode.map { predicateNode ->
                        val parser = predicateNode.traverse(p.codec)
                        parser.nextToken()
                        deserialize(parser, ctxt)
                    }
                    Predicate.Or(predicates)
                }

                else -> throw IllegalArgumentException("Unknown predicate op: $op")
            }
        }
    }

    class RuleQuantifierDeserializer : JsonDeserializer<RuleQuantifier>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): RuleQuantifier {
            val node: JsonNode = p.codec.readTree(p)
            val type = node.get("type").asText()

            return when (type) {
                "zero-or-more" -> {
                    val predicateNode = node.get("predicate")
                    val parser = predicateNode.traverse(p.codec)
                    parser.nextToken()
                    val predicate = p.codec.readValue(parser, Predicate::class.java) as Predicate
                    RuleQuantifier.ZeroOrMore(predicate)
                }

                "exactly" -> {
                    val count = node.get("count").asInt()
                    val predicateNode = node.get("predicate")
                    val parser = predicateNode.traverse(p.codec)
                    parser.nextToken()
                    val predicate = p.codec.readValue(parser, Predicate::class.java) as Predicate
                    RuleQuantifier.Exactly(count, predicate)
                }

                "at-end" -> {
                    RuleQuantifier.AtEnd
                }

                else -> throw IllegalArgumentException("Unknown rule quantifier type: $type")
            }
        }
    }
}
