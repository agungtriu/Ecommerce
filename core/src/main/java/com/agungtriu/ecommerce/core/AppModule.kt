package com.agungtriu.ecommerce.core

import android.content.Context
import androidx.room.Room
import com.agungtriu.ecommerce.core.datastore.DataStoreManager
import com.agungtriu.ecommerce.core.remote.ApiService
import com.agungtriu.ecommerce.core.room.AppDatabase
import com.agungtriu.ecommerce.core.utils.Config.API_BASE_URL
import com.agungtriu.ecommerce.core.utils.Config.DATABASE_NAME
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAuthInterceptor(dataStoreManager: DataStoreManager): AuthInterceptor =
        AuthInterceptor(dataStoreManager)

    @Provides
    fun provideAuthAuthentication(
        dataStoreManager: DataStoreManager,
        appDatabase: AppDatabase,
        chuckerInterceptor: ChuckerInterceptor,
        authInterceptor: AuthInterceptor
    ): Authenticator =
        AuthAuthenticator(dataStoreManager, appDatabase, chuckerInterceptor, authInterceptor)

    @Provides
    fun provideChuckerCollector(@ApplicationContext appContext: Context): ChuckerCollector {
        return ChuckerCollector(
            context = appContext,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    }

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext appContext: Context,
        chuckerCollector: ChuckerCollector
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(appContext)
            .collector(chuckerCollector)
            .createShortcut(true)
            .build()
    }

    @Provides
    fun provideClient(
        chuckerInterceptor: ChuckerInterceptor,
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(chuckerInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_BASE_URL)
            .client(client)
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
