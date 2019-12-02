package co.pradal.remi.adventOfCode2019.day02

import java.lang.IllegalStateException

fun solvePuzzle(): Int {
    val parsedInput = intCodeInput
        .split(',')
        .map(String::toInt)

    val preProcessedInput = parsedInput
        .replace(1, 12)
        .replace(2, 2)

    return processParsedInput(0, preProcessedInput).first()
}

fun processParsedInput(position: Int, input: List<Int>): List<Int> {
    return when (input[position].toToken()) {
        Token.Add -> processParsedInput(position + 4, applyOperation(input, position, Int::plus))
        Token.Multiply -> processParsedInput(position + 4, applyOperation(input, position, Int::times))
        Token.End -> input
    }
}

fun applyOperation(input: List<Int>, position: Int, operator: (Int, Int) -> Int): List<Int> {
    val firstValue = input[input[position + 1]]
    val secondValue = input[input[position + 2]]
    val positionToOverwrite = input[position + 3]
    return input.replace(positionToOverwrite, operator(firstValue, secondValue))
}

fun <T> List<T>.replace(position: Int, element: T): List<T> {
    return this.toMutableList().apply {
        this[position] = element
    }
}

private fun Int.toToken(): Token {
    return Token
        .values()
        .find { it.value == this }
        ?: throw IllegalStateException()
}


val intCodeInput = """
    1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,10,19,23,1,6,23,27,1,5,27,31,1,10,31,35,2,10,35,39,1,39,5,43,2,43,6,47,2,9,47,51,1,51,5,55,1,5,55,59,2,10,59,63,1,5,63,67,1,67,10,71,2,6,71,75,2,6,75,79,1,5,79,83,2,6,83,87,2,13,87,91,1,91,6,95,2,13,95,99,1,99,5,103,2,103,10,107,1,9,107,111,1,111,6,115,1,115,2,119,1,119,10,0,99,2,14,0,0
""".trimIndent()

enum class Token(val value: Int) {
    Add(1),
    Multiply(2),
    End(99)
}