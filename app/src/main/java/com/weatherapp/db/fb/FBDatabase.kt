package com.weatherapp.db.fb

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FBDatabase {

    interface Listener {
        fun onUserLoaded(user: FBUser)
        fun onUserSignOut()
        fun onCityAdded(city: FBCity)
        fun onCityUpdated(city: FBCity)
        fun onCityRemoved(city: FBCity)
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var citiesListReg: ListenerRegistration? = null
    private var listener: Listener? = null

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                citiesListReg?.remove()
                listener?.onUserSignOut()
            } else {
                val uid = firebaseAuth.currentUser!!.uid
                val refCurrUser = db.collection("users").document(uid)

                refCurrUser.get().addOnSuccessListener { documentSnapshot ->
                    documentSnapshot.toObject(FBUser::class.java)?.let { user ->
                        listener?.onUserLoaded(user)
                    }
                }

                citiesListReg = refCurrUser.collection("cities")
                    .addSnapshotListener { snapshots, ex ->
                        if (ex != null) return@addSnapshotListener
                        snapshots?.documentChanges?.forEach { change ->
                            val fbCity = change.document.toObject(FBCity::class.java)
                            when (change.type) {
                                DocumentChange.Type.ADDED -> listener?.onCityAdded(fbCity)
                                DocumentChange.Type.MODIFIED -> listener?.onCityUpdated(fbCity)
                                DocumentChange.Type.REMOVED -> listener?.onCityRemoved(fbCity)
                            }
                        }
                    }
            }
        }
    }

    fun setListener(listener: Listener? = null) {
        this.listener = listener
    }

    fun register(user: FBUser) {
        val currentUser = auth.currentUser ?: throw RuntimeException("User not logged in!")
        db.collection("users").document(currentUser.uid).set(user)
    }

    fun add(city: FBCity) {
        val currentUser = auth.currentUser ?: throw RuntimeException("User not logged in!")
        val cityName = city.name ?: throw RuntimeException("City with null or empty name!")
        if (cityName.isEmpty()) throw RuntimeException("City with null or empty name!")
        db.collection("users").document(currentUser.uid).collection("cities").document(cityName).set(city)
    }

    fun remove(city: FBCity) {
        val currentUser = auth.currentUser ?: throw RuntimeException("User not logged in!")
        val cityName = city.name ?: throw RuntimeException("City with null or empty name!")
        if (cityName.isEmpty()) throw RuntimeException("City with null or empty name!")
        db.collection("users").document(currentUser.uid).collection("cities").document(cityName).delete()
    }
}