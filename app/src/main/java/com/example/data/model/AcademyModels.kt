package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.MemberJhopeColor
import com.example.ui.theme.MemberJiminColor
import com.example.ui.theme.MemberJinColor
import com.example.ui.theme.MemberJungkookColor
import com.example.ui.theme.MemberRmColor
import com.example.ui.theme.MemberSugaColor
import com.example.ui.theme.MemberVColor

enum class BtsMember(
    val displayName: String,
    val motivationTheme: String,
    val deckName: String,
    val accentColor: Color,
    val emblemSymbol: String
) {
    RM(
        displayName = "RM",
        motivationTheme = "Knowledge, leadership and learning",
        deckName = "RM's Recall",
        accentColor = MemberRmColor,
        emblemSymbol = "📖"
    ),
    JIN(
        displayName = "Jin",
        motivationTheme = "Confidence and self-belief",
        deckName = "Jin's Revision",
        accentColor = MemberJinColor,
        emblemSymbol = "🌙"
    ),
    SUGA(
        displayName = "SUGA",
        motivationTheme = "Discipline and hard work",
        deckName = "SUGA's Focus",
        accentColor = MemberSugaColor,
        emblemSymbol = "🎹"
    ),
    JHOPE(
        displayName = "j-hope",
        motivationTheme = "Positivity and hope",
        deckName = "j-hope's Hope Cards",
        accentColor = MemberJhopeColor,
        emblemSymbol = "☀️"
    ),
    JIMIN(
        displayName = "Jimin",
        motivationTheme = "Patience and self-love",
        deckName = "Jimin's Memory Garden",
        accentColor = MemberJiminColor,
        emblemSymbol = "🌸"
    ),
    V(
        displayName = "V",
        motivationTheme = "Creativity and individuality",
        deckName = "V's Visual Learning",
        accentColor = MemberVColor,
        emblemSymbol = "🎨"
    ),
    JUNGKOOK(
        displayName = "Jungkook",
        motivationTheme = "Dedication and continuous improvement",
        deckName = "Jungkook's Challenge Cards",
        accentColor = MemberJungkookColor,
        emblemSymbol = "⭐"
    );

    companion object {
        fun safeValueOf(raw: String?): BtsMember =
            entries.firstOrNull {
                it.name.equals(raw, ignoreCase = true) ||
                    it.displayName.equals(raw, ignoreCase = true)
            } ?: RM
    }
}

enum class QuoteLabelType(val badgeText: String) {
    VERIFIED_BTS_QUOTE("Verified BTS Quote"),
    INSPIRED_BY_MEMBER("Original Motivation")
}

enum class QuoteSlot(val label: String) {
    MORNING("Morning Study"),
    EVENING("Evening Review"),
    SESSION("Study Session"),
    BREAK("Break Encouragement"),
    SHUFFLE("Shuffle"),
    CELEBRATION("Achievement Celebration")
}

data class MotivationQuote(
    val quoteId: String,
    val member: BtsMember,
    val text: String,
    val labelType: QuoteLabelType,
    val sourceNote: String,
    val contextTag: String
)

data class CurrentAffairsItem(
    val id: String,
    val title: String,
    val category: String,
    val publishedDate: String,
    val source: String,
    val whatHappened: String,
    val whyImportant: String,
    val background: String,
    val prelimsRelevance: String,
    val mainsRelevance: String,
    val syllabusTopic: String,
    val importantFacts: List<String>
)

data class MainsPromptItem(
    val id: String,
    val paper: String,
    val marks: Int,
    val wordLimit: Int,
    val question: String,
    val questionHi: String,
    val introTip: String,
    val bodyStructure: List<String>,
    val conclusionTip: String,
    val modelKeyPoints: List<String>
)

data class EssayPromptItem(
    val id: String,
    val year: Int?,
    val sourceLabel: String,
    val topic: String,
    val topicHi: String,
    val interpretationGuide: String,
    val dimensions: List<String>,
    val verifiedQuoteSuggestion: String
)

data class EnglishLessonItem(
    val id: String,
    val title: String,
    val focusArea: String,
    val durationMinutes: Int,
    val grammarRule: String,
    val hindiToEnglishDrills: List<Pair<String, String>>,
    val keyVocabulary: List<Triple<String, String, String>>,
    val speakingPrompt: String
)

data class InterviewQuestionItem(
    val id: String,
    val category: String,
    val question: String,
    val idealApproach: String,
    val pitfallsToAvoid: String
)

enum class ReminderCategory(
    val id: String,
    val title: String,
    val defaultHour: Int,
    val defaultMinute: Int,
    val messageBody: String
) {
    MORNING_START("MORNING_START", "Morning Study Start", 6, 0, "Good morning IAS Aspirant! Time to start your first NCERT Foundation block."),
    SUBJECT_CHANGE("SUBJECT_CHANGE", "Subject Change", 8, 30, "Time to switch subjects smoothly—fresh focus, zero fatigue."),
    BREAK_TIME("BREAK_TIME", "Break Time", 10, 0, "Take a mindful 15-minute break. Hydrate and rest your eyes!"),
    RETURN_FROM_BREAK("RETURN_FROM_BREAK", "Return from Break", 10, 15, "Break over! Return to your desk with Purple Dream focus."),
    ENGLISH_SPEAKING("ENGLISH_SPEAKING", "English Speaking Practice (45m)", 12, 30, "45 minutes of daily English speaking & translation practice starts now!"),
    DAILY_MCQ_TEST("DAILY_MCQ_TEST", "Daily MCQ Test", 16, 0, "Test your Prelims accuracy with today's MCQ & PYQ drill."),
    REVISION_SESSION("REVISION_SESSION", "Spaced Revision Session", 17, 30, "Complete your 1d/3d/7d/15d/30d spaced repetition flashcards."),
    CURRENT_AFFAIRS("CURRENT_AFFAIRS", "Current Affairs Digest", 19, 0, "Time for verified Current Affairs & PIB/IR analysis."),
    WEEKLY_TEST("WEEKLY_TEST", "Weekly Test Prep", 20, 0, "Review weak concepts for this week's Prelims & Mains mock."),
    PENDING_TASKS("PENDING_TASKS", "Pending Tasks Check", 21, 0, "Check any remaining study blocks from today's 9-hour plan."),
    BEDTIME_REVIEW("BEDTIME_REVIEW", "Bedtime Study Review", 22, 0, "Quick 10-minute recall before sleep—log your streak & stars!")
}
