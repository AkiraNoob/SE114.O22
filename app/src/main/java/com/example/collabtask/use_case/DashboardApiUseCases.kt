package com.example.collabtask.use_case

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class DashboardApiUseCases {
    companion object {
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()
        private var boardFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("board")
        private var userJoinedTeamFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("userJoinedTeam")
        private var userJoinedBoardFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("userJoinedBoard")

        suspend fun getPersonalBoard(): QuerySnapshot? {
            if (auth.currentUser != null) {
                val currentUserId = auth.currentUser!!.uid
                return boardFirestore.whereEqualTo("teamId", null)
                    .whereEqualTo("ownerId", currentUserId).get().await()
            }
            return null
        }

        suspend fun getUserJoinedTeam(): List<DocumentSnapshot>? {
            if (auth.currentUser != null) {
                val currentUserId = auth.currentUser!!.uid
                val userJoinedTeam =
                    userJoinedTeamFirestore.whereEqualTo("userId", currentUserId).get().await()

                val teamList: MutableList<DocumentSnapshot> = mutableListOf()

                for (item in userJoinedTeam.documents) {
                    teamList.add(TeamApiUseCases.getTeamById(item.data!!["teamId"].toString()))
                }

                return teamList.sortedBy { it.data!!["name"] as String }
            }

            return null
        }

        suspend fun getRecentBoardsOfTeam(teamId: String): QuerySnapshot? {
            if (auth.currentUser != null) {
                val currentUserId = auth.currentUser!!.uid
                val userJoinedBoard: List<String> =
                    userJoinedBoardFirestore.whereEqualTo("userId", currentUserId).get()
                        .await().documents.map {
                            it.data!!["boardId"] as String
                        }

                return boardFirestore.whereIn(FieldPath.documentId(), userJoinedBoard)
                    .whereEqualTo("teamId", teamId).limit(3)
                    .orderBy("name", Query.Direction.ASCENDING).get().await()
            }

            return null
        }
    }
}