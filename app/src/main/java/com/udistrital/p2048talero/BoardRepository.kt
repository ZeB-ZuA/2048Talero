package com.udistrital.p2048talero

import com.google.firebase.database.FirebaseDatabase

class BoardRepository {

    private val database = FirebaseDatabase.getInstance()
    private val gameRef = database.getReference("game")

    fun save(gameLogic: GameLogic){

        val gameData = mapOf(

            "board" to gameLogic.board.map { it.toList() }.toList().flatten(),
            "score" to gameLogic.score
        )



    }





}