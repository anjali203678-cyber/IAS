package com.example.data.seed

import com.example.data.model.CurrentAffairsItem
import com.example.data.model.EnglishLessonItem
import com.example.data.model.EssayPromptItem
import com.example.data.model.InterviewQuestionItem
import com.example.data.model.MainsPromptItem
import com.example.data.model.StudyTask
import com.example.data.model.TaskStatus

object AcademiesSeed {

    // Exact 9-hour (540 study minutes) automatic daily plan; breaks shown separately in UI
    fun defaultNineHourStudyPlan(todayDate: String): List<StudyTask> = listOf(
        StudyTask(
            id = "plan_01_ncert",
            title = "NCERT Foundation: History Class 6 Ch 1",
            titleHi = "एनसीईआरटी आधार: इतिहास कक्षा 6 अध्याय 1",
            category = "FOUNDATION",
            durationMinutes = 90,
            actualMinutes = 0,
            scheduledTime = "06:00",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 0
        ),
        StudyTask(
            id = "plan_02_polity_geo",
            title = "Foundation Subject II: Geography / Polity Basics",
            titleHi = "आधार विषय II: भूगोल / राजव्यवस्था मूल सिद्धांत",
            category = "FOUNDATION_2",
            durationMinutes = 75,
            actualMinutes = 0,
            scheduledTime = "07:45",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 1
        ),
        StudyTask(
            id = "plan_03_history_opt",
            title = "History Optional: Sources & Chronology",
            titleHi = "इतिहास वैकल्पिक: स्रोत एवं कालक्रम",
            category = "HISTORY_OPTIONAL",
            durationMinutes = 90,
            actualMinutes = 0,
            scheduledTime = "09:15",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 2
        ),
        StudyTask(
            id = "plan_04_english_45",
            title = "English Speaking & Translation Academy (45m)",
            titleHi = "दैनिक अंग्रेज़ी बोलचाल व अनुवाद अभ्यास (45 मिनट)",
            category = "ENGLISH_SPEAKING",
            durationMinutes = 45,
            actualMinutes = 0,
            scheduledTime = "12:00",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 3
        ),
        StudyTask(
            id = "plan_05_current_affairs",
            title = "Current Affairs, PIB & IR Digest",
            titleHi = "समसामयिकी, PIB एवं अंतर्राष्ट्रीय संबंध",
            category = "CURRENT_AFFAIRS",
            durationMinutes = 60,
            actualMinutes = 0,
            scheduledTime = "14:00",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 4
        ),
        StudyTask(
            id = "plan_06_mcq_csat",
            title = "Prelims MCQ Bank + CSAT Practice",
            titleHi = "प्रारंभिक परीक्षा MCQ एवं CSAT अभ्यास",
            category = "MCQ_AND_CSAT",
            durationMinutes = 60,
            actualMinutes = 0,
            scheduledTime = "15:15",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 5
        ),
        StudyTask(
            id = "plan_07_mains_writing",
            title = "Mains Answer Writing & Structure Drill",
            titleHi = "मुख्य परीक्षा उत्तर लेखन अभ्यास",
            category = "MAINS_WRITING",
            durationMinutes = 60,
            actualMinutes = 0,
            scheduledTime = "16:30",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 6
        ),
        StudyTask(
            id = "plan_08_revision",
            title = "Spaced Repetition + Flashcard Memory Academy",
            titleHi = "दैनिक पुनरावृत्ति (1d/3d/7d/15d/30d) एवं फ्लैशकार्ड",
            category = "REVISION",
            durationMinutes = 45,
            actualMinutes = 0,
            scheduledTime = "19:30",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 7
        ),
        StudyTask(
            id = "plan_09_weekly_prep",
            title = "Weekly Test Review & Weak Topic Mapping",
            titleHi = "साप्ताहिक टेस्ट तैयारी एवं कमजोर विषय सुधार",
            category = "WEEKLY_TEST",
            durationMinutes = 15,
            actualMinutes = 0,
            scheduledTime = "20:30",
            status = TaskStatus.PENDING.name,
            taskDate = todayDate,
            custom = false,
            orderIndex = 8
        )
    ) // Sum = 90 + 75 + 90 + 45 + 60 + 60 + 60 + 45 + 15 = 540 minutes = 9.0 hours!

