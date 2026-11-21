package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.BtsMember
import com.example.data.model.MotivationQuote
import com.example.data.model.QuoteLabelType
import com.example.data.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.civilsDataStore by preferencesDataStore(name = "civils_ias_prefs")

class AppPreferences(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LAST_SYNC_EPOCH = longPreferencesKey("last_sync_epoch")
        val EXTRA_QUOTES_JSON = stringPreferencesKey("extra_quotes_json")
        val DAILY_FLASHCARD_TARGET = stringPreferencesKey("daily_flashcard_target")
        val NOTIF_ONBOARDING_SHOWN = booleanPreferencesKey("notif_onboarding_shown")
    }

    val themeModeFlow: Flow<ThemeMode> = context.civilsDataStore.data.map { prefs ->
        ThemeMode.safeValueOf(prefs[Keys.THEME_MODE])
    }

    val lastSyncEpochFlow: Flow<Long> = context.civilsDataStore.data.map { prefs ->
        prefs[Keys.LAST_SYNC_EPOCH] ?: 0L
    }

    val extraQuotesFlow: Flow<List<MotivationQuote>> = context.civilsDataStore.data.map { prefs ->
        val raw = prefs[Keys.EXTRA_QUOTES_JSON] ?: return@map emptyList()
        parseQuotesJson(raw)
    }

    val dailyFlashcardTargetFlow: Flow<Int> = context.civilsDataStore.data.map { prefs ->
        prefs[Keys.DAILY_FLASHCARD_TARGET]?.toIntOrNull()?.coerceIn(5, 100) ?: 15
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.civilsDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setLastSyncEpoch(epochMs: Long) {
        context.civilsDataStore.edit { it[Keys.LAST_SYNC_EPOCH] = epochMs }
    }

    suspend fun setDailyFlashcardTarget(target: Int) {
        context.civilsDataStore.edit { it[Keys.DAILY_FLASHCARD_TARGET] = target.coerceIn(5, 100).toString() }
    }

    suspend fun appendExtraQuotes(newQuotes: List<MotivationQuote>) {
        context.civilsDataStore.edit { prefs ->
            val existing = parseQuotesJson(prefs[Keys.EXTRA_QUOTES_JSON] ?: "[]")
            val combined = (existing + newQuotes).distinctBy { it.quoteId }
            val arr = JSONArray()
            combined.forEach { q ->
                arr.put(
                    JSONObject().apply {
                        put("quoteId", q.quoteId)
                        put("member", q.member.name)
                        put("text", q.text)
                        put("labelType", q.labelType.name)
                        put("sourceNote", q.sourceNote)
                        put("contextTag", q.contextTag)
                    }
                )
            }
            prefs[Keys.EXTRA_QUOTES_JSON] = arr.toString()
        }
    }

    private fun parseQuotesJson(raw: String): List<MotivationQuote> = try {
        val arr = JSONArray(raw)
        (0 until arr.length()).mapNotNull { idx ->
            val obj = arr.optJSONObject(idx) ?: return@mapNotNull null
            MotivationQuote(
                quoteId = obj.optString("quoteId", "ai_q_$idx"),
                member = BtsMember.safeValueOf(obj.optString("member", "RM")),
                text = obj.optString("text", ""),
                labelType = if (obj.optString("labelType") == QuoteLabelType.VERIFIED_BTS_QUOTE.name) {
                    QuoteLabelType.VERIFIED_BTS_QUOTE
                } else {
                    QuoteLabelType.INSPIRED_BY_MEMBER
                },
                sourceNote = obj.optString("sourceNote", "Original study motivation"),
                contextTag = obj.optString("contextTag", "SESSION")
            ).takeIf { it.text.isNotBlank() }
        }
    } catch (_: Exception) {
        emptyList()
    }
}
