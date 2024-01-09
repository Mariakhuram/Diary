package com.mk.diary.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


object RealtimeFirebaseInstance {
    private var database: DatabaseReference? = null
    fun getDatabaseInstance(): DatabaseReference {
        if (database == null) {
            // Initialize Firebase database instance if not already initialized
            database = FirebaseDatabase.getInstance().reference
        }
        return database!!
    }
}