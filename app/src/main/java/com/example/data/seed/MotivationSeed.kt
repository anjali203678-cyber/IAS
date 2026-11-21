package com.example.data.seed

import com.example.data.model.BtsMember
import com.example.data.model.MotivationQuote
import com.example.data.model.QuoteHistoryEntry
import com.example.data.model.QuoteLabelType
import com.example.data.model.QuoteSlot

object MotivationSeed {

    private fun q(id: String, m: BtsMember, ctx: String, text: String) = MotivationQuote(
        quoteId = id,
        member = m,
        text = text,
        labelType = QuoteLabelType.INSPIRED_BY_MEMBER,
        sourceNote = "Original study motivation inspired by ${m.displayName} (${m.motivationTheme})",
        contextTag = ctx
    )

    // 140 unique original motivational messages (20 per member x 7 members)
    val initialQuotes: List<MotivationQuote> = listOf(
        // ==================== RM (Knowledge, leadership and learning) ====================
        q("rm_01", BtsMember.RM, "MORNING", "Every big exam is built from small quiet mornings. Open your first NCERT page today and let curiosity lead you."),
        q("rm_02", BtsMember.RM, "SESSION", "Do not rush past a tough paragraph—understanding one concept deeply today saves ten hours of confusion in 2029."),
        q("rm_03", BtsMember.RM, "CHAPTER_COMPLETE", "You just turned unfamiliar facts into real knowledge. Step by step, you are becoming the leader you aim to be."),
        q("rm_04", BtsMember.RM, "EVENING", "Before you rest tonight, reflect on one new idea you truly understood today—that quiet growth is priceless."),
        q("rm_05", BtsMember.RM, "BREAK", "Even the clearest mind needs fresh air. Step away for a few minutes and return with steady clarity."),
        q("rm_06", BtsMember.RM, "TEST_RESULT", "Every wrong MCQ is simply a signpost pointing to what deserves your attention next. Read the explanation calmly."),
        q("rm_07", BtsMember.RM, "GOAL_COMPLETE", "Nine hours of honest study completed! Consistency turns a mountain into walkable steps."),
        q("rm_08", BtsMember.RM, "MORNING", "Read like a thinker, not just a memorizer: ask 'why' behind every historical event and constitutional rule."),
        q("rm_09", BtsMember.RM, "SESSION", "Learning English terms alongside Hindi concepts is building a bridge—walk across it one word at a time."),
        q("rm_10", BtsMember.RM, "SESSION", "Great administrators listen carefully before speaking. Practice that same patience when reading complex Mains topics."),
        q("rm_11", BtsMember.RM, "CHAPTER_COMPLETE", "Completing a chapter is like finishing a thoughtful book—now lock its wisdom in with active recall."),
        q("rm_12", BtsMember.RM, "EVENING", "Do not compare your Chapter 1 to someone else's Chapter 20. Your December 2028 target belongs to your own steady pace."),
        q("rm_13", BtsMember.RM, "BREAK", "Give your thoughts space to breathe; knowledge settles best when the mind is calm."),
        q("rm_14", BtsMember.RM, "TEST_RESULT", "Scores fluctuate, but deep understanding stays. Review your weak concept and watch your accuracy climb."),
        q("rm_15", BtsMember.RM, "MORNING", "Today is a fresh blank notebook. Fill it with clear notes, clean maps, and calm focus."),
        q("rm_16", BtsMember.RM, "SESSION", "When History feels vast, remember: every era is simply human stories connected by cause and effect."),
        q("rm_17", BtsMember.RM, "GOAL_COMPLETE", "Today you kept a promise to your 2029 self. That quiet self-respect is true leadership."),
        q("rm_18", BtsMember.RM, "EVENING", "Articulate one concept in your own words tonight—if you can explain it simply, you own it."),
        q("rm_19", BtsMember.RM, "SESSION", "Mistakes in English practice are proof that you are brave enough to grow beyond your comfort zone."),
        q("rm_20", BtsMember.RM, "MORNING", "Start with clarity, stay curious, and let every NCERT line sharpen your perspective on India."),

        // ==================== JIN (Confidence and self-belief) ====================
        q("jin_01", BtsMember.JIN, "MORNING", "Smile at your desk this morning—you are capable of mastering every subject from the very basics."),
        q("jin_02", BtsMember.JIN, "SESSION", "Coming from a Science stream into History is your superpower: you bring fresh eyes and logical focus!"),
        q("jin_03", BtsMember.JIN, "TEST_RESULT", "One tricky test cannot define your potential. Dust it off, laugh off the fear, and try again stronger."),
        q("jin_04", BtsMember.JIN, "BREAK", "You have worked hard this block—drink water, stretch your shoulders, and give yourself credit."),
        q("jin_05", BtsMember.JIN, "CHAPTER_COMPLETE", "Look at you finishing another milestone! Confidence comes from keeping small promises like this."),
        q("jin_06", BtsMember.JIN, "EVENING", "Be proud of every minute you showed up today, even the moments that felt slow."),
        q("jin_07", BtsMember.JIN, "GOAL_COMPLETE", "Full 9-hour goal achieved! Stand tall tonight knowing you gave your preparation your best."),
        q("jin_08", BtsMember.JIN, "MORNING", "Speak your English practice sentences aloud with pride today—clarity grows with confidence."),
        q("jin_09", BtsMember.JIN, "SESSION", "Never feel embarrassed to learn basic concepts slowly. Every IAS topper once opened Class 6 NCERT for the first time."),
        q("jin_10", BtsMember.JIN, "TEST_RESULT", "Celebrate every right answer and treat every missed question as free training before the real 2029 exam."),
        q("jin_11", BtsMember.JIN, "BREAK", "A calm smile resets your energy faster than stress ever could. Take your break guilt-free."),
        q("jin_12", BtsMember.JIN, "EVENING", "Trust the process you built today. Even quiet progress shines brightly over months."),
        q("jin_13", BtsMember.JIN, "MORNING", "You do not need to know everything today—you only need to trust yourself enough to start."),
        q("jin_14", BtsMember.JIN, "SESSION", "If a date or term slips your mind, don't scold yourself. Use a memory story and smile—you've got this."),
        q("jin_15", BtsMember.JIN, "CHAPTER_COMPLETE", "Another chapter unlocked! Keep shining with steady self-belief."),
        q("jin_16", BtsMember.JIN, "GOAL_COMPLETE", "You proved today that discipline and warmth can coexist. Rest well, future officer!"),
        q("jin_17", BtsMember.JIN, "SESSION", "When writing a Mains answer, believe your thoughts matter—structure them clearly and write boldly."),
        q("jin_18", BtsMember.JIN, "BREAK", "Rest is part of winning, not a delay. Recharge and come back bright."),
        q("jin_19", BtsMember.JIN, "EVENING", "Tomorrow is another chance to surprise yourself with how much you remember."),
        q("jin_20", BtsMember.JIN, "MORNING", "Hold your head high: 2029 is built on the confidence you practice right here at your study table."),

        // ==================== SUGA (Discipline and hard work) ====================
        q("suga_01", BtsMember.SUGA, "MORNING", "Motivation wakes you up, but silent daily routine carries you to LBSNAA. Begin your first block on time."),
        q("suga_02", BtsMember.SUGA, "SESSION", "No shortcuts—read the NCERT carefully, revise on schedule, and trust raw, unglamorous hard work."),
        q("suga_03", BtsMember.SUGA, "BREAK", "Take the exact 15-minute break, then sit back down immediately. Discipline is built in transitions."),
        q("suga_04", BtsMember.SUGA, "TEST_RESULT", "Don't look away from weak areas—fix them today so they cannot hurt you in the Prelims hall."),
        q("suga_05", BtsMember.SUGA, "CHAPTER_COMPLETE", "Good. One chapter done properly. Log your revision dates and keep the streak unbroken."),
        q("suga_06", BtsMember.SUGA, "EVENING", "Quiet hours when nobody is watching are what separate serious aspirants from wishful dreamers."),
        q("suga_07", BtsMember.SUGA, "GOAL_COMPLETE", "Nine real hours logged. Zero excuses, pure effort—that is how big dreams become reality."),
        q("suga_08", BtsMember.SUGA, "MORNING", "Protect your morning focus from distractions. First two blocks set the tone for the entire day."),
        q("suga_09", BtsMember.SUGA, "SESSION", "Repetition is not boring—it is how champions make difficult facts automatic."),
        q("suga_10", BtsMember.SUGA, "TEST_RESULT", "Accuracy matters more than speed right now. Understand why the wrong options failed."),
        q("suga_11", BtsMember.SUGA, "BREAK", "Stretch, rest your eyes, keep your phone away, and return sharp."),
        q("suga_12", BtsMember.SUGA, "EVENING", "Even on low-energy days, showing up for one honest hour keeps your momentum alive."),
        q("suga_13", BtsMember.SUGA, "MORNING", "December 2028 syllabus completion is simple math: win today's blocks one by one."),
        q("suga_14", BtsMember.SUGA, "SESSION", "If memorizing dates feels tough, write a timeline three times. Effort beats talent when talent gets lazy."),
        q("suga_15", BtsMember.SUGA, "CHAPTER_COMPLETE", "Done well—now schedule Day 1, Day 3, and Day 7 revisions so the effort isn't wasted."),
        q("suga_16", BtsMember.SUGA, "GOAL_COMPLETE", "Another brick laid in your IAS foundation. Sleep deeply so your memory consolidates."),
        q("suga_17", BtsMember.SUGA, "SESSION", "Focus on the current 45-minute block only. Don't worry about 2029 during a single study sprint."),
        q("suga_18", BtsMember.SUGA, "BREAK", "Rest cleanly so your next study block is 100% focused."),
        q("suga_19", BtsMember.SUGA, "EVENING", "Check your pending tasks honestly and adjust tomorrow without self-blame."),
        q("suga_20", BtsMember.SUGA, "MORNING", "Steady, quiet, relentless—that is your study rhythm today."),

        // ==================== J-HOPE (Positivity and hope) ====================
        q("jhope_01", BtsMember.JHOPE, "MORNING", "Good morning sunshine! Today's 9 hours are 9 bright chances to grow closer to your IAS dream!"),
        q("jhope_02", BtsMember.JHOPE, "SESSION", "Bring positive energy to even the driest Polity or Economics topic—curiosity makes everything lighter!"),
        q("jhope_03", BtsMember.JHOPE, "BREAK", "Take a cheerful stretch break, shake off tension, and come back smiling for the next subject!"),
        q("jhope_04", BtsMember.JHOPE, "TEST_RESULT", "Yay for trying! Even a tough mock test just showed you how to earn extra marks next time!"),
        q("jhope_05", BtsMember.JHOPE, "CHAPTER_COMPLETE", "Woohoo! Chapter complete! Celebrate this bright star on your Purple Dream journey!"),
        q("jhope_06", BtsMember.JHOPE, "EVENING", "Look back at today with gratitude—every flashcard flipped and every English sentence spoken counts!"),
        q("jhope_07", BtsMember.JHOPE, "GOAL_COMPLETE", "You lit up your entire 9-hour plan today! Hope and hard work make an unstoppable team!"),
        q("jhope_08", BtsMember.JHOPE, "MORNING", "Start with a warm smile and a hopeful heart—today is full of good learning ahead."),
        q("jhope_09", BtsMember.JHOPE, "SESSION", "Turn your English 45-minute session into a joyful conversation—progress sounds wonderful!"),
        q("jhope_10", BtsMember.JHOPE, "TEST_RESULT", "Keep your spirit bright! Every practice quiz builds exam-hall calmness."),
        q("jhope_11", BtsMember.JHOPE, "BREAK", "Sip water, look at the sky for a minute, and bring fresh sunshine back to your desk."),
        q("jhope_12", BtsMember.JHOPE, "EVENING", "Even if a block ran late, tomorrow brings a brand-new sunrise and fresh energy."),
        q("jhope_13", BtsMember.JHOPE, "MORNING", "Your positive mindset is the secret fuel that carries you through long syllabus months."),
        q("jhope_14", BtsMember.JHOPE, "SESSION", "Love the process of discovering India's geography, heritage, and constitution!"),
        q("jhope_15", BtsMember.JHOPE, "CHAPTER_COMPLETE", "Sparkle earned! Keep dancing forward toward December 2028!"),
        q("jhope_16", BtsMember.JHOPE, "GOAL_COMPLETE", "What a radiant study day! Give yourself a joyful pat on the back."),
        q("jhope_17", BtsMember.JHOPE, "SESSION", "Stuck on a hard fact? Smile, make a funny acronym, and turn difficulty into fun!"),
        q("jhope_18", BtsMember.JHOPE, "BREAK", "Happy breaks make sharper study sessions—enjoy your recharge!"),
        q("jhope_19", BtsMember.JHOPE, "EVENING", "End your day with hope: your future self is cheering for every page you read tonight."),
        q("jhope_20", BtsMember.JHOPE, "MORNING", "Shine bright today—your hope can carry you across every challenge!"),

        // ==================== JIMIN (Patience and self-love) ====================
        q("jimin_01", BtsMember.JIMIN, "MORNING", "Be gentle and patient with yourself today. Real mastery blooms softly when you stay consistent."),
        q("jimin_02", BtsMember.JIMIN, "SESSION", "If remembering facts feels hard, don't be harsh on yourself—revise kindly with flashcards and stories."),
        q("jimin_03", BtsMember.JIMIN, "BREAK", "Take care of yourself during this break—your health and peace matter as much as your notes."),
        q("jimin_04", BtsMember.JIMIN, "TEST_RESULT", "Please don't get discouraged by low marks today. Every flower needs water and patience before blooming."),
        q("jimin_05", BtsMember.JIMIN, "CHAPTER_COMPLETE", "You did so well finishing this chapter! Pause for a moment and appreciate your patience."),
        q("jimin_06", BtsMember.JIMIN, "EVENING", "Forgive any missed minutes today, cherish what you learned, and rest with a peaceful heart."),
        q("jimin_07", BtsMember.JIMIN, "GOAL_COMPLETE", "Nine hours completed with care and grace! You deserve deep rest and warm pride tonight."),
        q("jimin_08", BtsMember.JIMIN, "MORNING", "You have plenty of time until 2029—build a loving, steady study rhythm that lasts."),
        q("jimin_09", BtsMember.JIMIN, "SESSION", "Read Hindi explanations slowly until they feel comfortable, then welcome each English term gently."),
        q("jimin_10", BtsMember.JIMIN, "TEST_RESULT", "Weak topics are not failures; they are places asking for a little extra care."),
        q("jimin_11", BtsMember.JIMIN, "BREAK", "Breathe deeply, relax your shoulders, and remind yourself why you started this IAS dream."),
        q("jimin_12", BtsMember.JIMIN, "EVENING", "Your effort today—even the small unseen steps—is enough to make tomorrow easier."),
        q("jimin_13", BtsMember.JIMIN, "MORNING", "Treat your study table as a safe garden where your knowledge grows day by day."),
        q("jimin_14", BtsMember.JIMIN, "SESSION", "When revision feels repetitive, remember it's self-care for your future exam confidence."),
        q("jimin_15", BtsMember.JIMIN, "CHAPTER_COMPLETE", "A new blossom in your Memory Garden! Keep nurturing your dreams patiently."),
        q("jimin_16", BtsMember.JIMIN, "GOAL_COMPLETE", "You cared for your goals all day long. Sleep peacefully under the purple stars."),
        q("jimin_17", BtsMember.JIMIN, "SESSION", "Take your time practicing English pronunciation—patience turns hesitation into fluency."),
        q("jimin_18", BtsMember.JIMIN, "BREAK", "Warm water, deep breath, gentle smile—you are doing wonderfully."),
        q("jimin_19", BtsMember.JIMIN, "EVENING", "No need to rush; steady love for learning carries you all the way."),
        q("jimin_20", BtsMember.JIMIN, "MORNING", "Start softly, stay steady, and believe in your gentle strength today."),

        // ==================== V (Creativity and individuality) ====================
        q("v_01", BtsMember.V, "MORNING", "Paint your notes with imagination today—draw mind maps, timelines, and vivid mental scenes!"),
        q("v_02", BtsMember.V, "SESSION", "Turn dry historical dates into movie scenes in your mind; visual stories never fade easily."),
        q("v_03", BtsMember.V, "BREAK", "Look out the window at the clouds or sky—creative minds refresh best with quiet daydreaming."),
        q("v_04", BtsMember.V, "TEST_RESULT", "See every question from a fresh angle! Elimination techniques are an art form in Prelims."),
        q("v_05", BtsMember.V, "CHAPTER_COMPLETE", "You crafted a whole mental canvas of this chapter—unique, vivid, and unforgettable!"),
        q("v_06", BtsMember.V, "EVENING", "Your individual study style—visuals, stories, bilingual notes—is your greatest strength."),
        q("v_07", BtsMember.V, "GOAL_COMPLETE", "Nine hours of inspired, creative learning complete! Your mind is a starry gallery tonight."),
        q("v_08", BtsMember.V, "MORNING", "Think uniquely when approaching Essays and Mains—original examples make answers shine."),
        q("v_09", BtsMember.V, "SESSION", "Picture India's rivers and mountain passes like brushstrokes on a map—you'll remember every location!"),
        q("v_10", BtsMember.V, "TEST_RESULT", "Notice the subtle patterns behind UPSC statements—curiosity spots traps before they catch you."),
        q("v_11", BtsMember.V, "BREAK", "Give your imagination a calm pause; art and memory grow in quiet moments."),
        q("v_12", BtsMember.V, "EVENING", "Sketch one quick mind map from memory before bed—let your mind play with ideas."),
        q("v_13", BtsMember.V, "MORNING", "Your journey to IAS doesn't have to copy anyone else's—make your study space uniquely yours."),
        q("v_14", BtsMember.V, "SESSION", "Connect Art & Culture sculptures with the emotions of their creators—history comes alive!"),
        q("v_15", BtsMember.V, "CHAPTER_COMPLETE", "Another vibrant chapter painted in your mind—keep creating your own path!"),
        q("v_16", BtsMember.V, "GOAL_COMPLETE", "What a masterpiece of a study day! Rest under the crescent moon."),
        q("v_17", BtsMember.V, "SESSION", "Use colors, symbols, and stories whenever facts look identical—make them unforgettable."),
        q("v_18", BtsMember.V, "BREAK", "Step back for a fresh perspective; you'll see the big picture clearly when you return."),
        q("v_19", BtsMember.V, "EVENING", "Dream boldly tonight—purple skies hold room for every creative ambition."),
        q("v_20", BtsMember.V, "MORNING", "Bring beauty and wonder into your morning NCERT reading today."),

        // ==================== JUNGKOOK (Dedication and continuous improvement) ====================
        q("jk_01", BtsMember.JUNGKOOK, "MORNING", "Aim to be 1% sharper today than yesterday! Lace up your focus and dive into your first study sprint."),
        q("jk_02", BtsMember.JUNGKOOK, "SESSION", "Don't stop when a topic feels hard—challenge yourself to conquer it one practice question at a time!"),
        q("jk_03", BtsMember.JUNGKOOK, "BREAK", "Hydrate and reset like an athlete between rounds—the next study block is where you level up!"),
        q("jk_04", BtsMember.JUNGKOOK, "TEST_RESULT", "Treat every test score as your new personal benchmark—next attempt, beat your own record!"),
        q("jk_05", BtsMember.JUNGKOOK, "CHAPTER_COMPLETE", "Chapter cleared with full dedication! Now challenge yourself with the 70-question bank!"),
        q("jk_06", BtsMember.JUNGKOOK, "EVENING", "Every hour you pushed through resistance today made your willpower stronger for 2029."),
        q("jk_07", BtsMember.JUNGKOOK, "GOAL_COMPLETE", "9/9 Hours conquered! That golden dedication is how ordinary days build extraordinary results!"),
        q("jk_08", BtsMember.JUNGKOOK, "MORNING", "Give 100% presence to whatever task is right in front of you—no half-hearted scrolling."),
        q("jk_09", BtsMember.JUNGKOOK, "SESSION", "Practice your 45-minute English speaking tirelessly—daily reps build unstoppable fluency!"),
        q("jk_10", BtsMember.JUNGKOOK, "TEST_RESULT", "Check your time per question and accuracy—small daily optimizations create huge score jumps."),
        q("jk_11", BtsMember.JUNGKOOK, "BREAK", "Quick active break, deep breath, and back into peak focus mode!"),
        q("jk_12", BtsMember.JUNGKOOK, "EVENING", "Review your difficult flashcards tonight—turning weaknesses into strengths is the real thrill!"),
        q("jk_13", BtsMember.JUNGKOOK, "MORNING", "Continuous improvement beats overnight cramming every single time. Let's win today!"),
        q("jk_14", BtsMember.JUNGKOOK, "SESSION", "Push through the middle of the study session when energy dips—that's where real growth lives."),
        q("jk_15", BtsMember.JUNGKOOK, "CHAPTER_COMPLETE", "Level up complete! Keep collecting knowledge stars every week until December 2028!"),
        q("jk_16", BtsMember.JUNGKOOK, "GOAL_COMPLETE", "Unstoppable dedication today! Rest like a champion and wake up ready to climb higher."),
        q("jk_17", BtsMember.JUNGKOOK, "SESSION", "Even if you start as an absolute beginner, relentless practice turns beginners into masters."),
        q("jk_18", BtsMember.JUNGKOOK, "BREAK", "Recharge fast and strong—you're doing amazing work today."),
        q("jk_19", BtsMember.JUNGKOOK, "EVENING", "End the day proud that you gave honest effort from first bell to final review."),
        q("jk_20", BtsMember.JUNGKOOK, "MORNING", "Today's dedication is tomorrow's rank—let's give it our best!")
    )
}

