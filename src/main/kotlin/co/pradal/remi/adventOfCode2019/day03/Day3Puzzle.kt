package co.pradal.remi.adventOfCode2019.day03

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


fun solveDay3Step1(input: String = inputString): Int {
    val (firstWire, secondWire) = getParsedInput(input).let { it.first.toSegment() to it.second.toSegment() }

    return getIntersections(firstWire, secondWire)
        .map { abs(it.x) + abs(it.y) }
        .filter { it != 0 }
        .min() ?: throw IllegalStateException()
}


fun solveDay3Step2(input: String = inputString): Int {
    val (firstWire, secondWire) = getParsedInput(input).let { it.first.toSegment() to it.second.toSegment() }

    return getIntersections(firstWire, secondWire)
        .map { computeDistanceOnWire(it, firstWire) + computeDistanceOnWire(it, secondWire) }
        .filter { it != 0 }
        .sorted()
        .first()
}

fun computeDistanceOnWire(intersection: Position, wire: List<Segment>): Int {
    val distance = wire
        .takeWhile { !isOnSegment(intersection, it) }
        .map { distance(it) }
        .sum()

    val distanceOnSegmentIntersection = wire
        .first { isOnSegment(intersection, it) }
        .let { Segment(it.first, intersection) }
        .let(::distance)

    return distance + distanceOnSegmentIntersection
}

fun distance(segment: Segment): Int {
    return ((segment.first.x - segment.second.x).toDouble().pow(2) + (segment.first.y - segment.second.y).toDouble().pow(
        2
    )).pow(0.5).toInt()
}

fun isOnSegment(position: Position, secondSegment: Segment): Boolean {
    if (position.x == secondSegment.first.x && position.x == secondSegment.second.x && position.y in min(
            secondSegment.first.y,
            secondSegment.second.y
        )..max(secondSegment.first.y, secondSegment.second.y)
    ) {
        return true
    }

    if (position.y == secondSegment.first.y && position.y == secondSegment.second.y && position.x in min(
            secondSegment.first.x,
            secondSegment.second.x
        )..max(secondSegment.first.x, secondSegment.second.x)
    ) {
        return true
    }

    return false
}

fun getIntersections(firstWire: List<Segment>, secondWire: List<Segment>): List<Position> {
    return firstWire
        .flatMap { firstWireSegment -> secondWire.map { secondWireSegment -> firstWireSegment to secondWireSegment } }
        .asSequence()
        .filter { areSegmentsIntersect(it.first, it.second) }
        .map { computeIntersection(it.first, it.second) }
        .toList()
}

fun List<WireMoveToken>.toSegment(): List<Segment> {
    return this.fold(Position(0, 0) to listOf<Segment>()) { acc, element ->
        val newSegmentOrigin = acc.first
        val newSegmentDestination = when (element.direction) {
            Direction.UP -> Position(newSegmentOrigin.x, newSegmentOrigin.y + element.measure)
            Direction.DOWN -> Position(newSegmentOrigin.x, newSegmentOrigin.y - element.measure)
            Direction.RIGHT -> Position(newSegmentOrigin.x + element.measure, newSegmentOrigin.y)
            Direction.LEFT -> Position(newSegmentOrigin.x - element.measure, newSegmentOrigin.y)
        }
        newSegmentDestination to (acc.second + (newSegmentOrigin to newSegmentDestination))
    }.second
}

fun areSegmentsIntersect(firstSegment: Segment, secondSegment: Segment): Boolean {
    val areLineIntersect = discriment(firstSegment, secondSegment) != 0
    val (verticalSegment, horizontalSegment) = if (firstSegment.first.x == firstSegment.second.x) {
        firstSegment to secondSegment
    } else {
        secondSegment to firstSegment
    }

    val isIntersectionOnSegment =
        verticalSegment.first.x in min(
            horizontalSegment.first.x,
            horizontalSegment.second.x
        )..max(horizontalSegment.first.x, horizontalSegment.second.x) &&
                horizontalSegment.first.y in min(
            verticalSegment.first.y,
            verticalSegment.second.y
        )..max(verticalSegment.first.y, verticalSegment.second.y)

    return areLineIntersect && isIntersectionOnSegment
}

fun discriment(firstSegment: Segment, secondSegment: Segment): Int {
    return (firstSegment.second.y - firstSegment.first.y) * (secondSegment.first.x - secondSegment.second.x) - (secondSegment.second.y - secondSegment.first.y) * (firstSegment.first.x - firstSegment.second.x)
}

fun computeIntersection(firstSegment: Segment, secondSegment: Segment): Position {
    val intersectionX =
        ((firstSegment.first.x * firstSegment.second.y - firstSegment.first.y * firstSegment.second.x) * (secondSegment.first.x - secondSegment.second.x) -
                (firstSegment.first.x - firstSegment.second.x) * (secondSegment.first.x * secondSegment.second.y - secondSegment.first.y * secondSegment.second.x)) / discriment(
            firstSegment,
            secondSegment
        )

    val intersectionY =
        ((firstSegment.first.x * firstSegment.second.y - firstSegment.first.y * firstSegment.second.x) * (secondSegment.first.y - secondSegment.second.y) -
                (firstSegment.first.y - firstSegment.second.y) * (secondSegment.first.x * secondSegment.second.y - secondSegment.first.y * secondSegment.second.x)) / discriment(
            firstSegment,
            secondSegment
        )

    return Position(intersectionX, intersectionY)
}

