package co.pradal.remi.adventOfCode2019.day04

fun solveDay4Step1(range: IntRange = input): Int {
    return findPasswordCount(range) { pair -> pair.second != 1 }
}

fun solveDay4Step2(range: IntRange = input): Int {
    return findPasswordCount(range) { pair -> pair.second == 2 }
}

fun findPasswordCount(range: IntRange, predicate: (Pair<Char, Int>) -> Boolean): Int {
    return range
        .asSequence()
        .map { it.toString().toCharArray().toList() }
        .filter(::areDigitsIncreasing)
        .filter { groupAdjacentChar(it).any(predicate) }
        .toList()
        .size
}

fun areDigitsIncreasing(list: List<Char>): Boolean {
    return list
        .map { it.toString().toInt() }
        .fold(true to 0) { acc, element ->
            return@fold (acc.first && (acc.second <= element)) to element
        }
        .first
}

fun groupAdjacentChar(list: List<Char>): List<Pair<Char, Int>> {
    return list.fold(Pair<List<Pair<Char, Int>>, Pair<Char, Int>?>(listOf(), null)) { acc, element ->
        val previousSequence = acc.second
        return@fold when {
            previousSequence == null -> acc.first to Pair(element, 1)
            previousSequence.first == element -> acc.first to Pair(element, previousSequence.second + 1)
            else -> (acc.first + previousSequence) to Pair(element, 1)
        }
    }.let { it.first + it.second!! }
}

val input = "123257-647015".split('-').map(String::toInt).let { it[0]..it[1] }