    // Verifiable policy/static-curated reference briefs (dated & sourced; never inventing live breaking news)
    val currentAffairsArchive: List<CurrentAffairsItem> = listOf(
        CurrentAffairsItem(
            id = "ca_dholavira_unesco",
            title = "Dholavira & Lothal Heritage Conservation Framework",
            category = "Art, Culture & Heritage",
            publishedDate = "Official UNESCO / ASI Reference Archive",
            source = "UNESCO World Heritage Centre & PIB Ministry of Culture",
            whatHappened = "Dholavira (Gujarat), a major Harappan city (~4700 years old), holds UNESCO World Heritage status, while India continues developing the National Maritime Heritage Complex at Lothal.",
            whyImportant = "Connects NCERT Class 6 History Ch 1 & Ch 3 (Indus cities) directly to modern cultural diplomacy and heritage conservation.",
            background = "Dholavira demonstrated world-class water harvesting reservoirs and tripartite urban planning during the Mature Harappan phase (2600–1900 BCE).",
            prelimsRelevance = "Know Harappan sites by river/state: Dholavira (Luni/Khadir Bet, Gujarat), Lothal (Bhogava, Gujarat), Rakhigarhi (Haryana), Kalibangan (Ghaggar, Rajasthan).",
            mainsRelevance = "GS I: Salient aspects of Art, Architecture & Ancient Urban Planning; sustainable water management lessons for modern cities.",
            syllabusTopic = "Prelims GS I (History & Culture) / Mains GS I",
            importantFacts = listOf(
                "Dholavira inscribed as India's 40th UNESCO World Heritage Site (2021).",
                "Known for rock-cut reservoirs and a 10-sign Indus script signboard."
            )
        ),
        CurrentAffairsItem(
            id = "ca_prakrit_classical",
            title = "Classical Language Status Extended to Marathi, Pali, Prakrit, Assamese & Bengali",
            category = "National Policy & Culture",
            publishedDate = "October 2024 (Union Cabinet Release)",
            source = "Press Information Bureau (PIB), Government of India",
            whatHappened = "The Union Cabinet approved Classical Language status for five additional languages: Marathi, Pali, Prakrit, Assamese, and Bengali—bringing India's Classical Languages count to 11.",
            whyImportant = "Prakrit is explicitly highlighted in NCERT Class 6 History Ch 1 as the language used by ordinary people and in early inscriptions!",
            background = "Previously 6 languages held Classical status: Tamil (2004), Sanskrit (2005), Kannada (2008), Telugu (2008), Malayalam (2013), and Odia (2014).",
            prelimsRelevance = "Criteria for Classical Language (Linguistic Experts Committee under Ministry of Culture): high antiquity (1500–2000 years), ancient heritage literature, distinct literary tradition.",
            mainsRelevance = "GS I: Preservation of linguistic diversity, ancient manuscripts, and cultural heritage.",
            syllabusTopic = "GS I (Art & Culture) & GS II (Governance)",
            importantFacts = listOf(
                "Total Classical Languages in India now = 11.",
                "Pali and Prakrit preserve Buddhist, Jain, and Ashokan epigraphic traditions."
            )
        ),
        CurrentAffairsItem(
            id = "ca_rbi_mpc",
            title = "Monetary Policy Framework & Inflation Targeting in India",
            category = "Economy",
            publishedDate = "RBI Statutory Framework (2016 Amendment Reference)",
            source = "Reserve Bank of India (RBI) / Economic Survey",
            whatHappened = "India's Monetary Policy Committee (MPC) operates under the flexible inflation targeting framework targeting 4% CPI inflation with a tolerance band of +/- 2% (2%–6%).",
            whyImportant = "Core Prelims & Mains GS III topic connecting Class 12 Macroeconomics NCERT to real policy decisions.",
            background = "Constituted under Section 45ZB of the amended RBI Act, 1934 (6 members: 3 from RBI including Governor as ex-officio Chairperson + 3 appointed by Central Government).",
            prelimsRelevance = "Difference between CPI (Consumer Price Index) and WPI; Repo Rate vs Standing Deposit Facility (SDF); MPC casting vote of RBI Governor.",
            mainsRelevance = "GS III: Balancing price stability (controlling inflation) with economic growth and employment.",
            syllabusTopic = "Prelims Economy & Mains GS III",
            importantFacts = listOf(
                "Target: 4% CPI (+/- 2%).",
                "MPC has 6 members; RBI Governor holds casting vote in case of tie."
            )
        ),
        CurrentAffairsItem(
            id = "ca_india_global_south",
            title = "India's Role as 'Voice of the Global South' & Neighbourhood First",
            category = "International Relations & Diplomacy",
            publishedDate = "MEA Annual Policy Reference",
            source = "Ministry of External Affairs (MEA), India",
            whatHappened = "India has positioned itself as a bridge between developing nations (Global South) and multilateral forums like G20, advocating digital public infrastructure (DPI), climate finance, and UN reforms.",
            whyImportant = "Essential for GS II (International Relations) and Personality Test diplomatic questions.",
            background = "African Union became a permanent G20 member during India's 2023 G20 Presidency in New Delhi.",
            prelimsRelevance = "Global Biofuels Alliance, Coalition for Disaster Resilient Infrastructure (CDRI), International Solar Alliance (ISA).",
            mainsRelevance = "GS II: Bilateral, regional and global groupings involving India and affecting India's interests.",
            syllabusTopic = "Mains GS II (IR) & Interview",
            importantFacts = listOf(
                "UPI & Aadhaar-style DPI shared globally under open-source cooperation.",
                "ISA & CDRI are India-led multilateral climate initiatives."
            )
        )
    )

