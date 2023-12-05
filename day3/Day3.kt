package day3

import NeighbourUtil.Companion.generateNeighbours

fun main() {
    val engineSchematic = input.split("\n")
        .map { row -> row.split("").filter(String::isNotBlank) }

    val maxColumnIndex = engineSchematic.size
    val maxRowIndex = engineSchematic[0].size

    val board = engineSchematic
        .flatMapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, value -> createSquare(value, rowIndex, columnIndex) }
        }
    val squaredByCoordinates = board.associateBy { square -> square.coordinates() }

    //Find all int squares near symbols. This array contains squares that are part of a partNumber.
    val intSquaresNearSymbols = board.filterIsInstance<SymbolSquare>()
        .flatMap { square -> generateNeighbours(square.rowIndex, square.columnIndex, maxRowIndex, maxColumnIndex) }
        .mapNotNull { coordinate -> squaredByCoordinates[coordinate] }
        .filterIsInstance<IntSquare>()

    val partNumbersNearSymbols = generatePartNumbers(intSquaresNearSymbols, squaredByCoordinates, maxColumnIndex).sum()
    val sumOfGearRatios = board.asSequence().filterIsInstance<SymbolSquare>()
        .filter { square -> square.value == "*" }
        .map { square -> generateNeighbours(square.rowIndex, square.columnIndex, maxRowIndex, maxColumnIndex) }
        .map { neighbours ->
            neighbours.mapNotNull { coordinate -> squaredByCoordinates[coordinate] }.filterIsInstance<IntSquare>()
        }
        .map { gearNeighbours -> generatePartNumbers(gearNeighbours, squaredByCoordinates, maxColumnIndex) }
        .filter { partNumbers -> partNumbers.size > 1 }
        .sumOf { partNumbers -> partNumbers[0] * partNumbers[1] }




    println("Result 1: $partNumbersNearSymbols")
    println("Result 2: $sumOfGearRatios")
}

fun generatePartNumbers(
    squares: List<IntSquare>,
    squaredByCoordinates: Map<Pair<Int, Int>, Square>,
    maxColumnIndex: Int
): List<Int> {
    // Generating part numbers from this array would possibly create doubles. So we first need to undouble it before generating numbers
    return undouble(squares).map { square -> generatePartNumbers(square, squaredByCoordinates, maxColumnIndex) }
}

private fun generatePartNumbers(
    square: IntSquare,
    squaredByCoordinates: Map<Pair<Int, Int>, Square>,
    maxColumnIndex: Int
): Int {
    val partNumberSquares = mutableListOf(square)
    var columnLeft = square.column - 1
    while (columnLeft >= 0 && squaredByCoordinates[Pair(square.row, columnLeft)] is IntSquare) {
        val intSquare = squaredByCoordinates[Pair(square.row, columnLeft)]
        if (intSquare != null && intSquare is IntSquare) {
            partNumberSquares.add(intSquare)
        }
        columnLeft--
    }

    var columnRight = square.column + 1
    while (columnRight < maxColumnIndex && squaredByCoordinates[Pair(square.row, columnRight)] is IntSquare) {
        val intSquare = squaredByCoordinates[Pair(square.row, columnRight)]
        if (intSquare != null && intSquare is IntSquare) {
            partNumberSquares.add(intSquare)
        }
        columnRight++
    }
    partNumberSquares.sort()
    return partNumberSquares.map { partNumberSquare -> partNumberSquare.value }
        .joinToString(separator = "") { a -> a.toString() }
        .toInt()
}

fun undouble(squares: List<IntSquare>): List<IntSquare> {
    val sortedSquares = squares.sorted()
    val result = sortedSquares.toMutableList()

    var previousSquare: IntSquare = sortedSquares[0]
    for (i in 1 until sortedSquares.size) {
        val nextSquare = sortedSquares[i]
        if (nextSquare.row == previousSquare.row && nextSquare.column - previousSquare.column == 1) {
            //They are neighbours, so we can delete the previous square from the result list
            result.remove(previousSquare)
        }
        previousSquare = nextSquare
    }
    return result

}

private fun createSquare(value: String, rowIndex: Int, columnIndex: Int) =
    if (value.toIntOrNull() != null) {
        IntSquare(rowIndex, columnIndex, value.toInt())
    } else if (value == ".") {
        EmptySquare(rowIndex, columnIndex)
    } else {
        SymbolSquare(rowIndex, columnIndex, value)
    }

