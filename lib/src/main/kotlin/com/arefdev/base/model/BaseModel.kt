package com.arefdev.base.model

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class BaseModel {

    companion object {
        private val gson = GsonBuilder().create()

        fun <T> fromJson(json: String?, cls: Class<T>?): T {
            return gson.fromJson(json, cls)
        }

        fun <T> fromJson(json: String?, type: Type?): T {
            return gson.fromJson(json, type)
        }

        fun <T> fromJson(json: JsonElement?, cls: Class<T>?): T {
            return gson.fromJson(json, cls)
        }

        fun <T> fromJson(json: JsonElement?, type: Type?): T {
            return gson.fromJson(json, type)
        }

        fun toJsonString(obj: Any?): String {
            return gson.toJson(obj)
        }

        protected fun <T> jsonSerialize(src: T, context: JsonSerializationContext): JsonObject {
            val json = JsonObject()
            val fields: Array<Field> = src!!::class.java.declaredFields
            for (field in fields) {
                val accessible = field.isAccessible
                field.isAccessible = true
                run {
                    var done = false
                    for (annotation in field.declaredAnnotations) {
                        if (annotation is SerializedName) {
                            try {
                                val element = context.serialize(field[src])
                                json.add(annotation.value, element)
                                done = true
                            } catch (ignored: Exception) {
                            }
                        }
                    }
                    if (!done) {
                        try {
                            val element = context.serialize(field[src])
                            json.add(field.name, element)
                        } catch (ignored: Exception) {
                        }
                    }
                }
                field.isAccessible = accessible
            }
            return json
        }

        protected fun <T> jsonDeserialize(json: JsonObject, dst: T, context: JsonDeserializationContext) {
            val fields: Array<Field> = dst!!::class.java.declaredFields
            for (field in fields) {
                val accessible = field.isAccessible
                field.isAccessible = true
                run {
                    var done = false
                    for (annotation in field.declaredAnnotations) {
                        if (annotation is SerializedName) {
                            try {
                                field[dst] = context.deserialize(json[annotation.value], field.type)
                                done = true
                            } catch (e1: Exception) {
                                for (alternative in annotation.alternate) {
                                    try {
                                        field[dst] = context.deserialize(json[alternative], field.type)
                                        done = true
                                        break
                                    } catch (ignored: Exception) {
                                    }
                                }
                            }
                        }
                    }
                    if (!done) {
                        try {
                            field[dst] = context.deserialize(json[field.name], field.type)
                        } catch (ignored: Exception) {
                        }
                    }
                }
                field.isAccessible = accessible
            }
        }
    }

    fun toJson(): JsonElement {
        return gson.toJsonTree(this)
    }

    fun toJsonString(): String {
        return gson.toJson(this)
    }

    fun clone(): BaseModel {
        return gson.fromJson(toJsonString(), javaClass)
    }
}
