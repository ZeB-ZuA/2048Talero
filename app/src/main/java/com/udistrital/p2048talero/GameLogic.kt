
package com.udistrital.p2048talero

import kotlin.random.Random

class GameLogic {
    // Matriz de 4x4 que representa el tablero del juego
    val board = Array(4) { IntArray(4) }

    // Variable para almacenar la puntuación del jugador
    var score = 0

    // Mapa que asocia valores del tablero con colores específicos
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

    // Método para iniciar el juego
    fun startGame() {
        fillRandomCell() // Llena una celda aleatoria con 2 o 4
        fillRandomCell() // Llena otra celda aleatoria con 2 o 4
    }

    // Método para obtener el valor de una celda específica
    fun getCellValue(i: Int, j: Int): Int {
        return board[i][j] // Devuelve el valor de la celda en la posición (i, j)
    }

    // Método para mover y combinar las celdas hacia la izquierda
    fun swipeLeft() {
        for (i in 0 until 4) { // Itera sobre cada fila
            val row = compressRow(getRow(i)) // Comprime y combina la fila
            setRow(i, row) // Actualiza la fila en el tablero
        }
        fillRandomCell() // Llena una celda aleatoria con 2 o 4
    }

    // Método para mover y combinar las celdas hacia la derecha
    fun swipeRight() {
        for (i in 0 until 4) { // Itera sobre cada fila
            val row = compressRow(getRow(i).reversed().toIntArray()) // Invierte, comprime y combina la fila
            setRow(i, row.reversed().toIntArray()) // Invierte de nuevo y actualiza la fila en el tablero
        }
        fillRandomCell() // Llena una celda aleatoria con 2 o 4
    }

    // Método para mover y combinar las celdas hacia arriba
    fun swipeUp() {
        for (j in 0 until 4) { // Itera sobre cada columna
            val column = compressRow(getColumn(j)) // Comprime y combina la columna
            setColumn(j, column) // Actualiza la columna en el tablero
        }
        fillRandomCell() // Llena una celda aleatoria con 2 o 4
    }

    // Método para mover y combinar las celdas hacia abajo
    fun swipeDown() {
        for (j in 0 until 4) { // Itera sobre cada columna
            val column = compressRow(getColumn(j).reversed().toIntArray()) // Invierte, comprime y combina la columna
            setColumn(j, column.reversed().toIntArray()) // Invierte de nuevo y actualiza la columna en el tablero
        }
        fillRandomCell() // Llena una celda aleatoria con 2 o 4
    }

    // Método para verificar si el jugador ha ganado
    fun hasWon(): Boolean {
        for (i in 0 until 4) { // Itera sobre cada celda del tablero
            for (j in 0 until 4) {
                if (board[i][j] == 2048) { // Si encuentra una celda con el valor 2048
                    return true // El jugador ha ganado
                }
            }
        }
        return false // Si no encuentra una celda con el valor 2048, el jugador no ha ganado
    }

    // Método para verificar si el jugador ha perdido
    fun hasLost(): Boolean {
        for (i in 0 until 4) { // Itera sobre cada celda del tablero
            for (j in 0 until 4) {
                if (board[i][j] == 0) { // Si encuentra una celda vacía (valor 0)
                    return false // El jugador no ha perdido
                }
                if (j < 3 && board[i][j] == board[i][j + 1]) { // Si hay celdas adyacentes con el mismo valor horizontalmente
                    return false // El jugador no ha perdido
                }
                if (i < 3 && board[i][j] == board[i + 1][j]) { // Si hay celdas adyacentes con el mismo valor verticalmente
                    return false // El jugador no ha perdido
                }
            }
        }
        return true // Si no hay celdas vacías ni movimientos posibles, el jugador ha perdido
    }

    // Método para llenar una celda aleatoria vacía con un 2 o un 4
    private fun fillRandomCell() {
        while (true) { // Loop hasta encontrar una celda vacía
            val i = Random.nextInt(4) // Genera una coordenada aleatoria para la fila
            val j = Random.nextInt(4) // Genera una coordenada aleatoria para la columna
            if (board[i][j] == 0) { // Si la celda está vacía
                val randomNumber = Random.nextInt(1, 11) // Genera un número aleatorio del 1 al 10
                board[i][j] = if (randomNumber == 1) 4 else 2 // 10% probabilidad de 4, 90% probabilidad de 2
                break // Sale del loop una vez que llena una celda vacía
            }
        }
    }

    // Método para obtener una fila del tablero
    private fun getRow(i: Int): IntArray {
        return board[i] // Devuelve la fila i del tablero
    }

    // Método para establecer una fila del tablero
    private fun setRow(i: Int, row: IntArray) {
        board[i] = row // Establece la fila i del tablero con los valores proporcionados
    }

    // Método para obtener una columna del tablero
    private fun getColumn(j: Int): IntArray {
        return IntArray(4) { i -> board[i][j] } // Devuelve la columna j del tablero como un array
    }

    // Método para establecer una columna del tablero
    private fun setColumn(j: Int, column: IntArray) {
        for (i in 0 until 4) { // Itera sobre cada fila
            board[i][j] = column[i] // Establece el valor de la columna j en la fila i
        }
    }

    // Método para comprimir y combinar una fila
    private fun compressRow(row: IntArray): IntArray {
        var lastNonZero = -1 // Índice de la última celda no cero encontrada
        for (i in 0 until 4) { // Itera sobre cada celda de la fila
            if (row[i] != 0) { // Si la celda no está vacía
                if (lastNonZero >= 0 && row[lastNonZero] == row[i]) { // Si la última celda no cero es igual a la actual
                    row[lastNonZero] *= 2 // Combina las celdas duplicando el valor
                    score += row[lastNonZero] // Actualiza la puntuación
                    row[i] = 0 // Vacía la celda actual
                } else {
                    lastNonZero++ // Actualiza el índice de la última celda no cero
                    row[lastNonZero] = row[i] // Mueve el valor a la última celda no cero
                    if (lastNonZero != i) { // Si el valor fue movido
                        row[i] = 0 // Vacía la celda original
                    }
                }
            }
        }
        return row // Devuelve la fila comprimida
    }

    // Método para obtener el color asociado a un valor específico del tablero
    fun getColor(value: Int): String {
        return colors[value] ?: "#FFFFFF" // Devuelve el color asociado o blanco si el valor no está en el mapa
    }
}
