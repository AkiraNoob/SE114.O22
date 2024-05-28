package com.example.collabtask.use_case

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TeamApiUseCases {
    companion object {
        private val teamFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("team")
        suspend fun getTeamById(teamId: String): DocumentSnapshot {
            return teamFirestore.document(teamId).get().await()
        }
    }
}