    val mainsPrompts: List<MainsPromptItem> = listOf(
        MainsPromptItem(
            id = "mains_gs1_sources",
            paper = "GS Paper I / History Optional",
            marks = 10,
            wordLimit = 150,
            question = "Compare literary manuscripts and stone inscriptions as sources for reconstructing early Indian history.",
            questionHi = "प्रारंभिक भारतीय इतिहास के पुनर्निर्माण में साहित्यिक पांडुलिपियों और प्रस्तर अभिलेखों की स्रोतों के रूप में तुलना कीजिए।",
            introTip = "Define both sources briefly in 2 lines—mentioning organic materials (palm leaf/birch bark) vs hard surfaces (stone/metal).",
            bodyStructure = listOf(
                "1. Strengths of Inscriptions: Durability, contemporary dating, exact royal decrees (e.g., Ashoka's edicts).",
                "2. Limitations of Inscriptions: Royal bias/eulogy (Prashastis), silent on commoners.",
                "3. Strengths of Manuscripts: Rich philosophical, scientific, literary depth (Sanskrit, Prakrit, Tamil).",
                "4. Synthesis with Archaeology: Material remains balance elite bias."
            ),
            conclusionTip = "Conclude that combining epigraphy, manuscripts, and archaeology creates a multi-dimensional 'Our Pasts' narrative.",
            modelKeyPoints = listOf(
                "Mention Kandahar bilingual edict (Greek + Aramaic).",
                "Mention Himalayan birch bark & temple preservation.",
                "Highlight why 'Our Pasts' is plural."
            )
        ),
        MainsPromptItem(
            id = "mains_gs2_federalism",
            paper = "GS Paper II (Polity)",
            marks = 15,
            wordLimit = 250,
            question = "Cooperative federalism is essential for India's inclusive growth. Discuss with reference to constitutional mechanisms.",
            questionHi = "भारत के समावेशी विकास के लिए सहकारी संघवाद अनिवार्य है। संवैधानिक तंत्रों के संदर्भ में चर्चा कीजिए।",
            introTip = "Start by noting India as a 'Union of States' (Article 1) balancing strong centre with state autonomy.",
            bodyStructure = listOf(
                "1. Constitutional Pillars: Seventh Schedule, Inter-State Council (Art 263), Finance Commission (Art 280), GST Council (Art 279A).",
                "2. Cooperative Initiatives: NITI Aayog, Aspirational Districts Programme.",
                "3. Friction Areas: Fiscal centralization, Governor role, river water disputes."
            ),
            conclusionTip = "Emphasize 'Team India' spirit—fiscal trust and empowered local bodies (73rd/74th Amendments).",
            modelKeyPoints = listOf("Article 1, 263, 279A, 280", "Sarkaria & Punchhi Commission recommendations")
        ),
        MainsPromptItem(
            id = "mains_gs4_ethics",
            paper = "GS Paper IV (Ethics Case Study)",
            marks = 15,
            wordLimit = 250,
            question = "As a District Collector, a flood relief contract is delayed if you follow full verification, while bypassing rules risks corruption. What ethical principles guide your decision?",
            questionHi = "जिला कलेक्टर के रूप में बाढ़ राहत कार्य में पूर्ण जांच से देरी होती है और नियम तोड़ने पर भ्रष्टाचार का जोखिम है। आप किन नैतिक सिद्धांतों से निर्णय लेंगे?",
            introTip = "Identify stakeholders (flood-affected citizens, administration, contractors) and core ethical dilemma (compassion/urgency vs procedural integrity).",
            bodyStructure = listOf(
                "1. Reject illegal shortcuts; invoke emergency procurement provisions permitted under disaster rules (GFR/NDMA).",
                "2. Ensure transparency via open digital tracking and joint verification team.",
                "3. Balance accountability with timely humanitarian relief."
            ),
            conclusionTip = "Uphold Constitutional morality, Empathy, and Rule of Law simultaneously.",
            modelKeyPoints = listOf("Transparency, Accountability, Compassion, Rule of Law")
        )
    )

