package com.marioraphael.carp

interface ApiService {
    @GET("driver-profile")
    fun getDriverProfile(): Call<DriverProfile>
}