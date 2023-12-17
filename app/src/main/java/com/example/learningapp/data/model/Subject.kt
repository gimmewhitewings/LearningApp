package com.example.learningapp.data.model

import androidx.annotation.StringRes
import com.example.learningapp.R

enum class Subject(@StringRes val nameResourceId: Int) {
    MATH(R.string.math),
    RUSSIAN(R.string.russian),
    PHYSICS(R.string.physics),
    INFORMATICS(R.string.informatics)
}
