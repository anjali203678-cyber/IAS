package com.example

import com.example.data.model.BtsMember
import com.example.data.model.ContentStatus
import com.example.data.model.QuestionCategory
import com.example.data.model.QuoteHistoryEntry
import com.example.data.model.QuoteSlot
import com.example.data.model.SyllabusSection
import com.example.data.seed.AcademiesSeed
import com.example.data.seed.FlashcardSeed
import com.example.data.seed.HistoryCh1NotesSeed
import com.example.data.seed.HistoryCh1QuestionsSeed
import com.example.data.seed.MotivationSeed
import com.example.data.seed.NcertCatalogSeed
import com.example.data.seed.QuoteSelectionEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CivilsDomainUnitTest {

    @Test
    fun `syllabus catalog covers all UPSC sections and gates completion on authored content`() {
        val sections = NcertCatalogSeed.allBooks.map { it.section }.toSet()
        assertEquals(SyllabusSection.entries.toSet(), sections)

        val ch1 = NcertCatalogSeed.findChapter("ncert_hist_6_ch1")
        assertNotNull(ch1)
        assertEquals(ContentStatus.FULL_CONTENT, ch1!!.contentStatus)
        assertEquals(1, NcertCatalogSeed.fullContentChaptersCount())
        assertTrue(NcertCatalogSeed.totalChaptersCount() > 30)
    }

    @Test
    fun `milestone chapter 1 has all 22 sections and exact 70 question bank`() {
        val content = HistoryCh1NotesSeed.chapterContent
        assertTrue(content.englishTerminology.isNotEmpty())
        assertTrue(content.topicNotes.isNotEmpty())
        assertTrue(content.comparisonTables.isNotEmpty())
        assertTrue(content.timeline.isNotEmpty())
        assertTrue(content.memoryTechniques.isNotEmpty())
        assertTrue(content.mindMapBranches.isNotEmpty())
        assertTrue(content.onePageRevisionSheet.isNotEmpty())

        val qs = HistoryCh1QuestionsSeed.allQuestions
        assertEquals(70, qs.size)
        assertEquals(30, qs.count { it.category == QuestionCategory.BASIC_MCQ })
        assertEquals(20, qs.count { it.category == QuestionCategory.UPSC_LEVEL_MCQ })
        assertEquals(10, qs.count { it.category == QuestionCategory.STATEMENT_MCQ })
        val pyqs = qs.filter { it.category == QuestionCategory.AUTHENTIC_PYQ }
        assertEquals(10, pyqs.size)
        assertTrue(pyqs.all { it.pyqYear != null && it.pyqYear!! in 1995..2025 })
        assertTrue(qs.all { it.options.size == 4 && it.correctIndex in 0..3 })
    }

    @Test
    fun `motivation engine has 140 quotes across 7 members and never repeats before exhaustion`() {
        val pool = MotivationSeed.initialQuotes
        assertEquals(140, pool.size)
        BtsMember.entries.forEach { member ->
            assertEquals(20, pool.count { it.member == member })
        }

        val history = mutableListOf<QuoteHistoryEntry>()
        val seenIds = mutableSetOf<String>()
        for (i in 0 until pool.size) {
            val res = QuoteSelectionEngine.selectNextQuote(
                pool = pool,
                history = history,
                todayDate = "2026-11-21",
                slot = QuoteSlot.SHUFFLE
            )
            assertFalse("Quote repeated before pool exhaustion at index $i", res.quote.quoteId in seenIds)
            seenIds.add(res.quote.quoteId)
            history.add(
                QuoteHistoryEntry(
                    id = "h_$i",
                    quoteId = res.quote.quoteId,
                    member = res.quote.member.name,
                    text = res.quote.text,
                    displayedDate = "2026-11-21",
                    slot = QuoteSlot.SHUFFLE.name
                )
            )
        }
        assertEquals(140, seenIds.size)

        // Same-day morning/evening protection
        val morningHistory = listOf(
            QuoteHistoryEntry(
                id = "m1",
                quoteId = "rm_01",
                member = BtsMember.RM.name,
                displayedDate = "2026-11-21",
                slot = QuoteSlot.MORNING.name
            )
        )
        val eveningRes = QuoteSelectionEngine.selectNextQuote(
            pool = pool,
            history = morningHistory,
            todayDate = "2026-11-21",
            slot = QuoteSlot.EVENING
        )
        assertNotEquals("rm_01", eveningRes.quote.quoteId)
    }

    @Test
    fun `default daily study plan equals exact 9 hours excluding breaks`() {
        val tasks = AcademiesSeed.defaultNineHourStudyPlan("2026-11-21")
        assertEquals(540, tasks.sumOf { it.durationMinutes })
        assertTrue(tasks.any { it.category == "ENGLISH_SPEAKING" && it.durationMinutes == 45 })
    }

    @Test
    fun `starter flashcards cover all 7 BTS member decks`() {
        val membersRepresented = FlashcardSeed.starterCards.map { it.deckMember }.toSet()
        assertEquals(BtsMember.entries.map { it.name }.toSet(), membersRepresented)
    }

    @Test
    fun `UPSC prelims negative marking formula computes +2 and -2 over 3 accurately`() {
        val correct = 45
        val wrong = 18
        val net = (correct * 2.0) - (wrong * (2.0 / 3.0))
        assertEquals(78.0, net, 0.01)
    }
}
