package com.example.fpquiz.data.remote

import retrofit2.http.GET

interface QuizApiService {
    @GET("v1/d80b0829-6015-4c3b-bdc8-e91537527418")
    suspend fun getPreguntes(): List<PreguntaDto>
}