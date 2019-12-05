package co.pradal.remi.adventOfCode2019.day05

import co.pradal.remi.adventOfCode2019.day03.Position
import kotlin.IllegalStateException


fun processInstructions(position: Int = 0, instructionList : List<Int>) {

}


enum class Token(val opCode: Int, val argumentNumber: Int) {
    Add(1, 3),
    Multiply(2, 3),
    Input(3, 1),
    Output(4, 1),
    End(99, 0)
}

enum class ParameterMode(val parameterModeCode: Int) {
    Immediate(0),
    Position(1)
}

private fun Int.toParameterMode(): ParameterMode =
    ParameterMode
        .values()
        .find { it.parameterModeCode == this }
        ?: throw IllegalStateException()

private fun Int.toToken() = Token.values().find { it.opCode == this } ?: throw IllegalStateException()

fun parseInstructionOpCode(value: Int): Pair<Token, List<ParameterMode>> {
    val reversedInstructionOpCode = value.toString().reversed()
    val token = reversedInstructionOpCode.first().toString().toInt().toToken()
    val parametersMode = reversedInstructionOpCode
        .padEnd(1 + token.argumentNumber)
        .slice(1 until 1 + token.argumentNumber)
        .map { it.toString().toInt().toParameterMode() }

    return token to parametersMode
}

data class Instruction(val token: Token, val parameters: List<Pair<ParameterMode, Int>>)