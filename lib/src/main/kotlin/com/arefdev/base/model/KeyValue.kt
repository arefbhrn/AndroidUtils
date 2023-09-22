package com.arefdev.base.model

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
data class KeyValue<K, V>(
    val key: K,
    var value: V
) : BaseModel()
