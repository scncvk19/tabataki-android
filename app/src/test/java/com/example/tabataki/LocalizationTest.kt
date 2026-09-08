package com.example.tabataki

import org.junit.Assert.assertEquals
import org.junit.Test

class LocalizationTest {
    @Test
    fun returnsSelectedLanguageTranslation() {
        assertEquals("Arbeitszeit", AppStrings.get(Language.DE, "work"))
    }

    @Test
    fun fallsBackToEnglishWhenTranslationIsMissing() {
        assertEquals("Delete Category?", AppStrings.get(Language.RU, "del_cat_title"))
    }

    @Test
    fun returnsBlankForUnknownKey() {
        assertEquals(" ", AppStrings.get(Language.EN, "unknown_key"))
    }
}
