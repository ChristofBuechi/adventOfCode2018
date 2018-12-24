package day1

import java.io.File

fun readData(initialValue: Int) {
    val operationList = readFileLineByLineUsingForEachLine("src/main/resources/day1.txt").map { createOperation(it) }


    val result: Int = calculateSum(initialValue, operationList)
    println("summarized result: $result")

    val doubleFrequency: Int = findDuplicate(initialValue, operationList)
    println("double frequency catch: $doubleFrequency")

}

private fun calculateSum(initialValue: Int, operationList: List<Operation>): Int {
    var result: Int = initialValue
    for (it in operationList) {

        if (it.operator == "+") {
            result += it.value
        } else {
            result -= it.value
        }
    }
    return result
}

fun findDuplicate(initialValue: Int, operationList: List<Operation>): Int {

    var result: Int = initialValue
    var finalResult: Int = 0
    val frequencySet: MutableSet<Int> = mutableSetOf<Int>()

    return generateSequence(0) { it + 1 }
            .map { it.rem(operationList.size) }            // gets position of next operation
            .map(operationList::get)                        // gets operation
            .map {                                         // apply the operation
                result = applyOperator(it, result)
                result
            }
            .map {                                      // check if the result was already found
                if (frequencySet.contains(it)) {
                    finalResult = it
                }
                frequencySet.add(it)
                it
            }         // adds result to frequencySet
            .first { it == finalResult }
}

private fun applyOperator(it: Operation, finalResult: Int): Int {
    var mutableResult = finalResult
    if (it.operator == "+") {
        mutableResult += it.value
    } else {
        mutableResult -= it.value
    }
    return mutableResult
}

fun createOperation(it: String): Operation {
    val value = it.substring(1).toInt()
    val operator = it.substring(0, 1)

    return Operation(operator, value)

}

fun readFileLineByLineUsingForEachLine(fileName: String) = File(fileName).useLines { (it).toList() }

fun main(args: Array<String>) {
    val initialValue = 0
    readData(initialValue)
}

class Operation(val operator: String, val value: Int)


