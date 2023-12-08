class MathUtil {
    companion object {

        fun lowestCommonDenominator(num1: Long, num2: Long): Long {
            return num1 * (num2 / greatestCommonDivisor(num1, num2))
        }

        fun lowestCommonDenominator(input: List<Long>): Long {
            var result = input[0]
            for (i in 1 until input.size) result = lowestCommonDenominator(result, input[i])
            return result
        }

        private fun greatestCommonDivisor(num1: Long, num2: Long): Long {
            var a = num1
            var b = num2
            while (b > 0) {
                val temp = b
                b = a % b // % is remainder
                a = temp
            }
            return a
        }
    }


}