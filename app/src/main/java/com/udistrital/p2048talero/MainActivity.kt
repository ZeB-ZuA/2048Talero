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

    lateinit var binding: GameBoardBinding
    private lateinit var gestureDetector: GestureDetector
    private val gameLogic = GameLogic()
   private val BoardRepository = BoardRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GameBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gameLogic.startGame()
        updateUI()

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null) {
                    val diffX = e2.x - e1.x
                    val diffY = e2.y - e1.y
                    if (abs(diffX) > abs(diffY)) {

                        if (diffX > 0) {
                            gameLogic.swipeRight()
                            updateUI()

                            println("Deslizamiento hacia la derecha")
                        } else {
                            gameLogic.swipeLeft()
                            updateUI()
                            println("Deslizamiento hacia la izquierda")
                        }
                    } else {

                        if (diffY > 0) {
                            gameLogic.swipeDown()
                            updateUI()
                            println("Deslizamiento hacia abajo")
                        } else {
                            gameLogic.swipeUp()
                            updateUI()
                            println("Deslizamiento hacia arriba")
                        }
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    fun updateUI() {
        val buttons = arrayOf(
            binding.c1,
            binding.c2,
            binding.c3,
            binding.c4,
            binding.c5,
            binding.c6,
            binding.c7,
            binding.c8,
            binding.c9,
            binding.c10,
            binding.c11,
            binding.c12,
            binding.c13,
            binding.c14,
            binding.c15,
            binding.c16
        )
        val score = binding.Score
        score.text = "Score => ${gameLogic.score.toString()}"

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val value = gameLogic.getCellValue(i, j)
                buttons[i * 4 + j].text = if (value != 0) value.toString() else ""
                buttons[i * 4 + j].setBackgroundColor(Color.parseColor(gameLogic.getColor(value)))
            }
        }
        if (gameLogic.hasWon()) {
            Toast.makeText(this, "¡Has ganado!", Toast.LENGTH_SHORT).show()
        } else if (gameLogic.hasLost()) {
            Toast.makeText(this, "Has perdido. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
        }

        BoardRepository.save(gameLogic)
    }


}


