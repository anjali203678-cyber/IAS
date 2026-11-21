package com.example.data.seed

import com.example.data.model.BtsMember
import com.example.data.model.ComparisonTableData
import com.example.data.model.ConfusedConceptItem
import com.example.data.model.DetailedChapterContent
import com.example.data.model.EnglishTermItem
import com.example.data.model.FlashcardDifficulty
import com.example.data.model.FlashcardItem
import com.example.data.model.KeyFactRow
import com.example.data.model.MasteryStatus
import com.example.data.model.MemoryTechniqueItem
import com.example.data.model.MindMapNode
import com.example.data.model.TimelineEntry
import com.example.data.model.TopicNoteBlock

object HistoryCh1NotesSeed {

    val chapterContent: DetailedChapterContent by lazy {
        DetailedChapterContent(
            chapterId = "ncert_hist_6_ch1",
            bookTitle = "NCERT Class 6 History: Our Pasts – I (हमारे अतीत – I)",
            chapterTitleEn = "What, Where, How and When?",
            chapterTitleHi = "क्या, कब, कहाँ और कैसे?",
            introHi = "इतिहास (History) केवल राजाओं की तारीखें रटने का विषय नहीं है, बल्कि यह समझने की यात्रा है कि हज़ारों साल पहले आम लोग—शिकारी, किसान, व्यापारी, और कारीगर—कैसे रहते थे, क्या खाते थे, और हम आज उनके बारे में कैसे जान पाते हैं। UPSC प्रारंभिक परीक्षा (Prelims) और इतिहास वैकल्पिक (History Optional Paper I) के लिए यह अध्याय आपकी नींव (Foundation) है।",
            beginnerExplanationHi = "बिलकुल शुरुआत से समझिए: मान लीजिए आपको जानना है कि आपके परदादा 100 साल पहले कैसे रहते थे। आप पुरानी तस्वीरें, चिट्ठियाँ या बर्तन देखेंगे। ठीक उसी तरह, हज़ारों-लाखों साल पुराने लोगों के जीवन को जानने के लिए इतिहासकार और पुरातत्वविद् (Archaeologists) पुरानी किताबें (पांडुलिपियाँ), पत्थरों पर लिखे लेख (अभिलेख), पुराने बर्तन, औज़ार और सिक्के ढूँढ़ते हैं। इस अध्याय में हम सीखते हैं कि भारत में सबसे पहले लोग किन नदियों और पहाड़ियों के पास बसे, हमारे देश के नाम 'इंडिया' और 'भारत' कैसे पड़े, और तारीखों (BCE / CE) को कैसे गिना जाता है।",
            englishTerminology = listOf(
                EnglishTermItem(
                    englishTerm = "Manuscript",
                    hindiMeaning = "पांडुलिपि (हाथ से लिखी पुस्तक)",
                    simpleExplanationHi = "लैटिन शब्द 'Manu' का अर्थ है 'हाथ'। ताड़ के पत्तों (Palm leaf) या भोजपत्र (Birch bark) पर हाथ से लिखी पुरानी किताबें।",
                    examUsage = "UPSC Prelims often distinguishes Palm-leaf manuscripts from stone inscriptions."
                ),
                EnglishTermItem(
                    englishTerm = "Inscription",
                    hindiMeaning = "अभिलेख (कठोर सतह पर उत्कीर्ण लेख)",
                    simpleExplanationHi = "पत्थर या धातु जैसी कठोर सतह पर खोदकर (carved/engraved) लिखे गए आदेश, उपलब्धियाँ या दान-विवरण।",
                    examUsage = "Ashokan Edicts (e.g., Kandahar inscription in Greek & Aramaic) are key inscriptions."
                ),
                EnglishTermItem(
                    englishTerm = "Archaeologist",
                    hindiMeaning = "पुरातत्वविद्",
                    simpleExplanationHi = "वह विशेषज्ञ जो ज़मीन की खुदाई (Excavation) से मिले अवशेषों—ईंट, बर्तन, हड्डी, औज़ार—का अध्ययन करके अतीत को समझते हैं।",
                    examUsage = "Material remains are primary sources in History Optional Paper I."
                ),
                EnglishTermItem(
                    englishTerm = "Tributary",
                    hindiMeaning = "सहायक नदी",
                    simpleExplanationHi = "एक छोटी नदी जो किसी बड़ी मुख्य नदी में जाकर मिल जाती है (जैसे सोन नदी, गंगा की सहायक नदी है)।",
                    examUsage = "Magadha emerged along the Ganga and its tributary Son."
                ),
                EnglishTermItem(
                    englishTerm = "Decipherment",
                    hindiMeaning = "अज्ञात लिपि को पढ़ना / अर्थ निकालना",
                    simpleExplanationHi = "पुरानी अज्ञात भाषा या लिपि (Script) के अक्षरों का अर्थ समझना, जैसे अशोक की ब्राह्मी लिपि को पढ़ना।",
                    examUsage = "James Prinsep deciphered Brahmi & Kharosthi scripts in 1837."
                ),
                EnglishTermItem(
                    englishTerm = "BCE & CE",
                    hindiMeaning = "ईसा पूर्व (Before Common Era) एवं ईस्वी (Common Era)",
                    simpleExplanationHi = "ईसा मसीह के जन्म को 0 मानकर उससे पहले का समय BCE (BC) और बाद का समय CE (AD) कहलाता है।",
                    examUsage = "Essential for arranging UPSC chronological timelines."
                )
            ),
            topicNotes = listOf(
                TopicNoteBlock(
                    headingHi = "1. नर्मदा नदी और शुरुआती संग्राहक (Hunter-Gatherers)",
                    headingEn = "Life Along the Narmada River",
                    explanationHi = "कई लाख साल पहले लोग नर्मदा नदी के तट पर रहते थे। वे 'कुशल संग्राहक' (Skilled Gatherers) थे—आसपास के जंगलों से जड़ें, फल और अन्य उत्पाद इकट्ठा करते थे और जानवरों का शिकार करते थे।",
                    keyBullets = listOf(
                        "Narmada Valley: Earliest evidence of skilled gatherers & hunters (~several hundred thousand years ago).",
                        "They knew rich forest wealth: roots, fruits, and forest produce."
                    )
                ),
                TopicNoteBlock(
                    headingHi = "2. सुलेमान और किरथर पहाड़ियाँ: पहली खेती और पशुपालन",
                    headingEn = "Sulaiman and Kirthar Hills (~8000 Years Ago)",
                    explanationHi = "उत्तर-पश्चिम में सुलेमान और किरथर पहाड़ियों (वर्तमान पाकिस्तान क्षेत्र) के पास लगभग 8000 साल पहले स्त्री-पुरुषों ने सबसे पहले गेहूँ (Wheat) और जौ (Barley) जैसी फसलें उपजाना शुरू किया और भेड़, बकरी, गाय-बैल जैसे पशुओं को पालतू बनाया। लोग गाँवों में रहने लगे।",
                    keyBullets = listOf(
                        "Location: Northwest (Sulaiman & Kirthar Hills).",
                        "Timeline: ~8000 years ago (~6000 BCE).",
                        "First crops here: Wheat and Barley; domesticated animals: Sheep, Goat, Cattle."
                    )
                ),
                TopicNoteBlock(
                    headingHi = "3. गारो पहाड़ियाँ और विंध्य क्षेत्र: चावल की खेती",
                    headingEn = "Garo Hills & North of the Vindhyas",
                    explanationHi = "उत्तर-पूर्व में गारो पहाड़ियाँ (मेघालय) तथा मध्य भारत में विंध्य पहाड़ियों के उत्तर में भी कृषि का विकास हुआ। विंध्य के उत्तर में स्थित स्थानों पर सबसे पहले चावल (Rice) उपजाया गया।",
                    keyBullets = listOf(
                        "Garo Hills (Northeast): Early agricultural development.",
                        "North of Vindhyas (Central India): Earliest rice cultivation (e.g., Koldihwa/Lahuradewa context)."
                    )
                ),
                TopicNoteBlock(
                    headingHi = "4. सिंधु और गंगा: शुरुआती नगर और विशाल साम्राज्य",
                    headingEn = "Indus (~4700 yrs ago) & Ganga (~2500 yrs ago)",
                    explanationHi = "लगभग 4700 साल पहले सिंधु (Indus) और उसकी सहायक नदियों के किनारे आरंभिक नगर फले-फूले (हड़प्पा सभ्यता)। बाद में लगभग 2500 साल पहले गंगा और उसकी सहायक नदियों (विशेषकर सोन नदी) के किनारे तथा समुद्र तटों पर नगर विकसित हुए और गंगा के दक्षिण में 'मगध' जैसा शक्तिशाली राज्य बना।",
                    keyBullets = listOf(
                        "~4700 years ago: First cities on Indus & tributaries (Jhelum, Chenab, Ravi, Beas, Sutlej).",
                        "~2500 years ago: Cities along Ganga & its tributary Son -> Rise of Magadha."
                    )
                ),
                TopicNoteBlock(
                    headingHi = "5. देश के नाम: 'इंडिया' (India) और 'भारत' (Bharat)",
                    headingEn = "Names of the Land",
                    explanationHi = "हमारे देश के दो प्रचलित नाम हैं:\n• 'इंडिया' शब्द 'इंडस' (Indus) से निकला है जिसे संस्कृत में 'सिंधु' कहते हैं। लगभग 2500 साल पहले उत्तर-पश्चिम से आने वाले ईरानियों और यूनानियों (Iranians & Greeks) ने सिंधु को 'हिंदोस' या 'इंदोस' कहा और इस नदी के पूर्व की भूमि को 'इंडिया' कहा।\n• 'भरत' नाम का प्रयोग उत्तर-पश्चिम में रहने वाले लोगों के एक समूह के लिए किया जाता था, जिसका उल्लेख ऋग्वेद (संस्कृत की आरंभिक रचना, ~3500 साल पुरानी) में मिलता है। बाद में यह नाम पूरे देश के लिए प्रयुक्त हुआ।",
                    keyBullets = listOf(
                        "Iranians & Greeks (~2500 yrs ago): Indus -> Hindos/Indos -> Land to the east = India.",
                        "Rigveda (~3500 yrs ago): Mentions 'Bharata' group in Northwest -> Later applied to the whole country."
                    )
                ),
                TopicNoteBlock(
                    headingHi = "6. अतीत की जानकारी के स्रोत: पांडुलिपि, अभिलेख और पुरातत्व",
                    headingEn = "Sources of History: Manuscripts, Inscriptions & Archaeology",
                    explanationHi = "अतीत की जानकारी तीन मुख्य स्रोतों से मिलती है:\n1) पांडुलिपियाँ (Manuscripts): ताड़पत्रों या हिमालय में उगने वाले भूर्ज (Birch) पेड़ की छाल पर हाथ से लिखी गई। ये संस्कृत, प्राकृत (आम लोगों की भाषा) और तमिल में थीं। इनमें से कई को कीड़ों ने खा लिया या नष्ट हो गईं, पर कई मंदिरों व विहारों में सुरक्षित हैं।\n2) अभिलेख (Inscriptions): पत्थर या धातु पर उत्कीर्ण। कंधार (अफ़ग़ानिस्तान) से मिला ~2250 साल पुराना अशोक का अभिलेख यूनानी (Greek) और अरामेइक (Aramaic) लिपियों में लिखा है।\n3) पुरातत्व (Archaeology): मिट्टी के बर्तन, सिक्के, हड्डियाँ (जिससे पता चलता है कि लोग क्या खाते थे), पत्थर/ईंट की इमारतें।",
                    keyBullets = listOf(
                        "Birch bark (भोजपत्र) grows in the Himalayas.",
                        "Prakrit was the language of ordinary people.",
                        "Kandahar Ashokan inscription (~2250 yrs ago) was bilingual: Greek & Aramaic."
                    )
                ),
                TopicNoteBlock(
                    headingHi = "7. 'Our Pasts' (हमारे अतीत) — बहुवचन क्यों?",
                    headingEn = "One Past or Many?",
                    explanationHi = "किताब के शीर्षक में 'Past' नहीं बल्कि 'Pasts' (बहुवचन) लिखा है, क्योंकि अलग-अलग समूहों के लोगों के अतीत अलग-अलग थे—जैसे पशुपालकों और किसानों का जीवन राजाओं-रानियों से अलग था, और व्यापारियों का जीवन शिल्पकारों से अलग था। इसके अलावा राजा अपनी विजयों का लेखा-जोखा रखते थे, जबकि आम लोग अपने कार्यों का विवरण नहीं रखते थे।",
                    keyBullets = listOf(
                        "Plural 'Pasts' highlights diverse experiences: kings vs farmers, herders, craftspersons, coastal dwellers.",
                        "Rulers kept written records of victories; ordinary people mostly left archaeological traces."
                    )
                )
            ),
            definitions = listOf(
                "History (इतिहास)" to "अतीत की घटनाओं, समाज, संस्कृति और परिवर्तनों का प्रमाण-आधारित अध्ययन।",
                "Archaeology (पुरातत्व)" to "उत्खनन से प्राप्त भौतिक अवशेषों के माध्यम से मानव अतीत का अध्ययन।",
                "Epigraphy (अभिलेखशास्त्र)" to "पत्थर या धातु पर खुदे अभिलेखों का अध्ययन।",
                "Numismatics (मुद्राशास्त्र)" to "प्राचीन सिक्कों के अध्ययन का विज्ञान।",
                "Manuscript (पांडुलिपि)" to "हाथ से ताड़पत्र या भोजपत्र पर लिखी गई प्राचीन पुस्तक।"
            ),
            keyFactsAndPersonalities = listOf(
                KeyFactRow("First Wheat & Barley", "सुलेमान एवं किरथर पहाड़ियाँ (उत्तर-पश्चिम)", "~8000 years ago"),
                KeyFactRow("Earliest Rice Cultivation", "विंध्य पर्वत के उत्तर में स्थित क्षेत्र", "Early Neolithic"),
                KeyFactRow("First Cities on Indus", "सिंधु एवं उसकी सहायक नदियों के किनारे (हड़प्पा)", "~4700 years ago"),
                KeyFactRow("Ganga Cities & Magadha", "गंगा और सोन नदी के दक्षिण में शक्तिशाली राज्य", "~2500 years ago"),
                KeyFactRow("Rigveda & 'Bharata'", "संस्कृत में रचित प्राचीनतम वेद जिसमें 'भरत' जन का उल्लेख है", "~3500 years ago"),
                KeyFactRow("Kandahar Inscription", "सम्राट अशोक का द्विभाषी अभिलेख (Greek & Aramaic)", "~2250 years ago"),
                KeyFactRow("Rosetta Stone (Egypt)", "तीन लिपियों में (Greek + 2 Egyptian forms) — कारतूस (Cartouche) से पठन", "196 BCE (Deciphered 1822)")
            ),
            comparisonTables = listOf(
                ComparisonTableData(
                    title = "Manuscripts (पांडुलिपि) vs Inscriptions (अभिलेख)",
                    headers = listOf("आधार (Basis)", "Manuscripts (पांडुलिपि)", "Inscriptions (अभिलेख)"),
                    rows = listOf(
                        listOf("सतह (Surface)", "ताड़पत्र (Palm leaf) / भोजपत्र (Birch bark)", "पत्थर (Stone) / धातु (Metal)"),
                        listOf("लिखने की विधि", "हाथ से स्याही/कलम द्वारा लिखना", "छेनी-हथौड़े से खोदकर (Engraved)"),
                        listOf("स्थायित्व (Durability)", "नाज़ुक; कीड़ों या नमी से नष्ट हो सकती हैं", "अत्यंत टिकाऊ; हज़ारों साल सुरक्षित"),
                        listOf("मुख्य विषय", "धर्म, विज्ञान, औषधि, कविता, नाटक", "राजाओं के आदेश, विजय, दान-विवरण")
                    )
                ),
                ComparisonTableData(
                    title = "Chronology Systems: BCE vs CE",
                    headers = listOf("Term", "Full Form", "Meaning in Hindi"),
                    rows = listOf(
                        listOf("BC / BCE", "Before Christ / Before Common Era", "ईसा मसीह के जन्म से पहले (उल्टी गिनती, जैसे 2500 BCE पहले है 1500 BCE से)"),
                        listOf("AD / CE", "Anno Domini / Common Era", "ईसा मसीह के जन्म के बाद ('In the year of the Lord')"),
                        listOf("BP", "Before Present", "वर्तमान (आमतौर पर 1950 CE) से पहले के वर्ष")
                    )
                )
            ),
            timeline = listOf(
                TimelineEntry("~Several Lakh Years Ago", "नर्मदा घाटी में कुशल संग्राहक और शिकारी", "Skilled gatherers along Narmada"),
                TimelineEntry("~8000 Years Ago", "सुलेमान-किरथर में गेहूँ व जौ की शुरुआत, पशुपालन", "Beginning of agriculture in Sulaiman-Kirthar"),
                TimelineEntry("~4700 Years Ago", "सिंधु और सहायक नदियों के किनारे प्रथम नगर", "Earliest cities on Indus & tributaries"),
                TimelineEntry("~3500 Years Ago", "ऋग्वेद की रचना ('भरत' समूह का उल्लेख)", "Composition of Rigveda"),
                TimelineEntry("~2500 Years Ago", "गंगा घाटी के नगर, मगध राज्य, और 'इंडिया' नाम", "Cities on Ganga, Magadha, Iranians/Greeks"),
                TimelineEntry("~2250 Years Ago", "कंधार में अशोक का यूनानी-अरामेइक अभिलेख", "Ashoka's Kandahar bilingual inscription")
            ),
            mapLocations = listOf(
                "Sulaiman & Kirthar Hills" to "उत्तर-पश्चिम (वर्तमान पाकिस्तान-अफ़ग़ान सीमा) — गेहूँ व जौ के प्रारंभिक केंद्र",
                "Garo Hills" to "उत्तर-पूर्व भारत (मेघालय) — प्रारंभिक कृषि क्षेत्र",
                "Vindhyas (North)" to "मध्य भारत — चावल की प्रारंभिक खेती",
                "Narmada River" to "मध्य भारत — प्रारंभिक शिकारी-संग्राहक निवास",
                "Indus & Tributaries" to "उत्तर-पश्चिम (झेलम, चिनाब, रावी, ब्यास, सतलुज) — 4700 वर्ष पूर्व नगर",
                "Ganga & Son" to "पूर्वी उत्तर प्रदेश व बिहार — 2500 वर्ष पूर्व मगध राज्य",
                "Kandahar" to "अफ़ग़ानिस्तान — सम्राट अशोक का द्विभाषी अभिलेख"
            ),
            prelimsPointers = listOf(
                "Wheat & Barley first appeared around Sulaiman & Kirthar (~8000 yrs ago); Rice first appeared north of the Vindhyas.",
                "Birch (भूर्ज) trees grow specifically in the Himalayas; manuscripts were written on both Palm leaves and Birch bark.",
                "Ashoka's Kandahar inscription is in TWO scripts & languages: Greek and Aramaic.",
                "Iranians and Greeks came from the Northwest ~2500 years ago and called the Indus 'Hindos' or 'Indos'.",
                "'Bharata' was originally a group of people living in the Northwest mentioned in the Rigveda (~3500 yrs ago).",
                "Remember reverse counting in BCE: 4700 years ago (~2700 BCE) is OLDER than 2500 years ago (~500 BCE)."
            ),
            mainsAnalyticalPoints = listOf(
                "Why 'Our Pasts' is plural: Indian history is not a single monolithic story of kings; studying farmers, herders, women, and artisans reveals multi-layered socio-economic realities (GS I & History Optional).",
                "Geographical determinism in Ancient India: Mountain passes in the northwest allowed cultural exchange & migrations (Iranians, Greeks), while river basins (Indus, Ganga) enabled agricultural surplus and state formation (Magadha).",
                "Limitations of written vs archaeological sources: Literary texts reflect elite perspectives and suffer decay, whereas archaeology gives silent evidence of everyday diet, housing, and trade."
            ),
            examplesAndCaseStudies = listOf(
                "Case Study — Rosetta Stone (Egypt): A carved stone from Rosetta (Egypt) containing one text in three scripts (Greek + two Egyptian scripts). Scholars matched king names like 'Ptolemy' and 'Cleopatra' inside oval frames called 'Cartouche' to decipher Egyptian hieroglyphs.",
                "Case Study — Animal Bones in Archaeology: Bones of wild animals vs domesticated cattle help archaeologists distinguish hunter-gatherer camps from settled farming villages."
            ),
            importantExamPoints = listOf(
                "Do NOT confuse 'AD' with 'After Death' — AD stands for 'Anno Domini' (In the year of the Lord).",
                "Sone (Son) is a southern tributary of the Ganga; Magadha was located south of the Ganga.",
                "Prakrit was the common people's spoken language, whereas Sanskrit was mainly used in elite/religious texts."
            ),
            confusedConcepts = listOf(
                ConfusedConceptItem(
                    pairTitle = "Manuscripts vs Inscriptions",
                    sideA = "Manuscripts: Written by hand on soft surfaces (Palm leaf, Birch bark). Fragile.",
                    sideB = "Inscriptions: Engraved on hard surfaces (Stone, Metal). Durable.",
                    memoryTipHi = "Manu = हाथ (हाथ से पत्ते पर), In-script = पत्थर के अंदर खोदकर!"
                ),
                ConfusedConceptItem(
                    pairTitle = "Wheat/Barley vs Rice First Areas",
                    sideA = "Wheat & Barley: Sulaiman & Kirthar hills (Northwest).",
                    sideB = "Rice: North of the Vindhyas (Central India).",
                    memoryTipHi = "सु-गे-जौ (सुलेमान-गेहूँ-जौ) और विंध्य-चावल!"
                ),
                ConfusedConceptItem(
                    pairTitle = "Indus (~4700 yrs) vs Ganga (~2500 yrs)",
                    sideA = "Indus cities flourished ~4700 years ago.",
                    sideB = "Ganga cities & Magadha flourished ~2500 years ago.",
                    memoryTipHi = "सिंधु बड़ी बहन (4700) है और गंगा छोटी बहन (2500)।"
                )
            ),
            chapterSummaryHi = "इस अध्याय से हमने सीखा कि इतिहास केवल राजाओं का नहीं बल्कि सभी वर्गों का ('अतीत' बहुवचन) है। नर्मदा पर शुरुआती संग्राहक रहे, सुलेमान-किरथर में ~8000 वर्ष पहले गेहूँ-जौ की खेती शुरू हुई, विंध्य के उत्तर में चावल उगा, ~4700 वर्ष पहले सिंधु पर प्रथम नगर बसे और ~2500 वर्ष पहले गंगा-सोन के पास मगध बना। ईरानियों/यूनानियों ने 'इंडिया' नाम दिया और ऋग्वेद के 'भरत' जन से 'भारत' नाम पड़ा। हम पांडुलिपि, अभिलेख और पुरातत्व की मदद से अतीत को जानते हैं।",
            onePageRevisionSheet = listOf(
                "1. Narmada -> Skilled gatherers & hunters (lakhs of years ago).",
                "2. Sulaiman & Kirthar (NW) -> First Wheat & Barley + Sheep/Goat/Cattle (~8000 yrs ago).",
                "3. Garo Hills (NE) & North of Vindhyas -> Agriculture & earliest Rice.",
                "4. Indus & Tributaries -> Earliest cities (~4700 yrs ago).",
                "5. Ganga & Son -> Cities & Magadha kingdom (~2500 yrs ago).",
                "6. India = from Indus/Sindhu (Iranians & Greeks: Hindos/Indos).",
                "7. Bharat = from 'Bharata' tribe in Rigveda (~3500 yrs ago).",
                "8. Manuscripts = Hand-written on Palm leaf / Himalayan Birch bark (Sanskrit, Prakrit, Tamil).",
                "9. Inscriptions = Engraved on stone/metal (e.g., Ashoka Kandahar in Greek & Aramaic, ~2250 yrs ago).",
                "10. Dates: BCE (Before Common Era) counts backward; CE (Common Era) counts forward."
            ),
            memoryTechniques = listOf(
                MemoryTechniqueItem(
                    techniqueType = "Acronym (संक्षिप्त सूत्र)",
                    title = "S-K-V-I-G River & Hill Ladder",
                    contentHi = "S-K (Sulaiman-Kirthar: गेहूँ-जौ 8000) -> V (Vindhyas: चावल) -> I (Indus: 4700 नगर) -> G (Ganga-Son: 2500 मगध)। क्रम याद रखें: 8000 -> 4700 -> 2500!",
                    visualCue = "🏔️ सुलेमान → 🌾 विंध्य चावल → 🏙️ सिंधु 4700 → 👑 गंगा-मगध 2500"
                ),
                MemoryTechniqueItem(
                    techniqueType = "Historical Storytelling (कहानी विधि)",
                    title = "नर्मदा से मगध की 4-पड़ाव यात्रा",
                    contentHi = "कल्पना कीजिए एक यात्री की: सबसे पहले वह नर्मदा के जंगल में फल तोड़ता है (संग्राहक)। फिर उत्तर-पश्चिम के सुलेमान पहाड़ पर 8000 साल पहले गेहूँ की रोटी बनाता है। फिर वह विंध्य पर्वत के पास चावल पकाता है, और अंत में 4700 साल पहले सिंधु के चमचमाते शहर देखकर 2500 साल पहले गंगा किनारे मगध की राजधानी पहुंचता है!",
                    visualCue = "🚶 नर्मदा जंगल → 🍞 सुलेमान गेहूँ → 🍚 विंध्य चावल → 🏛️ सिंधु से मगध"
                ),
                MemoryTechniqueItem(
                    techniqueType = "Concept Association (तुलनात्मक जुड़ाव)",
                    title = "पांडुलिपि vs अभिलेख ट्रिक",
                    contentHi = "Manuscript में 'Manu' = हाथ (Manual) -> नरम पत्ता (Palm/Birch)। Inscription में 'In' = पत्थर के अंदर छेनी से खुदना (Hard Stone)।",
                    visualCue = "📜 हाथ + पत्ता = पांडुलिपि | 🪨 छेनी + पत्थर = अभिलेख"
                ),
                MemoryTechniqueItem(
                    techniqueType = "Chronology Trick (तिथि क्रम)",
                    title = "4700 - 3500 - 2500 - 2250 Step-Down",
                    contentHi = "याद रखें '47-35-25-22': 4700 (सिंधु नगर) -> 3500 (ऋग्वेद) -> 2500 (गंगा नगर व ईरानी/यूनानी) -> 2250 (अशोक कंधार अभिलेख)।",
                    visualCue = "⏳ 4700 → 3500 → 2500 → 2250 years ago"
                )
            ),
            mindMapRoot = "Ch 1: What, Where, How & When?",
            mindMapBranches = listOf(
                MindMapNode("Early Settlements", listOf("Narmada: Gatherers", "Sulaiman-Kirthar: Wheat/Barley (8000y)", "Vindhya North: Rice", "Indus: Cities (4700y)", "Ganga-Son: Magadha (2500y)")),
                MindMapNode("Names of Land", listOf("India <- Indus/Sindhu (Iranians & Greeks)", "Bharat <- Rigveda tribe (~3500y)")),
                MindMapNode("Historical Sources", listOf("Manuscripts (Palm/Birch, Prakrit/Sanskrit/Tamil)", "Inscriptions (Stone/Metal, Kandahar Greek+Aramaic)", "Archaeology (Pots, Tools, Bones, Coins)")),
                MindMapNode("Time & Dates", listOf("BCE = Before Common Era", "CE = Common Era (AD = Anno Domini)", "BP = Before Present"))
            ),
            starterFlashcards = listOf(
                FlashcardItem(
                    id = "fc_h6c1_1",
                    deckMember = BtsMember.RM.name,
                    subject = "History",
                    chapterId = "ncert_hist_6_ch1",
                    front = "Where did men and women first begin to grow crops like wheat and barley about 8000 years ago?",
                    back = "Sulaiman and Kirthar hills (in the northwest).",
                    explanation = "Here people also began rearing animals like sheep, goat, and cattle, and lived in villages.",
                    difficulty = FlashcardDifficulty.EASY.name,
                    sourceRef = "NCERT Class 6 Our Pasts – I, Ch 1, p. 2",
                    masteryStatus = MasteryStatus.NEW.name
                ),
                FlashcardItem(
                    id = "fc_h6c1_2",
                    deckMember = BtsMember.V.name,
                    subject = "History",
                    chapterId = "ncert_hist_6_ch1",
                    front = "Which region saw the earliest cultivation of rice in this NCERT chapter?",
                    back = "Places located to the north of the Vindhyas (Central India).",
                    explanation = "Do not confuse with Sulaiman-Kirthar (wheat & barley).",
                    difficulty = FlashcardDifficulty.MEDIUM.name,
                    sourceRef = "NCERT Class 6 Our Pasts – I, Ch 1, p. 2",
                    masteryStatus = MasteryStatus.NEW.name
                ),
                FlashcardItem(
                    id = "fc_h6c1_3",
                    deckMember = BtsMember.JIN.name,
                    subject = "History",
                    chapterId = "ncert_hist_6_ch1",
                    front = "Which two scripts & languages appear on Emperor Ashoka's Kandahar inscription (~2250 years ago)?",
                    back = "Greek and Aramaic.",
                    explanation = "Kandahar is in present-day Afghanistan, where Greek and Aramaic were understood locally.",
                    difficulty = FlashcardDifficulty.MEDIUM.name,
                    sourceRef = "NCERT Class 6 Our Pasts – I, Ch 1, p. 5",
                    masteryStatus = MasteryStatus.NEW.name
                ),
                FlashcardItem(
                    id = "fc_h6c1_4",
                    deckMember = BtsMember.SUGA.name,
                    subject = "History",
                    chapterId = "ncert_hist_6_ch1",
                    front = "What does 'AD' stand for, and what is its modern equivalent?",
                    back = "Anno Domini ('in the year of the Lord'); modern equivalent is CE (Common Era).",
                    explanation = "AD never means 'After Death'. BC is replaced by BCE (Before Common Era).",
                    difficulty = FlashcardDifficulty.EASY.name,
                    sourceRef = "NCERT Class 6 Our Pasts – I, Ch 1, p. 8",
                    masteryStatus = MasteryStatus.NEW.name
                ),
                FlashcardItem(
                    id = "fc_h6c1_5",
                    deckMember = BtsMember.JUNGKOOK.name,
                    subject = "History",
                    chapterId = "ncert_hist_6_ch1",
                    front = "Where is the earliest mention of the name 'Bharata' found, and how old is that text?",
                    back = "In the Rigveda (composed in Sanskrit about 3500 years ago).",
                    explanation = "Originally used for a group of people in the northwest; later applied to the whole country.",
                    difficulty = FlashcardDifficulty.MEDIUM.name,
                    sourceRef = "NCERT Class 6 Our Pasts – I, Ch 1, p. 4",
                    masteryStatus = MasteryStatus.NEW.name
                )
            ),
            questions = HistoryCh1QuestionsSeed.allQuestions,
            sourceReferences = listOf(
                "NCERT Class 6 History: Our Pasts – I, Chapter 1: 'What, Where, How and When?' (Official NCERT Textbook, pp. 1–10)",
                "Official UPSC CSE Prelims Archives (for verified PYQs marked with exact exam year)"
            ),
            pyqAvailabilityNote = "Showing 10 authentic relevant UPSC CSE Prelims PYQs on Ancient Indian Sources, Inscriptions, Decipherment & Early Civilizations (with exact exam years) + 60 structured practice MCQs (30 Basic, 20 UPSC-level, 10 Statement-based) = 70 total questions."
        )
    }
}
