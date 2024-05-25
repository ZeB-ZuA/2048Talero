package com.udistrital.p2048talero

import kotlin.random.Random

class GameLogic {
    val board = Array(4) { IntArray(4) }
    var score = 0

    private val colors = mapOf(
        2 to "#EEE4DA",
        4 to "#EDE0C8",
        8 to "#F2B179",
        16 to "#F59563",
        32 to "#F67C5F",
        64 to "#F65E3B",
        128 to "#EDCF72",
        256 to "#EDCC61",
        512 to "#EDC850",
        1024 to "#EDC53F",
        2048 to "#EDC22E"
    )
    fun startGame() {
        fillRandomCell()
        fillRandomCell()

    }
    fun getCellValue(i: Int, j: Int): Int {
        return board[i][j]
    }

    fun swipeLeft() {
        for (i in 0 until 4) {
            val row = compressRow(getRow(i))
            setRow(i, row)
        }
        fillRandomCell()
    }

    fun swipeRight() {
        for (i in 0 until 4) {
            val row = compressRow(getRow(i).reversed().toIntArray())
            setRow(i, row.reversed().toIntArray())
        }
        fillRandomCell()
    }

    fun swipeUp() {
        for (j in 0 until 4) {
            val column = compressRow(getColumn(j))
            setColumn(j, column)
        }
        fillRandomCell()
    }

    fun swipeDown() {
        for (j in 0 until 4) {
            val column = compressRow(getColumn(j).reversed().toIntArray())
            setColumn(j, column.reversed().toIntArray())
        }
        fillRandomCell()
    }

    fun hasWon(): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (board[i][j] == 2048) {
                    return true
                }
            }
        }
        return false
    }

    fun hasLost(): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (board[i][j] == 0) {
                    return false
                }
                if (j < 3 && board[i][j] == board[i][j + 1]) {
                    return false
                }
                if (i < 3 && board[i][j] == board[i + 1][j]) {
                    return false
                }
            }
        }
        return true
    }

    private fun fillRandomCell() {
        while (true) {
            val i = Random.nextInt(4)
            val j = Random.nextInt(4)
            if (board[i][j] == 0) {
                val randomNumber = Random.nextInt(1, 11)
                board[i][j] = if (randomNumber == 1) 4 else 2
                break
            }
        }
    }


    private fun getRow(i: Int): IntArray {
        return board[i]
    }

    private fun setRow(i: Int, row: IntArray) {
        board[i] = row
    }

    private fun getColumn(j: Int): IntArray {
        return IntArray(4) { i -> board[i][j] }
    }

    private fun setColumn(j: Int, column: IntArray) {
        for (i in 0 until 4) {
            board[i][j] = column[i]
        }
    }

    private fun compressRow(row: IntArray): IntArray {
        var lastNonZero = -1
        for (i in 0 until 4) {
            if (row[i] != 0) {
                if (lastNonZero >= 0 && row[lastNonZero] == row[i]) {
                    row[lastNonZero] *= 2
                    score += row[lastNonZero]
                    row[i] = 0
                } else {
                    lastNonZero++
                    row[lastNonZero] = row[i]
                    if (lastNonZero != i) {
                        row[i] = 0
                    }
                }
            }
        }
        return row
    }

    fun getColor(value: Int): String {
        return colors[value] ?: "#FFFFFF"
    }

}
