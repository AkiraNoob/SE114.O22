package com.example.collabtask.use_case

import com.example.collabtask.model.Card
import com.example.collabtask.model.CardComment
import com.example.collabtask.model.Label
import com.example.collabtask.model.User
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class BoardListDetailApiUseCases {
    companion object {
        private val cardFirestore = FirebaseFirestore.getInstance().collection("card")
        private val userFirestore = FirebaseFirestore.getInstance().collection("user")
        suspend fun getCardsOfBoardList(boardListId: String): List<Card> {

            val cardList = mutableListOf<Card>()

            val querySnapshot = cardFirestore.whereEqualTo("listId", boardListId).get().await()

            for (document in querySnapshot.documents) {
                val card = document.toObject(Card::class.java)
                if (card != null) {
                    card.id = document.id
                    val labelQuerySnapshot =
                        cardFirestore.document(document.id).collection("label").get().await()
                    val labelList = mutableListOf<Label>()

                    for (labelDocument in labelQuerySnapshot.documents) {
                        val label = labelDocument.toObject(Label::class.java)
                        if (label != null) {
                            labelList.add(label)
                        }
                    }
                    card.labelList = labelList

                    val commentQuerySnapshot =
                        cardFirestore.document(document.id).collection("comment").get().await()
                    val commentList = mutableListOf<CardComment>()

                    for (commentQuerySnapshotDocument in commentQuerySnapshot.documents) {
                        val comment = commentQuerySnapshotDocument.toObject(CardComment::class.java)
                        if (comment != null) {
                            commentList.add(comment)
                        }
                    }
                    card.commentList = commentList

                    cardList.add(card)
                }
            }



            return cardList
        }
    }
}