package com.agungtriu.ecommerce.data.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agungtriu.ecommerce.core.remote.model.response.DataTypePayment
import com.agungtriu.ecommerce.core.remote.model.response.ResponsePayment
import com.agungtriu.ecommerce.helper.Extension.toResponseError
import com.agungtriu.ecommerce.helper.ViewState
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfig @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) {
    fun getPayment(): LiveData<ViewState<List<DataTypePayment>>> {
        val result = MutableLiveData<ViewState<List<DataTypePayment>>>()
        result.value = ViewState.Loading
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                val data = Gson().fromJson(
                    remoteConfig.getValue("payment").asString(),
                    ResponsePayment::class.java
                ).data
                if (data != null) {
                    result.value = ViewState.Success(data)
                }
            }
            .addOnFailureListener {
                result.value = ViewState.Error(it.toResponseError())
            }

        return result
    }

    fun updatePayment(): LiveData<ViewState<List<DataTypePayment>>> {
        val result = MutableLiveData<ViewState<List<DataTypePayment>>>()
        result.value = ViewState.Loading
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("payment")) {
                    remoteConfig.activate().addOnCompleteListener {
                        val data = Gson().fromJson(
                            Firebase.remoteConfig.getValue("payment").asString(),
                            ResponsePayment::class.java
                        ).data
                        if (data != null) {
                            result.value = ViewState.Success(data)
                        }
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                result.value = ViewState.Error(error.toResponseError())
            }
        })
        return result
    }
}