    // Authentic UPSC CSE Mains Essay PYQs (with verified exam years)
    val essayPrompts: List<EssayPromptItem> = listOf(
        EssayPromptItem(
            id = "essay_pyq_2023_1",
            year = 2023,
            sourceLabel = "UPSC CSE Mains Essay 2023 (Authentic PYQ)",
            topic = "Thinking is like a game, it does not begin unless there is an opposite team.",
            topicHi = "चिंतन एक खेल की तरह है; यह तब तक प्रारंभ नहीं होता जब तक कोई विरोधी पक्ष न हो।",
            interpretationGuide = "Explore how debate, dissent, counter-arguments, and dialectical thinking sharpen truth in science, democracy, and personal growth.",
            dimensions = listOf("Philosophy & Epistemology (Vad-Vivad)", "Indian Democracy & Parliamentary Debate", "Science & Hypothesis Testing", "History: Reform movements challenging orthodoxy", "Conclusion: Healthy opposition strengthens unity"),
            verifiedQuoteSuggestion = "Rigveda: 'आ नो भद्राः क्रतवो यन्तु विश्वतः' (Let noble thoughts come to us from all directions)."
        ),
        EssayPromptItem(
            id = "essay_pyq_2022_1",
            year = 2022,
            sourceLabel = "UPSC CSE Mains Essay 2022 (Authentic PYQ)",
            topic = "Poets are the unacknowledged legislators of the world.",
            topicHi = "कवि संसार के अनधिकृत विधायक होते हैं।",
            interpretationGuide = "Originates from Percy Bysshe Shelley's 'A Defence of Poetry' (1821). Discuss how literature, songs, and culture shape empathy and social reform before laws catch up.",
            dimensions = listOf("Ancient India: Valmiki, Sangam poets, Kabir & Bhakti saints", "Freedom Struggle: Tagore, Subramania Bharati", "Modern Social Change: Cinema & literature shaping constitutional values", "Balance: Law enforces order; art transforms hearts"),
            verifiedQuoteSuggestion = "Attributed to Percy Bysshe Shelley, 'A Defence of Poetry' (1821)."
        ),
        EssayPromptItem(
            id = "essay_pyq_2021_1",
            year = 2021,
            sourceLabel = "UPSC CSE Mains Essay 2021 (Authentic PYQ)",
            topic = "The process of self-discovery has now been technologically outsourced.",
            topicHi = "आत्म-अन्वेषण की प्रक्रिया अब तकनीकी रूप से आउटसोर्स हो चुकी है।",
            interpretationGuide = "Contrast inner reflection (swadhyaya) with algorithmic feeds, social media validation, and AI shortcuts.",
            dimensions = listOf("Individual Psychology & Attention", "Education & Critical Thinking", "Ethical Tech Design", "Way Forward: Mindful use of technology as a tool, not master"),
            verifiedQuoteSuggestion = "Socrates (Plato's Apology): 'The unexamined life is not worth living.'"
        )
    )