val exampleInput = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...${'$'}.*....
    .664.598..
""".trimIndent()

val input = """
    ..........................380.......................143............................108.............630...........425........................
    ....*585..30....217*616..........${'$'}...................../....${'$'}.................447...........381..................+..........973.............
    .210......*...............639...541..-........830*...........912..........743*.......................828..671........+......*...............
    .......760....${'$'}..............*........737.*.......949..568.......................=........628.85........&.#..........87...535.....794.......
    ....#......616..........373.999..392......853..........&.........666.......*.....365.............807............@....................*......
    .680................800*..............684................329....*.......960.186........725........*......&.....631.....700*818..............
    ............-402...........%.........@.........576.............956................../.....*....237....490........................998........
    ........624.........600/...283..906................301....903...........=495.....917..165..193......................................+.......
    ....977.+........................*........*610.....-....................................*.......489*795.......-....@545..915......*......641
    ...-.......123........@........113.....643............117.483../...................961.984..................878...........*........277..@...
    .............${'$'}....787..80....................490......${'$'}....../..802.....591...373...*..............228..................848..840............
    .613.......................810..........740......476.....902............${'$'}..........201......%.......${'$'}..-.......993......................701.
    ......268.......429.........${'$'}..........+....582..........@...538*789......................941...136.....334......*.........508...250........
    ........*...745*.........+......*77.........*......653..................#....27................/......+..........491....#....*...*....192...
    .......978.............26....957......*...400.........*834.#85.......429............................291................690...69.814....#....
    ...276.....=.571....................65....................................765.......179..720..................460.302.......................
    ...*.....577...*..........................=50..758............/.52@........*..........@..............279........-...........................
    ..821...........676..839...227..................*..........273...........789...42.288...#.......226......................451..592......943..
    .........*...................*......................................${'$'}................*..378.......*..876.497....@..575*.........*.....*.....
    ......524........353........664......781...../108..................124.............407........272.43.#....&..421.......708......../..405....
    ................*....769.............*.............../.........432..........57${'$'}..............*...................=...........887.637........
    ................96...*................700..%.......573........*....644..........802......798.897.......456..258.130..182......*.............
    ...........&.........420............#.....369..............121........${'$'}....916...*........*...............*.............*637.......*816.....
    ...911...97....990...................557.......................355............#.578........764.872.........80.....*792..........517.........
    .....*..........*...-609....675.............657......728......%....446....963.........201......%.......232.....576.......116........630.....
    ..801..........352............%..............*..........*422..........*..*........538..*.........*435.*.....................*730.....*......
    ....................&................952..283.....931...............95...617........%..215....174.........139........375...........755.426..
    ..........972.......82...500........*.............*...854.......208...........853%..................=......*...........*..941...........%...
    ....975+.....*.....................58....${'$'}..836.899..../.........*...................@682........952........193.......13.*........711.......
    ............757..............45.......453...../.............603.735......155..848..........402..........954..............530...........%....
    ..........................................285...........596...=.............*...............*.......*....#......*681...........*162.517.....
    .173.589.......892..451......268...........*............../............764...476...........206...860.123.....812.....97*319.139.............
    .......*............%....981....*288....917....666..............#.........&.......532@...-...............330.........................731....
    ......764.......*........*...................=...+.985...952...498..+473.................98..&..663..414....*...885&...788..../.............
    .............508.439.....956......799.122..786.............*.............214.................63............346........=......507............
    ...252*780.......................*....*.....................446............*.....900.540.................................510...........899..
    ...........169-......%..@........328.506................573.................367.....*..........646.896...815...691..............%141........
    .................-.195.606.....&................908......./...67*49...@.........707.....#607......*........*.....=..........................
    ..902..@......742.............706..355&.....953...*..811...............125..490....*241..............+..468..............512....334.........
    ...*....463.......802.260&....................*..711.-...-......................%.................360................811.%.............%....
    .113............%..*....................%861.999.......458..................334.924..425..406..........184#................712....#.....176.
    .........166...689.124......*550.840.......................742................#........%......405...........26.........546*.......665.......
    ............*............159........*...........@879.......*.........48..................987......751.319....*...113........................
    ..........163............................................453...524.....${'$'}..........*936...*...........*......119..+.......756.......&...326..
    777*241.......210.974.258.....528..............................${'$'}...............559.......921......@....382...............*........147...*...
    ....................*.*....................131*156........./................@......445.........477......*.....47=....875..730..........499..
    .....682..........545..856............................165..740...831.........368..........851......926...223............*...................
    ....*.............................447..................@...........*..............%...418....*.....*...........838...325....................
    ....722.......71....848...747........*..690....275..........740...16..123..129...837.....+.359.68..426......=....-........791...617..582....
    .............*............=...271...358.*.........*...................+.......*................*............378............${'$'}................
    570.......620..399*666...................702.....32...............215..........896.325......377...................=286.................200..
    ...*833...................................................12/.247......510.........*...369........813*..........*...........................
    ....................................%..........-......&..........*655....*.......380....*.............778.....98.910.......752..............
    .............643.783..............621....777..523.....384..${'$'}...........693...............336................................#..+............
    521.........*.......*.................${'$'}...................261.31..............549.................102......467-.......219......286....${'$'}118..
    ..........837........904..636.298.204..368.......47............/.................*...........246...*....#.......526......*..................
    .....201......497............*....*..............*................982........+..243.............*.774....488.......*..641...........140.....
    838...*....+.......................296....*765..489....8....................796.......=.......200................996.........364.......*....
    .......540.93....+...............................................*600.................47..................256........#..........*...+..823..
    ................14...-65..................328.............................407+..............54.464..990....*...305..129.......679..141......
    .........................487.-.....831.......*.....407.776.409.....75.............426..782...*...*..#......347....*....................864..
    ....995...................=...117.../..*..531.........*......${'$'}.......*973...........*...@......614................194........172...930*.....
    .......*386...........................799.....660...................................415....../.....%370...289............988................
    .....-..........835+..316.47..............226......904.........789..........................32............${'$'}....616..........=...............
    ...243.....26............*........479%....=....@...=..............*......#.....292..............389.....@.....*...................+...${'$'}546..
    ..........*.....&.......................${'$'}....199......@........&..25......345.....*36...626.....*.....632..108..69*362...&561..517..........
    .136*291...858...815........974.......913............786....908......495......805..........*122.948.........................................
    ...........................*...............103*363...............438...........*..@.@...........................%401.......90=......403.....
    .313&.618....@...${'$'}521.....332............................768.......*.........428.50..445.338.....*.......................%.....115.*........
    .......*.....583..............586...........=..............*.......11.......................=.171.280.........140..750...798...&....286..111
    .....604..........266......................566....-.....398..238.........499........#...@......................*.....%......................
    .........545*739.${'$'}............68..337*.........296..............*................694...100......&..............233..........................
    .....320................468....-........997......................796...48.....................219.....&............378.552...8..............
    .......${'$'}..........356+....*................*.........205................/........645*....600.........709.....................*...760........
    ...625..................681........870-...907.......-....142..173...859..............802...*.....................741..289*74.............705
    .........244......908....................................*.../........*..................501......92.............*..........................
    ............*......${'$'}.....576.384..111.119.............127..............720....898....534............*....347........................487*....
    ....610...922...........${'$'}.....*...*..........381*36.........................../.......*......665.843.......*....754.470..690................
    .....*.........${'$'}..135.......142.242.352..............................775...-.....*....67....*.........+...576......*.....#..................
    .....249....735......*.......................................402-...*....903..667.419......526........294...................................
    .361.....-......418.659.575......803.596......910....367...........950.....................................308........*.....................
    ...*......690..*...........*797.................*.......*....................160..........61.....#........*..........141.....402...599..811.
    ...505..............288%........525%...........355...324.................876*...............*538..39....482..696............@.........*.....
    ...............401........@............884..........................&.......................................*.........749.............86....
    .........484......*38..370........328.*............553......&845.....662................520................46................24.............
    .....407*..........................${'$'}..310..........*.....................765..672............832.......................@.657....555../..588.
    ..........107.789..200..213.....................898..545........67...........%................./...........101.742...422..%...=...&.622.....
    .............*......*............*292......946......&..............243+.651....178...665............./.........*.............92.............
    ......974...........814.......220......906......${'$'}......*.....879...........*......+.....*........-...995........185.18...........254..198...
    ...........791..808.....984...............&...985...871.678..*.............340................973..........253.......#..................*...
    ..../.....*........=....*..........92*411.....................835...................%.................#.../................${'$'}.........279....
    ....926....292...........839...333..........600.115....................=....&.....336.....350........966......999...540..908.............705
    ................................*...497...........*....994..........447......131.........*...................*..............................
    ...........${'$'}.....-....../.....394...=.......175.417....*................................719...................992....................-......
    ..........960.698.....610.986.........*776..........516...34......&.....75...751...${'$'}........%.......651.....=......300...........364.905....
    ...317.......................*..+...........................*....783.....*..=......655.......310.....+...339..........*......256*........145
    ....&..107..................276.136..905......548..%.......334.........471...................................279.727..696...................
    343...*......733........515..........${'$'}....357....@.301.........*348...................118.........&...439...*.......................922.....
    ....754.....-......646.@.................-..................929.................475......*......785......*..684..............27......*......
    ...................*.....463..../59.609...........168*863...................801...&..211..382..........347............28.............473....
    .................725.....#...........*......79..........................978*...........*......................199.....*.........579.........
    ...........................157*662....958...*...............546@.................&..181...416.109..............*....258.....421*............
    ...859......777=.............................217.862..................817*597...655........*.........76.....443.....................292.....
    ...@...170..........273...........+..=.............................=................217....253..438....*....................................
    .......&.......................971..876...............205.....394.502..122...........${'$'}.............${'$'}...40............802....................
    ..894........*.623...718..................216........*.........=.......................544.....................102............932.951..615..
    ....+......865........*...............................298.721................147.......*.........165.741*794...*......318...&.......*...*...
    ...................878..........................*.........../............947...........314.......*...........999............493.....996.330.
    ...........705.........79........994.645.703.203.258...........706...227*..........+............590....822*.................................
    .....468..*......*311./...@23...${'$'}.....@....................588...*.........317......113....857.............19.#..&260...571...251=....772...
    ......*..11...175..........................................*....259...509.....*...........*.......443..487....73........................*...
    .....161..............................................832...611..........*609..742.........938......*.*............@909.....66.232..........
    ..........125...............630.....596.......%477.......*........................................601..........158............*......201*864
    ...................819.......=..-.........................198..............=..........418...60............904...............................
    781...663..250...................281....905.+...187....%..............271..852...........=..*...484...517*..................................
    .......*......=....865.826...%............&.679..*.....160..576.....................462+...717.............&........389........783....67....
    .......988............*......79..441............474.........#.....471.......298................./.....*511.445......*......463*.............
    ...........91....807.............*......173.............#27.................*......937.353.....85..510..............109.#............70.....
    ...........*.......*...........955..398...@......75.........*...........32...221../....*....7..........234../481........841.........@.......
    ........#..593......864.....................932.*........960........160*.............953........385...+.....................................
    .702*..596.........................${'$'}....952*.....20.........................................257....*........741.858.......*......839..491...
    ................#........618.....693...............................333*87......173......268*......775..................699.710...*..........
    ..........985..586...818..*..........*723.......437*..........148...................333........................156.............821.....651..
    .....................#...982......360.......757.....706.122....&............245........-........16.......571.......544................${'$'}.....
    6...381......................657..............#...................112*.....*.....496......388.&....556#.....*428..*................-.....828
    ........#.398.688.900@..674....*....284.860..........-....................588.............*...47.........@........280............86.........
    .....526.....*.............*...........*.....154......267..960......563.......29.......398........309#....849./........240*685..............
    .......................692.420.978#.........*.....261.....%........+.........*.....492......${'$'}957..............979..................254.691..
    ...978#...131*980.......+.................925.....*...627......519....368*..324...*..................${'$'}..................52............*.....
    ....................453........400${'$'}............................*................745...+..........-..838..#....118.........=.....815.........
    ....679......796.........920........${'$'}...75...290..195.......307...340.....649..........403......165.....26.......*..........711.*...........
    ....*.........%..100.440.........206....${'$'}...*....*...................*254..........990..............249..........380....-.....%.535.........
    ..........988.....*...*...................399..723.....*....................107......*.....................49...........422.................
    ...800*......*..183..117..375......375..............807.691........981................890.......622...97.....${'$'}.515................@.........
    .......413.129...........*.............729....&137...........956....${'$'}.....976....................*....../..........................334......
    ...313..........794-..807.....*698.....&...........874*.........*...............71........................655...............................
    .....*.....................193............*547.........459.....338...489..581.......483..@115..125........*..........220....................
    ....907..361*.....243..834.............987.....149..........@.........*...............*........${'$'}.......509..698.142../.....@.=750......4....
    ....................-...*.....................*............611........21..251.&......578........................*.......717.......47#.......
    .........................610.26.............892...............................299............601............721..729........................
""".trimIndent()