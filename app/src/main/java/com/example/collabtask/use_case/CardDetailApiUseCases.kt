package com.example.collabtask.use_case

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CardDetailApiUseCases {
    companion object {
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()
        private var cardFirestore: CollectionReference = FirebaseFirestore.getInstance().collection("card")
        suspend fun getCardDetail(cardId: String): DocumentSnapshot? {
            if(auth.currentUser != null) {

                return cardFirestore.document(cardId).get().await()
            }

            return null
        }
    }
}