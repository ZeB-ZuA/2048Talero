package com.udistrital.p2048talero

import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.udistrital.p2048talero.databinding.GameBoardBinding
import com.udistrital.p2048talero.ui.theme.P2048TaleroTheme
import java.nio.file.spi.FileTypeDetector
import kotlin.math.abs


class MainActivity : ComponentActivity() {

    lateinit var binding: GameBoardBinding // Tardío inicialización para la vinculación del layout
    private lateinit var gestureDetector: GestureDetector // Tardío inicialización para el detector de gestos
    private val gameLogic = GameLogic() // Inicializa la lógica del juego
    private val BoardRepository = BoardRepository() // Inicializa el repositorio del tablero

    // Método que se llama cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GameBoardBinding.inflate(layoutInflater) // Infla el layout utilizando la vinculación
        setContentView(binding.root) // Establece la vista principal de la actividad
        gameLogic.startGame() // Inicia el juego
        updateUI() // Actualiza la interfaz de usuario

        // Inicializa el detector de gestos con una implementación personalizada
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?, // Evento inicial del gesto
                e2: MotionEvent, // Evento final del gesto
                velocityX: Float, // Velocidad del gesto en el eje X
                velocityY: Float // Velocidad del gesto en el eje Y
            ): Boolean {
                if (e1 != null) {
                    val diffX = e2.x - e1.x // Diferencia en el eje X
                    val diffY = e2.y - e1.y // Diferencia en el eje Y
                    if (abs(diffX) > abs(diffY)) {
                        if (diffX > 0) {
                            gameLogic.swipeRight() // Deslizar a la derecha
                            updateUI() // Actualiza la interfaz de usuario
                            println("Deslizamiento hacia la derecha")
                        } else {
                            gameLogic.swipeLeft() // Deslizar a la izquierda
                            updateUI() // Actualiza la interfaz de usuario
                            println("Deslizamiento hacia la izquierda")
                        }
                    } else {
                        if (diffY > 0) {
                            gameLogic.swipeDown() // Deslizar hacia abajo
                            updateUI() // Actualiza la interfaz de usuario
                            println("Deslizamiento hacia abajo")
                        } else {
                            gameLogic.swipeUp() // Deslizar hacia arriba
                            updateUI() // Actualiza la interfaz de usuario
                            println("Deslizamiento hacia arriba")
                        }
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY) // Llama a la implementación del super
            }
        })
    }

    // Método para manejar eventos táctiles
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event) // Pasa el evento al detector de gestos
        return super.onTouchEvent(event) // Llama a la implementación del super
    }

    // Método para actualizar la interfaz de usuario
    fun updateUI() {
        // Array de botones que representan las celdas del tablero
        val buttons = arrayOf(
            binding.c1, binding.c2, binding.c3, binding.c4,
            binding.c5, binding.c6, binding.c7, binding.c8,
            binding.c9, binding.c10, binding.c11, binding.c12,
            binding.c13, binding.c14, binding.c15, binding.c16
        )
        val score = binding.Score // Referencia al elemento de puntuación
        score.text = "Score => ${gameLogic.score}" // Actualiza el texto de la puntuación

        // Actualiza el texto y el color de cada botón según el valor en el tablero del juego
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val value = gameLogic.getCellValue(i, j) // Obtiene el valor de la celda
                buttons[i * 4 + j].text = if (value != 0) value.toString() else "" // Establece el texto del botón
                buttons[i * 4 + j].setBackgroundColor(Color.parseColor(gameLogic.getColor(value))) // Establece el color de fondo del botón
            }
        }

        // Verifica si el jugador ha ganado o perdido
        if (gameLogic.hasWon()) {
            Toast.makeText(this, "¡Has ganado!", Toast.LENGTH_SHORT).show() // Muestra un mensaje de victoria
        } else if (gameLogic.hasLost()) {
            Toast.makeText(this, "Has perdido. Intenta de nuevo.", Toast.LENGTH_SHORT).show() // Muestra un mensaje de derrota
        }

        BoardRepository.save(gameLogic) // Guarda el estado del juego en el repositorio
    }
}


