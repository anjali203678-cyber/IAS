package com.example.data.model

enum class SyllabusSection(val label: String, val labelHi: String) {
    NCERT_FOUNDATION("A. NCERT Foundation (Class 6–12)", "A. एनसीईआरटी आधार (कक्षा 6–12)"),
    UPSC_PRELIMS("B1. UPSC Prelims (GS I & CSAT II)", "B1. प्रारंभिक परीक्षा (GS I एवं CSAT II)"),
    UPSC_MAINS("B2. UPSC Mains (GS I–IV, Essay & Language)", "B2. मुख्य परीक्षा (GS I–IV, निबंध एवं भाषा)"),
    HISTORY_OPTIONAL("C. History Optional (Paper I & II)", "C. इतिहास वैकल्पिक विषय (पेपर I एवं II)"),
    OTHER_AREAS("D. Current Affairs, IR, Security & Ethics", "D. समसामयिकी, अंतर्राष्ट्रीय संबंध एवं नीतिशास्त्र")
}

enum class ContentStatus {
    FULL_CONTENT,       // All 22 sections + Memory Academy + Question Bank complete
    OUTLINE_AND_SYLLABUS // Official syllabus & chapter entry mapped; detailed notes pending
}

data class BookCatalogItem(
    val bookId: String,
    val section: SyllabusSection,
    val subject: String,
    val classOrPaper: String,
    val title: String,
    val titleHi: String,
    val sourceInfo: String,
    val chapters: List<ChapterCatalogItem>
)

data class ChapterCatalogItem(
    val chapterId: String,
    val bookId: String,
    val chapterNumber: Int,
    val title: String,
    val titleHi: String,
    val keyFocus: String,
    val contentStatus: ContentStatus
)

data class EnglishTermItem(
    val englishTerm: String,
    val hindiMeaning: String,
    val simpleExplanationHi: String,
    val examUsage: String
)

data class TopicNoteBlock(
    val headingHi: String,
    val headingEn: String,
    val explanationHi: String,
    val keyBullets: List<String>
)

data class KeyFactRow(
    val label: String,
    val detailHi: String,
    val yearOrPlace: String
)

data class ComparisonTableData(
    val title: String,
    val headers: List<String>,
    val rows: List<List<String>>
)

data class TimelineEntry(
    val period: String,
    val eventHi: String,
    val eventEn: String
)

data class ConfusedConceptItem(
    val pairTitle: String,
    val sideA: String,
    val sideB: String,
    val memoryTipHi: String
)

data class MemoryTechniqueItem(
    val techniqueType: String, // Mnemonic, Acronym, Story, Visual Map, Chronology
    val title: String,
    val contentHi: String,
    val visualCue: String
)

data class MindMapNode(
    val branchTitle: String,
    val children: List<String>
)

enum class QuestionCategory(val label: String) {
    BASIC_MCQ("Basic Concept MCQ"),
    UPSC_LEVEL_MCQ("UPSC Prelims Level MCQ"),
    STATEMENT_MCQ("Statement-Based MCQ"),
    AUTHENTIC_PYQ("Authentic UPSC PYQ")
}

data class ChapterQuestion(
    val questionId: String,
    val chapterId: String,
    val category: QuestionCategory,
    val questionText: String,
    val questionTextHi: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationHi: String,
    val incorrectOptionsReason: List<String>,
    val relatedConcept: String,
    val sourceRef: String,
    val pyqYear: Int? = null // Non-null only for verified authentic PYQs
)

data class DetailedChapterContent(
    val chapterId: String,
    val bookTitle: String,
    val chapterTitleEn: String,
    val chapterTitleHi: String,
    val introHi: String,
    val beginnerExplanationHi: String,
    val englishTerminology: List<EnglishTermItem>,
    val topicNotes: List<TopicNoteBlock>,
    val definitions: List<Pair<String, String>>,
    val keyFactsAndPersonalities: List<KeyFactRow>,
    val comparisonTables: List<ComparisonTableData>,
    val timeline: List<TimelineEntry>,
    val mapLocations: List<Pair<String, String>>,
    val prelimsPointers: List<String>,
    val mainsAnalyticalPoints: List<String>,
    val examplesAndCaseStudies: List<String>,
    val importantExamPoints: List<String>,
    val confusedConcepts: List<ConfusedConceptItem>,
    val chapterSummaryHi: String,
    val onePageRevisionSheet: List<String>,
    val memoryTechniques: List<MemoryTechniqueItem>,
    val mindMapRoot: String,
    val mindMapBranches: List<MindMapNode>,
    val starterFlashcards: List<FlashcardItem>,
    val questions: List<ChapterQuestion>,
    val sourceReferences: List<String>,
    val pyqAvailabilityNote: String
)