fun getParsedInput(input: String): Pair<List<WireMoveToken>, List<WireMoveToken>> {
    val splitInput = input.split('\n')
    return getWireFromString(splitInput[0]) to getWireFromString(splitInput[1])
}

fun getWireFromString(inputWire: String): List<WireMoveToken> = inputWire
    .split(',')
    .map { WireMoveToken(direction = it[0].toToken(), measure = it.substring(1).toInt()) }

private fun Char.toToken() =
    Direction.values().find { it.directionRepresentation == this } ?: throw IllegalStateException()

data class WireMoveToken(val direction: Direction, val measure: Int)

data class Position(val x: Int, val y: Int)

typealias Segment = Pair<Position, Position>

enum class Direction(val directionRepresentation: Char) {
    UP('U'),
    DOWN('D'),
    RIGHT('R'),
    LEFT('L'),
}

val inputString = """
R992,U221,L822,U805,R667,D397,L969,U433,R918,D517,L494,U909,L224,D738,R247,D312,L803,D656,L571,D968,L392,D332,L581,U487,R522,D780,L74,D561,L246,U380,L125,U11,R735,D761,R482,D208,R985,D991,L352,U140,L586,D492,L777,U96,R682,D969,R775,U279,R671,D423,R838,U907,L486,D702,L432,D625,R463,U559,R12,D531,R510,D347,R147,U949,R175,U160,L975,D627,L537,D343,L406,D237,R953,U725,L996,D740,L703,D996,R157,U356,R247,D541,L592,D345,R580,U656,R50,D423,L158,U502,L86,U729,L720,D464,R901,D739,L20,U21,R497,D14,L580,U610,L114,D858,R853,U550,L354,D433,L507,U144,R9,U422,R674,U604,R107,D999,L420,U675,R538,D491,R84,D158,R303,D450,L616,U938,L162,U102,L160,U275,L281,D164,L254,U103,R60,D707,R655,U128,L907,U225,L292,U919,R517,D276,R308,D113,L455,U584,R899,U321,L417,U449,L780,U387,L579,U224,L192,D325,L626,U145,R178,D162,L18,D469,R169,U694,R162,D806,L10,U979,L944,D304,R719,D253,L343,D711,R429,D933,R445,D772,R230,D407,R335,U883,L900,D377,R413,D44,R805,D378,R421,D860,L597,U63,L583,D561,R235,D502,L37,U29,L381,U803,R588,D972,R678,D223,L440,U835,R88,D16,R529,D867,R742,U25,R353,D952,R31,D202,R737,D744,R765,U154,L969,U851,L22,U165,L12,D457,R635,U829,L996,D871,L397,U995,R215,D505,R93,U12,R183,D920,L442,D393,L919,D803,R22,D806,R776,U558,R263,D222,R111,D530,L908,D640,R351,D172,R315,U731,R25,U718,L172,D145,L606,U803,R837,U310,L607,D523,R271,U927,R3,U518,R754,D322,L924,D256,L997,U153,L904,D745,L475,U346,L979,D658,R208,U924,L484,U961,R94,D283,L79,U927,R122,D513,L806,D480,L971,U340,R328,D427,L494
L998,U308,R889,D471,R719,U326,L6,U802,L608,U149,R454,U6,R837,U255,L720,D60,L426,D525,L190,U995,R676,U172,R910,U645,R249,D725,R355,U668,L988,U253,L820,D266,R836,D750,R998,U113,L502,U634,L620,U903,L542,D426,L497,D766,R930,U415,R655,D676,L694,D548,L280,U895,L899,U235,R912,D257,R161,D834,R88,D379,L723,U508,L604,D1,R706,D321,R725,U986,R52,D741,L738,D810,R595,U352,L835,D712,R797,D332,L451,D145,L608,U940,R886,D945,R929,D4,R332,D303,L877,D927,R686,U762,L588,D496,R352,D516,R355,D299,L459,D831,R9,U322,R635,U895,L127,U27,R996,D491,L360,U921,L146,U833,L420,D60,R32,D936,R815,D451,R715,U570,R889,D35,R135,U814,L559,D141,L470,U410,L711,D668,L196,U42,R989,U448,L875,U417,R554,U61,R259,D111,L177,D147,L925,D427,R911,U667,L209,U641,L516,U521,R373,D165,L91,U594,R968,U536,L694,U270,R602,U92,L158,U321,R422,D851,L73,D492,L698,D950,L988,U48,L184,D99,R67,D168,R269,D918,L645,D736,L597,U104,L427,U72,R568,D749,R16,U190,L146,D911,L820,D275,R12,U402,R461,D595,L103,D326,R948,U288,L1,D786,R698,D286,L557,U283,R278,U327,R457,D136,L878,D23,L371,U836,R987,U695,R904,U395,R869,U276,R310,D843,L994,D209,R554,U653,L924,U659,R695,U779,L427,U504,R711,D679,R191,D775,R816,D293,L415,D323,R505,U154,R966,U446,R837,U707,L591,D593,L696,U168,R35,U905,R141,U708,L772,D898,R254,U612,R934,U114,R912,D576,L721,D965,R731,U737,R494,D760,R909,D244,R662,D863,L23,D298,L234,D476,L571,D786,L48,U960,L377,U134,R335,D453,R203,D120,L27,U365,R254,U446,R738,D919,L42,U529,R31,D104,R583,U272,R867,U834,L43,D220,R424
""".trimIndent()