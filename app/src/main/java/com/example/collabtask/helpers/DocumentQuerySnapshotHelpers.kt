package com.example.collabtask.helpers

import com.google.firebase.firestore.QuerySnapshot
import kotlin.reflect.KClass

class DocumentQuerySnapshotHelpers {
    companion object {
        fun <T : Any> convertDocumentSnapshotsToDataList(
            snapshots: QuerySnapshot,
            clazz: KClass<T>
        ): List<T> {
            val dataList = mutableListOf<T>()
            for (document in snapshots) {
                val data = document.toObject<T>(clazz.java) as T
                dataList.add(data)
            }
            return dataList
        }
    }
}