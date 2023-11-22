package com.agungtriu.ecommerce.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindPreLoginRepository(
        preLoginRepositoryImp: PreLoginRepositoryImp
    ): PreLoginRepository


    @Singleton
    @Binds
    abstract fun bindMainRepository(
        mainRepositoryImp: MainRepositoryImp
    ): MainRepository


    @Singleton
    @Binds
    abstract fun bindStoreRepository(
        storeRepositoryImp: StoreRepositoryImp
    ): StoreRepository


    @Singleton
    @Binds
    abstract fun bindWishlistRepository(
        wishlistRepositoryImp: WishlistRepositoryImp
    ): WishlistRepository


    @Singleton
    @Binds
    abstract fun bindCartRepository(
        cartRepositoryImp: CartRepositoryImp
    ): CartRepository


    @Singleton
    @Binds
    abstract fun bindCheckoutRepository(
        checkoutRepositoryImp: CheckoutRepositoryImp
    ): CheckoutRepository


    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        notificationRepositoryImp: NotificationRepositoryImp
    ): NotificationRepository
}