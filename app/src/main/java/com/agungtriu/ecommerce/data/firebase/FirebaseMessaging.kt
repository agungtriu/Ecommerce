package com.agungtriu.ecommerce.data.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseMessaging @Inject constructor() {
    fun subscribeTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic)
    }
}
