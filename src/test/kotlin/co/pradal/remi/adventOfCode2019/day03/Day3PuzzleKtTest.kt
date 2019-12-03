package co.pradal.remi.adventOfCode2019.day03

import org.junit.Assert.assertEquals
import org.junit.Test


class Day3PuzzleKtTest {

    @Test
    fun `solveDay3Step1 test case 1`() {
        val input = """
            R75,D30,R83,U83,L12,D49,R71,U7,L72
            U62,R66,U55,R34,D71,R55,D58,R83
        """.trimIndent()

        val result = solveDay3Step1(input)

        assertEquals(159, result)
    }

    @Test
    fun `solveDay3Step1 test case 2`() {
        val input = """
            R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
            U98,R91,D20,R16,D67,R40,U7,R15,U6,R7
        """.trimIndent()

        val result = solveDay3Step1(input)

        assertEquals(135, result)
    }
}