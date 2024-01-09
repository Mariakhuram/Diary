package com.mk.diary.domain.usecase

import com.mk.diary.domain.repository.BackgroundThemeRepository
import javax.inject.Inject

class BackgroundThemeUseCase @Inject constructor(private val rep:BackgroundThemeRepository) {

    suspend fun classicBackground():List<String> {
        return rep.classicImages()
    }
    suspend fun holidayBackground():List<String> {
        return rep.holidayImages()
    }

    suspend fun cuteBackground():List<String> {
        return rep.cuteImages()
    }
    suspend fun simpleBackground():List<String> {
        return rep.simpleImages()
    }
    suspend fun colorBackground():List<String> {
        return rep.colorImages()
    }

    suspend fun allBackground():List<String> {
        return rep.getAllImages()
    }

    suspend fun getSliderTheme():List<String> {
        return rep.getSliderTheme()
    }

    suspend fun getApplyTheme():List<String> {
        return rep.getApplyTheme()
    }
}