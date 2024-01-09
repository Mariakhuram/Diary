package com.mk.diary.data.repositoryimp

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mk.diary.domain.repository.BackgroundThemeRepository
import com.mk.diary.firebase.FirebaseKey
import com.mk.diary.firebase.RealtimeFirebaseInstance
import com.mk.diary.helpers.ResultCase
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class BackgroundThemeRepositoryImp @Inject constructor() : BackgroundThemeRepository {
    override suspend fun classicImages(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.BACKGROUND_THEME)
            .child(FirebaseKey.CLASSIC_THEME)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun holidayImages(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.BACKGROUND_THEME)
            .child(FirebaseKey.HOLIDAY_THEME)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun simpleImages(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.BACKGROUND_THEME)
            .child(FirebaseKey.SIMPLE_THEME)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun cuteImages(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.BACKGROUND_THEME)
            .child(FirebaseKey.CUTE_THEME)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun colorImages(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.BACKGROUND_THEME)
            .child(FirebaseKey.COLOR_THEME)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun getAllImages(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.BACKGROUND_THEME)
            .child(FirebaseKey.ALL_THEME)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun getSliderTheme(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.APP_THEME_SLIDER)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }

    override suspend fun getApplyTheme(): List<String> {
        val list = mutableListOf<String>()
        val defered = CompletableDeferred<List<String>>()
        RealtimeFirebaseInstance.getDatabaseInstance().database.getReference(FirebaseKey.APP_THEME_APPLY)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val mydata=data.getValue(String::class.java)
                        if (mydata!=null){
                            list.add(mydata)
                        }
                    }
                    defered.complete(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    defered.completeExceptionally(error.toException())
                }
            })
        return defered.await()
    }
}

