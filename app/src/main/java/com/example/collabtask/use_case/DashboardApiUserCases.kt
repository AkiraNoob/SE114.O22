package com.example.collabtask.use_case

import com.example.collabtask.helpers.DocumentQuerySnapshotHelpers
import com.example.collabtask.model.Board
import com.example.collabtask.model.Team
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DashboardApiUserCases {
    companion object {
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()
        private var boardFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("board")
        private var userJoinedTeamFirestore: CollectionReference =
            FirebaseFirestore.getInstance().collection("userJoinedTeam")

        suspend fun getPersonalBoard(): List<Board> {
            if (auth.currentUser != null) {
                val currentUserId = auth.currentUser!!.uid
                val personalBoards = boardFirestore.whereEqualTo("teamId", null)
                    .whereEqualTo("ownerId", currentUserId).get().await()

                return DocumentQuerySnapshotHelpers.convertDocumentSnapshotsToDataList(
                    personalBoards,
                    Board::class
                )
            }
            return emptyList()
        }

        suspend fun getUserJoinedTeam(): List<Team> {
            if (auth.currentUser != null) {
                val currentUserId = auth.currentUser!!.uid
                val joinedTeam = userJoinedTeamFirestore.whereEqualTo("userId", currentUserId).get().await()

                return DocumentQuerySnapshotHelpers.convertDocumentSnapshotsToDataList(joinedTeam, Team::class)
            }

            return emptyList()
        }
    }
}