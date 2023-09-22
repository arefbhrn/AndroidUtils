package com.arefdev.base.utils

import android.util.Pair

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object SearchAlgorithms {

    private fun normalize(str: String): String {
        return StringUtils.strToPersian(str.lowercase())
    }

    object InStrings {

        fun startingWithSearch(list: List<String>, query: String): List<String> {
            val mQuery = normalize(query)
            val result = mutableListOf<String>()
            for (i in list.indices) {
                val temp = normalize(list[i])
                if (temp.startsWith(mQuery)) {
                    result.add(list[i])
                }
            }
            return result
        }

        fun exactSearch(list: List<String>, query: String): List<String> {
            val mQuery = normalize(query)
            return list.filter { normalize(it) == mQuery }
        }

        fun containSearch(list: List<String>, query: String): List<String> {
            val mQuery = normalize(query)
            return list.filter {
                var normalizedItem = normalize(it)
                var isOK = true
                for (j in mQuery.indices) {
                    val lastCharIndex = normalizedItem.indexOf(mQuery[j])
                    if (lastCharIndex < 0) {
                        isOK = false
                        break
                    }
                    if (normalizedItem.length > lastCharIndex + 1) {
                        normalizedItem = normalizedItem.substring(lastCharIndex + 1)
                    }
                }
                return@filter isOK
            }
        }

        fun exactContainSearch(list: List<String>, query: String): List<String> {
            val mQuery = normalize(query)
            return list.filter { normalize(it).contains(mQuery) }
        }

        fun faultyContainSearch(list: List<String>, query: String): List<String> {
            val mQuery = normalize(query)
            val result = mutableListOf<Pair<String, Float>>()
            for (item in list) {
                var normalizedItem = normalize(item)
                if (normalizedItem[0] != mQuery[0]) {
                    continue
                }
                var score = 1f
                for (j in 1 until mQuery.length) {
                    val lastCharIndex = normalizedItem.indexOf(mQuery[j])
                    if (lastCharIndex < 0) {
                        continue
                    }
                    score++
                    if (normalizedItem.length > lastCharIndex + 1) {
                        normalizedItem = normalizedItem.substring(lastCharIndex + 1)
                    }
                }
                score /= mQuery.length.toFloat()
                result.add(Pair(item, score))
            }
            result.sortWith { a, b ->
                return@sortWith if (a.second < b.second)
                    1
                else if (a.second > b.second)
                    -1
                else
                    0
            }
            val finalResult = mutableListOf<String>()
            for (item in result) {
                if (item.second > 0.4) {
                    finalResult.add(item.first)
                }
            }
            return finalResult
        }
    }
}
