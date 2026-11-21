package com.example.data.seed

import com.example.data.model.BtsMember
import com.example.data.model.FlashcardDifficulty
import com.example.data.model.FlashcardItem
import com.example.data.model.MasteryStatus

object FlashcardSeed {

    private fun card(
        id: String,
        member: BtsMember,
        subject: String,
        chapterId: String,
        front: String,
        back: String,
        explanation: String,
        diff: FlashcardDifficulty,
        source: String
    ) = FlashcardItem(
        id = id,
        deckMember = member.name,
        subject = subject,
        chapterId = chapterId,
        front = front,
        back = back,
        explanation = explanation,
        difficulty = diff.name,
        sourceRef = source,
        masteryStatus = MasteryStatus.NEW.name,
        favourite = false,
        reviewCount = 0,
        intervalDays = 1,
        nextReviewEpochMs = 0L,
        custom = false
    )

    val starterCards: List<FlashcardItem> = HistoryCh1NotesSeed.chapterContent.starterFlashcards + listOf(
        // RM's Recall — Polity Articles & History Dates
        card("fc_rm_2", BtsMember.RM, "Polity", "ncert_pol_11_12_ch1",
            "Which Article of the Indian Constitution abolishes 'Untouchability'?",
            "Article 17 (Right to Equality)",
            "अस्पृश्यता का अंत (Abolition of Untouchability) अनुच्छेद 17 के तहत एक पूर्ण मौलिक अधिकार है।",
            FlashcardDifficulty.EASY, "Indian Constitution, Part III / NCERT Class 11"),
        card("fc_rm_3", BtsMember.RM, "History", "ncert_hist_6_ch7",
            "In which year did James Prinsep decipher Ashokan Brahmi and Kharosthi scripts?",
            "1837 CE",
            "1837 में जेम्स प्रिंसेप ने ब्राह्मी व खरोष्ठी को पढ़ा और 'देवानामपिय पियदस्सी' को अशोक के रूप में पहचाना।",
            FlashcardDifficulty.MEDIUM, "NCERT Class 12 Themes Part I / UPSC PYQ"),

        // Jin's Revision — Historical Personalities & Revisions
        card("fc_jin_2", BtsMember.JIN, "History", "ncert_hist_6_ch1",
            "Who composed the Arthashastra and served as advisor to Chandragupta Maurya?",
            "Kautilya (Chanakya / Vishnugupta)",
            "अर्थशास्त्र राजनीति, शासन कला और अर्थव्यवस्था पर प्रसिद्ध प्राचीन ग्रंथ है।",
            FlashcardDifficulty.EASY, "NCERT Class 6 Our Pasts I, Ch 7"),
        card("fc_jin_3", BtsMember.JIN, "Polity", "ncert_pol_11_12_ch1",
            "Which Constitutional Amendment added the words 'Socialist', 'Secular' and 'Integrity' to the Preamble?",
            "42nd Constitutional Amendment Act, 1976",
            "42वें संविधान संशोधन (1976) को 'मिनी संविधान' भी कहा जाता है।",
            FlashcardDifficulty.MEDIUM, "NCERT Class 11 Indian Constitution at Work"),

        // SUGA's Focus — Economics & UPSC Facts
        card("fc_suga_2", BtsMember.SUGA, "Economics", "ncert_eco_9_12_ch5",
            "What is the Repo Rate?",
            "The interest rate at which RBI lends short-term money to commercial banks against government securities.",
            "जब RBI को महंगाई नियंत्रित करनी होती है तो वह Repo Rate बढ़ाता है ताकि बाज़ार में नकदी कम हो।",
            FlashcardDifficulty.MEDIUM, "NCERT Class 12 Macroeconomics Ch 3"),
        card("fc_suga_3", BtsMember.SUGA, "History", "ncert_hist_6_ch1",
            "Distinguish Manuscript vs Inscription in one key line.",
            "Manuscript = hand-written on soft palm leaf/birch bark; Inscription = engraved on hard stone/metal.",
            "पांडुलिपि नाज़ुक होती है जबकि अभिलेख हज़ारों साल तक सुरक्षित रहते हैं।",
            FlashcardDifficulty.EASY, "NCERT Class 6 Our Pasts I, Ch 1, p. 4-5"),

        // j-hope's Hope Cards — English Vocabulary & Polity
        card("fc_jhope_1", BtsMember.JHOPE, "English Vocabulary", "upsc_other_areas_ch5",
            "What does 'Pragmatic' mean in UPSC Mains & Essay writing?",
            "व्यावहारिक / यथार्थवादी (Dealing with things sensibly and realistically based on practical outcomes).",
            "Example: 'India follows a pragmatic foreign policy balancing strategic autonomy with partnerships.'",
            FlashcardDifficulty.EASY, "English Academy Vocabulary Deck"),
        card("fc_jhope_2", BtsMember.JHOPE, "Environment", "ncert_bio_env_12_ch3",
            "What is Lindeman's 10% Law in an ecosystem?",
            "Only about 10% of energy at one trophic level is transferred to the next higher trophic level.",
            "शेष 90% ऊर्जा श्वसन और ऊष्मा के रूप में खर्च हो जाती है—यही कारण है कि खाद्य श्रृंखला छोटी होती है।",
            FlashcardDifficulty.MEDIUM, "NCERT Class 12 Biology Ch 14"),

        // Jimin's Memory Garden — Geography & Art & Culture
        card("fc_jimin_1", BtsMember.JIMIN, "Geography", "ncert_geo_6_10_ch5",
            "Where are the Garo, Khasi, and Jaintia hills located?",
            "Meghalaya (North-East India)",
            "गारो पहाड़ियाँ प्राचीन कृषि क्षेत्रों (NCERT Class 6 Ch 1) और विश्व के सर्वाधिक वर्षा वाले क्षेत्रों (मावसिनराम) के लिए प्रसिद्ध हैं।",
            FlashcardDifficulty.EASY, "NCERT Class 6 History Ch 1 & Class 9 Geography"),
        card("fc_jimin_2", BtsMember.JIMIN, "History", "ncert_hist_6_ch1",
            "Which Himalayan tree's bark was specially prepared to write ancient Indian manuscripts?",
            "Birch tree (भूर्ज वृक्ष / Bhojpatra)",
            "भोजपत्र की छाल पर संस्कृत, प्राकृत और अन्य प्राचीन ग्रंथ लिखे गए।",
            FlashcardDifficulty.EASY, "NCERT Class 6 Our Pasts I, Ch 1, p. 4"),

        // V's Visual Learning — Maps, Timelines & Locations
        card("fc_v_2", BtsMember.V, "Geography", "ncert_geo_11_12_ch2",
            "What is the ITCZ (Inter Tropical Convergence Zone)?",
            "A low-pressure belt near the equator where Northeast and Southeast trade winds converge.",
            "ग्रीष्म ऋतु में ITCZ के उत्तर (गंगा के मैदान) की ओर खिसकने से भारतीय दक्षिण-पश्चिम मानसून आकर्षित होता है।",
            FlashcardDifficulty.HARD, "NCERT Class 11 India: Physical Environment Ch 4"),
        card("fc_v_3", BtsMember.V, "History Optional", "hist_opt_p1_ch1",
            "What is 'Historiography' (इतिहासलेखन)?",
            "The study of how historians interpret, write, and debate the past using different schools of thought.",
            "मुख्य धाराएँ: प्राच्यवादी (Orientalist), राष्ट्रवादी (Nationalist), मार्क्सवादी (Marxist) और सबाल्टर्न (Subaltern)।",
            FlashcardDifficulty.MEDIUM, "UPSC History Optional Paper I"),

        // Jungkook's Challenge Cards — Current Affairs & High-Yield UPSC Facts
        card("fc_jk_2", BtsMember.JUNGKOOK, "Current Affairs", "upsc_other_areas_ch1",
            "What is the difference between 'GDP' and 'GNP'?",
            "GDP measures production within domestic territory; GNP = GDP + Net Factor Income from Abroad (NFIA).",
            "सरल सूत्र: GDP में 'भूगोल' (देश की सीमा) प्रमुख है, जबकि GNP में 'नागरिकता/निवासी' प्रमुख है।",
            FlashcardDifficulty.MEDIUM, "NCERT Class 12 Macroeconomics Ch 2"),
        card("fc_jk_3", BtsMember.JUNGKOOK, "Polity", "ncert_pol_11_12_ch3",
            "Which Constitutional Amendments gave constitutional status to Panchayats and Municipalities?",
            "73rd Amendment (Panchayats, Part IX) & 74th Amendment (Municipalities, Part IXA), 1992.",
            "11वीं अनुसूची में पंचायतों के 29 विषय और 12वीं अनुसूची में नगरपालिकाओं के 18 विषय शामिल हैं।",
            FlashcardDifficulty.EASY, "NCERT Class 11 Indian Constitution at Work Ch 8")
    )
}
