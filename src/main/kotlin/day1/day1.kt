package day1

import java.io.File

fun calculate(initialValue: Int): Int {
    val list: List<String> = readFileLineByLineUsingForEachLine("src/main/resources/day1.txt")

    var result: Int = initialValue
    val frequencySet: MutableSet<Int> = mutableSetOf<Int>()
    var found: Boolean = false

    for (it in list) {
        val value = it.substring(1).toInt()
        val operation = it.substring(0, 1)

        if (operation == "+") {
            result += value
        } else {
            result -= value
        }


        if (!found) {
            if (frequencySet.contains(result)) {
                found = true
                print("found dupplicate on frequency of  $result\n")
            } else {
                frequencySet.add(result)
            }
        }

    }

    return result
}

fun readFileLineByLineUsingForEachLine(fileName: String) = File(fileName).useLines { (it).toList() }


fun main(args: Array<String>) {
    val initialValue = 0
    println(calculate(initialValue))
}
