package com.udistrital.p2048talero

import com.google.firebase.database.FirebaseDatabase

class BoardRepository {

    private val database = FirebaseDatabase.getInstance()
    private val gameRef = database.getReference("game")

    fun save(gameLogic: GameLogic){

        val gameData = mapOf(

            "board" to gameLogic.board.map { it.toList() }.toList().flatten(), //castea el board en una lista de listas para que el firebase lo recosnosza
            "score" to gameLogic.score
        )
        gameRef.setValue(gameData)

    }

}