object QuoteSelectionEngine {

    data class SelectionResult(
        val quote: MotivationQuote,
        val collectionExhausted: Boolean,
        val remainingUnseenCount: Int
    )

    /**
     * Selects a non-repeating quote while:
     * 1. Never repeating any quoteId in [history] until all quotes in [pool] are exhausted.
     * 2. Preventing the same quote or same member from repeating between MORNING & EVENING on [todayDate].
     * 3. Rotating fairly across all 7 BTS members (preferring least-shown members).
     * 4. Matching [preferredContext] when unseen candidates exist.
     */
    fun selectNextQuote(
        pool: List<MotivationQuote>,
        history: List<QuoteHistoryEntry>,
        todayDate: String,
        slot: QuoteSlot,
        preferredContext: String? = null,
        preferredMember: BtsMember? = null
    ): SelectionResult {
        if (pool.isEmpty()) {
            val fallback = MotivationSeed.initialQuotes.first()
            return SelectionResult(fallback, collectionExhausted = true, remainingUnseenCount = 0)
        }

        val seenIds = history.map { it.quoteId }.toSet()
        var unseen = pool.filter { it.quoteId !in seenIds }
        val exhausted = unseen.isEmpty()
        if (exhausted) {
            unseen = pool
        }

        // Same-day morning/evening guard
        val todayEntries = history.filter { it.displayedDate == todayDate }
        val todayQuoteIds = todayEntries.map { it.quoteId }.toSet()
        val sameDayDifferentSlotMembers = todayEntries
            .filter {
                (slot == QuoteSlot.EVENING && it.slot == QuoteSlot.MORNING.name) ||
                    (slot == QuoteSlot.MORNING && it.slot == QuoteSlot.EVENING.name)
            }
            .map { it.member }
            .toSet()

        val notShownToday = unseen.filter { it.quoteId !in todayQuoteIds }
        var candidates = if (notShownToday.isNotEmpty()) notShownToday else unseen

        // Avoid same member morning & evening on the same day if possible
        val crossSlotFiltered = candidates.filter { it.member.name !in sameDayDifferentSlotMembers }
        if (crossSlotFiltered.isNotEmpty()) {
            candidates = crossSlotFiltered
        }

        // Apply preferred member if requested
        if (preferredMember != null) {
            val memberMatches = candidates.filter { it.member == preferredMember }
            if (memberMatches.isNotEmpty()) {
                candidates = memberMatches
            }
        } else {
            // Fair rotation across all 7 members: pick from members with fewest appearances in history
            val countsByMember = BtsMember.entries.associateWith { m ->
                history.count { it.member == m.name }
            }
            val minCount = candidates.minOf { countsByMember[it.member] ?: 0 }
            val fairlyRotated = candidates.filter { (countsByMember[it.member] ?: 0) == minCount }
            if (fairlyRotated.isNotEmpty()) {
                candidates = fairlyRotated
            }
        }

        // Apply context tag preference if specified
        if (!preferredContext.isNullOrBlank()) {
            val contextMatches = candidates.filter { it.contextTag.equals(preferredContext, ignoreCase = true) }
            if (contextMatches.isNotEmpty()) {
                candidates = contextMatches
            }
        }

        val chosen = candidates.random()
        val remainingAfter = (unseen.size - 1).coerceAtLeast(0)
        return SelectionResult(
            quote = chosen,
            collectionExhausted = exhausted,
            remainingUnseenCount = remainingAfter
        )
    }

    fun memberOfTheDay(todayDate: String): BtsMember {
        val hash = todayDate.fold(0) { acc, c -> (acc * 31 + c.code) and 0x7fffffff }
        return BtsMember.entries[hash % BtsMember.entries.size]
    }
}
