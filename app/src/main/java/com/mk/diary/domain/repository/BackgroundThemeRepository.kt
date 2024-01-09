package com.mk.diary.domain.repository


interface BackgroundThemeRepository {
    suspend fun classicImages():List<String>
    suspend fun holidayImages():List<String>
    suspend fun simpleImages():List<String>
    suspend fun cuteImages():List<String>
    suspend fun colorImages():List<String>

    suspend fun getAllImages():List<String>

    suspend fun getSliderTheme():List<String>

    suspend fun getApplyTheme():List<String>
}