    val englishLessons: List<EnglishLessonItem> = listOf(
        EnglishLessonItem(
            id = "eng_day_1",
            title = "Day 1: Introducing Yourself & Your UPSC Goal (45 Min)",
            focusArea = "Simple Present & Present Continuous + Confidence",
            durationMinutes = 45,
            grammarRule = "Use 'I am preparing for...' for ongoing action (Present Continuous), and 'My optional subject is History' for facts (Simple Present).",
            hindiToEnglishDrills = listOf(
                "मैंने विज्ञान विषय से कक्षा 12 उत्तीर्ण की है।" to "I have completed Class 12 in the Science stream.",
                "मेरा लक्ष्य 2029 में IAS अधिकारी बनना है।" to "My goal is to become an IAS officer in 2029.",
                "मैं इतिहास को अपने वैकल्पिक विषय के रूप में पढ़ रही हूँ।" to "I am studying History as my optional subject.",
                "अतीत को समझने के लिए हम अभिलेखों और पांडुलिपियों का अध्ययन करते हैं।" to "To understand the past, we study inscriptions and manuscripts."
            ),
            keyVocabulary = listOf(
                Triple("Perseverance", "दृढ़ता / लगातार प्रयास", "Success in UPSC requires patience and perseverance."),
                Triple("Chronology", "कालक्रम", "Understanding chronology helps connect historical events."),
                Triple("Articulate", "स्पष्ट रूप से व्यक्त करना", "An administrator must articulate ideas calmly and clearly.")
            ),
            speakingPrompt = "Speak for 2 minutes aloud: Introduce your educational background, why you chose History Optional, and one thing you learned from NCERT History Chapter 1."
        ),
        EnglishLessonItem(
            id = "eng_day_2",
            title = "Day 2: Past Tense for History & Storytelling (45 Min)",
            focusArea = "Simple Past Tense + Cause & Effect Connectors",
            durationMinutes = 45,
            grammarRule = "When narrating ancient history, use Simple Past (lived, grew, flourished) + connectors like 'Therefore', 'Meanwhile', 'Consequently'.",
            hindiToEnglishDrills = listOf(
                "हज़ारों साल पहले लोग नर्मदा नदी के किनारे रहते थे।" to "Thousands of years ago, people lived along the Narmada River.",
                "सिंधु नदी के किनारे प्रारंभिक नगर 4700 वर्ष पहले विकसित हुए।" to "Earliest cities flourished along the Indus River 4700 years ago.",
                "अभिलेख पत्थर जैसी कठोर सतह पर खोदे जाते थे।" to "Inscriptions were engraved on hard surfaces like stone."
            ),
            keyVocabulary = listOf(
                Triple("Flourish", "फलना-फूलना / विकसित होना", "Early cities flourished near fertile river basins."),
                Triple("Excavation", "उत्खनन / खुदाई", "Archaeological excavation revealed ancient pottery and grains."),
                Triple("Durable", "टिकाऊ", "Stone inscriptions are far more durable than palm-leaf manuscripts.")
            ),
            speakingPrompt = "Explain in 5 English sentences how 'India' and 'Bharat' got their names."
        )
    )

    val interviewQuestions: List<InterviewQuestionItem> = listOf(
        InterviewQuestionItem(
            id = "int_1",
            category = "DAF & Academic Transition",
            question = "You completed Class 12 in Science, yet you chose History as your UPSC Optional. Why?",
            idealApproach = "Show balanced maturity: Science trained analytical rigor and evidence-based reasoning, while History deepens understanding of Indian society, governance evolution, and human empathy—both complement an IAS officer.",
            pitfallsToAvoid = "Never say 'I disliked Science' or 'History is easy to score.' Present positive synthesis."
        ),
        InterviewQuestionItem(
            id = "int_2",
            category = "Situational & Administrative",
            question = "As an SDM, local citizens protest a heritage site conservation fence because it blocks a shortcut road. How will you resolve it?",
            idealApproach = "Combine empathy with heritage law: hold a dialogue with community elders, provide a safe alternative pathway nearby, explain tourism/livelihood benefits of preserving the monument, and uphold ASI guidelines calmly.",
            pitfallsToAvoid = "Avoid heavy-handed force as a first step or ignoring statutory monument protection."
        ),
        InterviewQuestionItem(
            id = "int_3",
            category = "International Relations & Diplomacy",
            question = "How does India balance strategic ties with Western partners and its leadership role in the Global South?",
            idealApproach = "Highlight 'Strategic Autonomy' and issue-based partnerships—India prioritizes national development, multipolarity, climate justice, and Digital Public Infrastructure sharing.",
            pitfallsToAvoid = "Avoid taking extreme partisan or emotional stances on foreign nations."
        )
    )
}
