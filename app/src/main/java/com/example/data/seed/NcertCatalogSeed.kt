package com.example.data.seed

import com.example.data.model.BookCatalogItem
import com.example.data.model.ChapterCatalogItem
import com.example.data.model.ContentStatus
import com.example.data.model.SyllabusSection

object NcertCatalogSeed {

    private fun ch(
        bookId: String,
        num: Int,
        title: String,
        titleHi: String,
        focus: String,
        full: Boolean = false
    ) = ChapterCatalogItem(
        chapterId = "${bookId}_ch$num",
        bookId = bookId,
        chapterNumber = num,
        title = title,
        titleHi = titleHi,
        keyFocus = focus,
        contentStatus = if (full) ContentStatus.FULL_CONTENT else ContentStatus.OUTLINE_AND_SYLLABUS
    )

    val allBooks: List<BookCatalogItem> = listOf(
        // ==================== A. NCERT FOUNDATION: HISTORY (Class 6-12) ====================
        BookCatalogItem(
            bookId = "ncert_hist_6",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 6 NCERT",
            title = "Our Pasts – I (Ancient India)",
            titleHi = "हमारे अतीत – I (प्राचीन भारत)",
            sourceInfo = "Official NCERT Textbook",
            chapters = listOf(
                ch("ncert_hist_6", 1, "What, Where, How and When?", "क्या, कब, कहाँ और कैसे?", "Sources, Narmada, Sulaiman-Kirthar, Garo, Indus & Ganga, Manuscripts vs Inscriptions, BCE/CE", full = true),
                ch("ncert_hist_6", 2, "From Hunting–Gathering to Growing Food", "आखेट–खाद्य संग्रह से भोजन उत्पादन तक", "Palaeolithic, Mesolithic, Neolithic, Bhimbetka, Mehrgarh"),
                ch("ncert_hist_6", 3, "In the Earliest Cities", "आरंभिक नगर", "Harappan Civilization, Mohenjodaro, Lothal, Town Planning"),
                ch("ncert_hist_6", 4, "What Books and Burials Tell Us", "क्या बताती हैं हमें किताबें और कब्रें", "Rigveda, Megaliths, Inamgaon"),
                ch("ncert_hist_6", 5, "Kingdoms, Kings and an Early Republic", "राज्य, राजा और एक प्राचीन गणराज्य", "Janapadas, Mahajanapadas, Varna, Vajji Sangha"),
                ch("ncert_hist_6", 6, "New Questions and Ideas", "नए प्रश्न नए विचार", "Buddha, Upanishads, Jainism, Sangha"),
                ch("ncert_hist_6", 7, "From a Kingdom to an Empire", "अशोक: एक अनोखा सम्राट जिसने युद्ध का त्याग किया", "Mauryan Empire, Ashoka Dhamma, Brahmi Inscriptions"),
                ch("ncert_hist_6", 8, "Villages, Towns and Trade", "खुशहाल गाँव और समृद्ध शहर", "Iron tools, Sangam literature, Arikamedu, Guilds"),
                ch("ncert_hist_6", 9, "New Empires and Kingdoms", "नए साम्राज्य और राज्य", "Samudragupta Prayaga Prashasti, Harshavardhana, Pallavas"),
                ch("ncert_hist_6", 10, "Buildings, Paintings and Books", "इमारतें, चित्र तथा किताबें", "Iron Pillar, Stupas, Ajanta, Puranas, Aryabhata")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_hist_7",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 7 NCERT",
            title = "Our Pasts – II (Medieval India)",
            titleHi = "हमारे अतीत – II (मध्यकालीन भारत)",
            sourceInfo = "Official NCERT Textbook",
            chapters = listOf(
                ch("ncert_hist_7", 1, "Tracing Changes Through a Thousand Years", "हज़ार वर्षों के दौरान हुए परिवर्तनों की पड़ताल", "Cartography, Hindustan terminology, Jatis, Archives"),
                ch("ncert_hist_7", 2, "New Kings and Kingdoms", "नए राजा और उनके राज्य", "Gurjara-Pratiharas, Rashtrakutas, Palas, Cholas"),
                ch("ncert_hist_7", 3, "The Delhi Sultans", "दिल्ली के सुल्तान", "Slave, Khalji, Tughluq, Sayyid, Lodi administration"),
                ch("ncert_hist_7", 4, "The Mughal Empire", "मुग़ल साम्राज्य", "Babur to Aurangzeb, Mansabdari, Zabti system"),
                ch("ncert_hist_7", 5, "Tribes, Nomads and Settled Communities", "जनजातियाँ, खानाबदोश और एक जगह बसे हुए समुदाय", "Gonds, Ahoms, Banjaras"),
                ch("ncert_hist_7", 6, "Devotional Paths to the Divine", "ईश्वर से अनुराग", "Bhakti & Sufi movements, Kabir, Nanak, Mirabai"),
                ch("ncert_hist_7", 7, "The Making of Regional Cultures", "क्षेत्रीय संस्कृतियों का निर्माण", "Jagannatha cult, Kathak, Miniature paintings"),
                ch("ncert_hist_7", 8, "Eighteenth-Century Political Formations", "अठारहवीं शताब्दी में नए राजनैतिक गठन", "Successor states, Marathas, Sikhs, Jats")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_hist_8",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 8 NCERT",
            title = "Our Pasts – III (Modern India)",
            titleHi = "हमारे अतीत – III (आधुनिक भारत)",
            sourceInfo = "Official NCERT Textbook",
            chapters = listOf(
                ch("ncert_hist_8", 1, "How, When and Where", "कैसे, कब और कहाँ", "Periodisation, Colonial records, Surveys"),
                ch("ncert_hist_8", 2, "From Trade to Territory", "व्यापार से साम्राज्य तक", "East India Company, Plassey, Buxar, Subsidiary Alliance"),
                ch("ncert_hist_8", 3, "Ruling the Countryside", "ग्रामीण क्षेत्र पर शासन चलाना", "Permanent Settlement, Mahalwari, Ryotwari, Indigo"),
                ch("ncert_hist_8", 4, "Tribals, Dikus and the Vision of a Golden Age", "आदिवासी, दीकु और एक स्वर्ण युग की कल्पना", "Birsa Munda, Santhal & Bastar rebellions"),
                ch("ncert_hist_8", 5, "When People Rebel: 1857 and After", "जब जनता बग़ावत करती है: 1857 और उसके बाद", "Revolt of 1857 causes, leaders, 1858 Act"),
                ch("ncert_hist_8", 6, "Civilising the Native, Educating the Nation", "देशी जनता को सभ्य बनाना और राष्ट्र को शिक्षित करना", "Wood's Despatch, Orientalists vs Anglicists"),
                ch("ncert_hist_8", 7, "Women, Caste and Reform", "महिलाएँ, जाति एवं सुधार", "Rammohun Roy, Vidyasagar, Phule, Ambedkar"),
                ch("ncert_hist_8", 8, "The Making of the National Movement: 1870s–1947", "राष्ट्रीय आंदोलन का संघटन: 1870 के दशक से 1947 तक", "INC, Swadeshi, Gandhian mass movements, Independence")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_hist_9",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 9 NCERT",
            title = "India and the Contemporary World – I",
            titleHi = "भारत और समकालीन विश्व – I",
            sourceInfo = "Official NCERT Textbook",
            chapters = listOf(
                ch("ncert_hist_9", 1, "The French Revolution", "फ़्रांसीसी क्रांति", "Liberty, Equality, Fraternity, 1789"),
                ch("ncert_hist_9", 2, "Socialism in Europe and the Russian Revolution", "यूरोप में समाजवाद एवं रूसी क्रांति", "Bolsheviks, 1917 Revolution, USSR"),
                ch("ncert_hist_9", 3, "Nazism and the Rise of Hitler", "नात्सीवाद और हिटलर का उदय", "Weimar Republic, World War II"),
                ch("ncert_hist_9", 4, "Forest Society and Colonialism", "वन्य समाज एवं उपनिवेशवाद", "Forest laws, Bastar, Java"),
                ch("ncert_hist_9", 5, "Pastoralists in the Modern World", "आधुनिक विश्व में चरवाहे", "Gaddis, Raikas, Maasai")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_hist_10",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 10 NCERT",
            title = "India and the Contemporary World – II",
            titleHi = "भारत और समकालीन विश्व – II",
            sourceInfo = "Official NCERT Textbook",
            chapters = listOf(
                ch("ncert_hist_10", 1, "The Rise of Nationalism in Europe", "यूरोप में राष्ट्रवाद का उदय", "Unification of Italy & Germany"),
                ch("ncert_hist_10", 2, "Nationalism in India", "भारत में राष्ट्रवाद", "Non-Cooperation, Civil Disobedience, Quit India"),
                ch("ncert_hist_10", 3, "The Making of a Global World", "भूमंडलीकृत विश्व का बनना", "Silk Routes, Bretton Woods, Decolonisation"),
                ch("ncert_hist_10", 4, "The Age of Industrialisation", "औद्योगीकरण का युग", "Proto-industrialisation, Indian weavers"),
                ch("ncert_hist_10", 5, "Print Culture and the Modern World", "मुद्रण संस्कृति और आधुनिक दुनिया", "Gutenberg, Indian press, Vernacular Act")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_hist_11",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 11 NCERT",
            title = "Themes in World History",
            titleHi = "विश्व इतिहास के कुछ विषय",
            sourceInfo = "Official NCERT Textbook",
            chapters = listOf(
                ch("ncert_hist_11", 1, "Writing and City Life", "लेखन कला और शहरी जीवन", "Mesopotamia, Cuneiform, Urbanism"),
                ch("ncert_hist_11", 2, "An Empire Across Three Continents", "तीन महाद्वीपों में फैला हुआ साम्राज्य", "Roman Empire society & economy"),
                ch("ncert_hist_11", 3, "Nomadic Empires", "यायावर साम्राज्य", "Genghis Khan & Mongols"),
                ch("ncert_hist_11", 4, "The Three Orders", "तीन वर्ग", "Feudalism in Europe"),
                ch("ncert_hist_11", 5, "Changing Cultural Traditions", "बदलती हुई सांस्कृतिक परंपराएँ", "Renaissance & Humanism"),
                ch("ncert_hist_11", 6, "Displacing Indigenous Peoples", "मूल निवासियों का विस्थापन", "Americas & Australia colonialism"),
                ch("ncert_hist_11", 7, "Paths to Modernisation", "आधुनिकीकरण के रास्ते", "Japan & China 19th–20th century")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_hist_12",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "History",
            classOrPaper = "Class 12 NCERT",
            title = "Themes in Indian History I, II & III",
            titleHi = "भारतीय इतिहास के कुछ विषय I, II एवं III",
            sourceInfo = "Official NCERT Textbooks (Parts 1–3)",
            chapters = listOf(
                ch("ncert_hist_12", 1, "Bricks, Beads and Bones (Harappan)", "ईंटें, मनके तथा अस्थियाँ", "Harappan archaeology & economy"),
                ch("ncert_hist_12", 2, "Kings, Farmers and Towns", "राजा, किसान और नगर", "Mauryan to Gupta polity & economy"),
                ch("ncert_hist_12", 3, "Kinship, Caste and Class", "बंधुत्व, जाति तथा वर्ग", "Mahabharata social history"),
                ch("ncert_hist_12", 4, "Thinkers, Beliefs and Buildings", "विचारक, विश्वास और इमारतें", "Sanchi, Buddhism, Jainism, Bhakti"),
                ch("ncert_hist_12", 5, "Through the Eyes of Travellers", "यात्रियों के नज़रिए", "Al-Biruni, Ibn Battuta, Bernier"),
                ch("ncert_hist_12", 6, "Bhakti-Sufi Traditions", "भक्ति-सूफी परंपराएँ", "Alvars, Nayanars, Chishti silsila"),
                ch("ncert_hist_12", 7, "An Imperial Capital: Vijayanagara", "एक साम्राज्य की राजधानी: विजयनगर", "Hampi, Krishnadeva Raya"),
                ch("ncert_hist_12", 8, "Peasants, Zamindars and the State", "किसान, ज़मींदार और राज्य", "Ain-i-Akbari, Mughal agrarian society"),
                ch("ncert_hist_12", 9, "Colonialism and the Countryside", "उपनिवेशवाद और देहात", "Santhals, Deccan Riots"),
                ch("ncert_hist_12", 10, "Rebels and the Raj (1857)", "विद्रोही और राज", "1857 Revolt narratives"),
                ch("ncert_hist_12", 11, "Mahatma Gandhi and the Nationalist Movement", "महात्मा गांधी और राष्ट्रीय आंदोलन", "Mass nationalism 1915–1948"),
                ch("ncert_hist_12", 12, "Framing the Constitution", "संविधान का निर्माण", "Constituent Assembly debates")
            )
        ),

        // ==================== ART & CULTURE ====================
        BookCatalogItem(
            bookId = "ncert_art_11",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Art and Culture",
            classOrPaper = "Class 11 NCERT",
            title = "An Introduction to Indian Art – Part I",
            titleHi = "भारतीय कला का परिचय – भाग 1",
            sourceInfo = "Official NCERT Fine Arts",
            chapters = listOf(
                ch("ncert_art_11", 1, "Prehistoric Rock Paintings", "प्रागैतिहासिक शैलचित्र", "Bhimbetka, Lakhudiyar"),
                ch("ncert_art_11", 2, "Arts of the Indus Valley", "सिंधु घाटी की कलाएँ", "Dancing Girl, Bearded Priest, Seals"),
                ch("ncert_art_11", 3, "Arts of the Mauryan Period", "मौर्यकालीन कला", "Pillars, Yaksha-Yakshini, Stupas"),
                ch("ncert_art_11", 4, "Post-Mauryan Trends in Indian Art & Architecture", "मौर्योत्तर कालीन कला परंपराएँ", "Gandhara, Mathura, Amaravati"),
                ch("ncert_art_11", 5, "Later Mural Traditions", "परवर्ती भित्तिचित्र परंपराएँ", "Ajanta, Bagh, Badami, Lepakshi"),
                ch("ncert_art_11", 6, "Temple Architecture and Sculpture", "मंदिर स्थापत्य और मूर्तिकला", "Nagara, Dravida, Vesara styles"),
                ch("ncert_art_11", 7, "Indian Bronze Sculpture", "भारतीय कांस्य मूर्तिकला", "Chola Nataraja, Lost-wax technique"),
                ch("ncert_art_11", 8, "Some Aspects of Indo-Islamic Architecture", "इंडो-इस्लामिक वास्तुकला", "Arcuate style, Qutb, Taj Mahal")
            )
        ),

        // ==================== GEOGRAPHY (Class 6-12) ====================
        BookCatalogItem(
            bookId = "ncert_geo_6_10",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Geography",
            classOrPaper = "Class 6–10 NCERT",
            title = "Earth, Environment, India & Resources",
            titleHi = "पृथ्वी, पर्यावरण एवं समकालीन भारत (कक्षा 6–10)",
            sourceInfo = "Official NCERT Geography",
            chapters = listOf(
                ch("ncert_geo_6_10", 1, "The Earth in the Solar System & Latitudes/Longitudes", "सौरमंडल में पृथ्वी एवं अक्षांश-देशांतर", "Time zones, Heat zones, Motions"),
                ch("ncert_geo_6_10", 2, "Major Domains & Landforms of the Earth", "पृथ्वी के प्रमुख परिमंडल एवं स्थलरूप", "Lithosphere, Mountains, Plateaus"),
                ch("ncert_geo_6_10", 3, "Our Changing Earth & Air/Water (Class 7)", "हमारी बदलती पृथ्वी, वायु और जल", "Plate tectonics, Atmosphere layers, Ocean currents"),
                ch("ncert_geo_6_10", 4, "Resources, Agriculture & Industries (Class 8)", "संसाधन, कृषि और उद्योग", "Soil types, Cropping patterns"),
                ch("ncert_geo_6_10", 5, "India: Size, Physical Features & Drainage (Class 9)", "भारत: आकार, भौतिक स्वरूप और अपवाह तंत्र", "Himalayas, Peninsular rivers, Monsoon"),
                ch("ncert_geo_6_10", 6, "Resources, Minerals, Water & Manufacturing (Class 10)", "संसाधन, खनिज और विनिर्माण उद्योग", "Conservation, Energy, Transport")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_geo_11_12",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Geography",
            classOrPaper = "Class 11–12 NCERT",
            title = "Fundamentals of Physical & Indian Geography",
            titleHi = "भौतिक भूगोल के मूल सिद्धांत एवं भारत: भौतिक पर्यावरण",
            sourceInfo = "Official NCERT Class 11–12",
            chapters = listOf(
                ch("ncert_geo_11_12", 1, "Geomorphic Processes & Landforms", "भू-आकृतिक प्रक्रियाएँ", "Weathering, Mass movement, Fluvial/Aeolian"),
                ch("ncert_geo_11_12", 2, "Atmosphere: Composition, Solar Radiation & Winds", "वायुमंडल: संरचना, सूर्यातप और पवन", "ITCZ, Jet Streams, Cyclones"),
                ch("ncert_geo_11_12", 3, "Oceanography: Salinity, Waves, Tides & Currents", "महासागरीय जल एवं संचलन", "Thermohaline circulation, El Nino"),
                ch("ncert_geo_11_12", 4, "Indian Climate, Monsoon & Natural Vegetation", "भारतीय जलवायु, मानसून एवं वनस्पति", "Koeppen, Forest types, Soils"),
                ch("ncert_geo_11_12", 5, "Human Geography: Population, Migration & Settlements", "मानव भूगोल: जनसंख्या और बस्तियाँ", "Demographic transition, Urbanisation")
            )
        ),

        // ==================== POLITICAL SCIENCE (Class 6-12) ====================
        BookCatalogItem(
            bookId = "ncert_pol_6_10",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Political Science",
            classOrPaper = "Class 6–10 NCERT",
            title = "Social & Political Life + Democratic Politics",
            titleHi = "सामाजिक एवं राजनीतिक जीवन तथा लोकतांत्रिक राजनीति (कक्षा 6–10)",
            sourceInfo = "Official NCERT Civics",
            chapters = listOf(
                ch("ncert_pol_6_10", 1, "Diversity, Government & Local Self-Government (Class 6)", "विविधता, सरकार और स्थानीय स्वशासन", "Panchayati Raj, Municipal bodies"),
                ch("ncert_pol_6_10", 2, "Equality, State Government & Media (Class 7)", "समानता, राज्य शासन और मीडिया", "Democracy pillars"),
                ch("ncert_pol_6_10", 3, "Indian Constitution, Secularism & Judiciary (Class 8)", "भारतीय संविधान, धर्मनिरपेक्षता और न्यायपालिका", "Fundamental Rights, Parliamentary system"),
                ch("ncert_pol_6_10", 4, "Constitutional Design, Elections & Rights (Class 9)", "संविधान निर्माण, चुनावी राजनीति और अधिकार", "Democratic institutions"),
                ch("ncert_pol_6_10", 5, "Power Sharing, Federalism & Political Parties (Class 10)", "सत्ता की साझेदारी, संघवाद और राजनीतिक दल", "Centre-state relations, Outcomes of democracy")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_pol_11_12",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Political Science",
            classOrPaper = "Class 11–12 NCERT",
            title = "Indian Constitution at Work & Political Theory",
            titleHi = "भारत का संविधान: सिद्धांत और व्यवहार एवं राजनीतिक सिद्धांत",
            sourceInfo = "Official NCERT Class 11–12",
            chapters = listOf(
                ch("ncert_pol_11_12", 1, "Constitution: Why and How & Rights", "संविधान: क्यों और कैसे तथा अधिकार", "Fundamental Rights, DPSP, Fundamental Duties"),
                ch("ncert_pol_11_12", 2, "Executive, Legislature & Judiciary", "कार्यपालिका, विधायिका और न्यायपालिका", "Parliament, President, PM, Supreme Court"),
                ch("ncert_pol_11_12", 3, "Federalism & Local Governments", "संघवाद और स्थानीय शासन", "73rd & 74th Amendments"),
                ch("ncert_pol_11_12", 4, "Political Theory: Liberty, Equality, Justice & Secularism", "स्वतंत्रता, समानता, न्याय और धर्मनिरपेक्षता", "Core UPSC Essay & GS II concepts"),
                ch("ncert_pol_11_12", 5, "Politics in India Since Independence (Class 12)", "स्वतंत्र भारत में राजनीति", "Integration of states, Emergency, Coalition era"),
                ch("ncert_pol_11_12", 6, "Contemporary World Politics (Class 12)", "समकालीन विश्व राजनीति", "Cold War, UN, Globalisation, South Asia")
            )
        ),

        // ==================== ECONOMICS (Class 9-12) ====================
        BookCatalogItem(
            bookId = "ncert_eco_9_12",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Economics",
            classOrPaper = "Class 9–12 NCERT",
            title = "Economics, Indian Economic Development & Macroeconomics",
            titleHi = "अर्थशास्त्र, भारतीय अर्थव्यवस्था का विकास एवं समष्टि अर्थशास्त्र",
            sourceInfo = "Official NCERT Class 9–12",
            chapters = listOf(
                ch("ncert_eco_9_12", 1, "Factors of Production, Human Capital & Poverty (Class 9)", "उत्पादन के कारक, मानव पूँजी और निर्धनता", "Palampur, Food security"),
                ch("ncert_eco_9_12", 2, "Sectors of Economy, Money & Credit (Class 10)", "अर्थव्यवस्था के क्षेत्रक, मुद्रा और साख", "Primary/Secondary/Tertiary, RBI, SHGs"),
                ch("ncert_eco_9_12", 3, "Indian Economy 1950–1990 & 1991 LPG Reforms (Class 11)", "भारतीय अर्थव्यवस्था 1950–1990 और 1991 सुधार", "Five-Year Plans, Liberalisation, Privatisation, Globalisation"),
                ch("ncert_eco_9_12", 4, "National Income Accounting: GDP, GNP & Inflation (Class 12)", "राष्ट्रीय आय लेखांकन: GDP, GNP एवं मुद्रास्फीति", "Nominal vs Real GDP, Deflator"),
                ch("ncert_eco_9_12", 5, "Money, Banking, Monetary Policy & Fiscal Budget (Class 12)", "मुद्रा, बैंकिंग, मौद्रिक नीति एवं सरकारी बजट", "Repo rate, CRR, SLR, Fiscal Deficit, BoP")
            )
        ),

        // ==================== SOCIOLOGY & SOCIETY ====================
        BookCatalogItem(
            bookId = "ncert_soc_11_12",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Sociology and Society",
            classOrPaper = "Class 11–12 NCERT",
            title = "Introducing Sociology & Indian Society",
            titleHi = "समाजशास्त्र का परिचय एवं भारतीय समाज",
            sourceInfo = "Official NCERT Class 11–12",
            chapters = listOf(
                ch("ncert_soc_11_12", 1, "Demographic Structure of Indian Society", "भारतीय समाज की जनसांख्यिकीय संरचना", "Demographic dividend, Sex ratio"),
                ch("ncert_soc_11_12", 2, "Social Institutions: Caste, Tribe & Family", "सामाजिक संस्थाएँ: जाति, जनजाति और परिवार", "Continuity and change"),
                ch("ncert_soc_11_12", 3, "Patterns of Social Inequality & Exclusion", "सामाजिक विषमता और बहिष्कार के स्वरूप", "Untouchability, Gender, Disability"),
                ch("ncert_soc_11_12", 4, "Challenges of Cultural Diversity", "सांस्कृतिक विविधता की चुनौतियाँ", "Regionalism, Communalism, Secularism")
            )
        ),

        // ==================== SCIENCE, BIOLOGY, PHYSICS & CHEMISTRY, ENVIRONMENT ====================
        BookCatalogItem(
            bookId = "ncert_sci_6_10",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Science",
            classOrPaper = "Class 6–10 NCERT",
            title = "General Science Foundation (UPSC Prelims)",
            titleHi = "सामान्य विज्ञान आधार (कक्षा 6–10)",
            sourceInfo = "Official NCERT Science",
            chapters = listOf(
                ch("ncert_sci_6_10", 1, "Nutrition, Health, Microorganisms & Immunity", "पोषण, स्वास्थ्य, सूक्ष्मजीव एवं प्रतिरक्षा", "Vitamins, Vaccines, Pathogens"),
                ch("ncert_sci_6_10", 2, "Cell Structure, Genetics & Control/Coordination", "कोशिका संरचना, आनुवंशिकता एवं समन्वय", "DNA, Hormones, Heredity"),
                ch("ncert_sci_6_10", 3, "Acids, Bases, Metals, Carbon & Periodic Concepts", "अम्ल, क्षार, धातु, कार्बन और यौगिक", "Everyday chemistry for Prelims"),
                ch("ncert_sci_6_10", 4, "Light, Electricity, Magnetism & Energy Sources", "प्रकाश, विद्युत, चुंबकत्व और ऊर्जा स्रोत", "Optics, Nuclear & Renewable energy")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_bio_env_12",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Biology & Environment",
            classOrPaper = "Class 12 NCERT",
            title = "Biotechnology, Ecology & Biodiversity (Biology Last 4 Chapters)",
            titleHi = "जैव प्रौद्योगिकी, पारिस्थितिकी एवं जैव विविधता",
            sourceInfo = "Official NCERT Class 12 Biology",
            chapters = listOf(
                ch("ncert_bio_env_12", 1, "Biotechnology: Principles & Applications", "जैव प्रौद्योगिकी: सिद्धांत एवं अनुप्रयोग", "CRISPR, GM Crops, PCR, Biopiracy"),
                ch("ncert_bio_env_12", 2, "Organisms and Populations", "जीव और समष्टियाँ", "Adaptations, Biomes, Population interactions"),
                ch("ncert_bio_env_12", 3, "Ecosystem: Energy Flow & Nutrient Cycles", "पारितंत्र: ऊर्जा प्रवाह और पोषक चक्र", "10% Law, Food webs, Ecological pyramids"),
                ch("ncert_bio_env_12", 4, "Biodiversity and Conservation", "जैव विविधता एवं संरक्षण", "In-situ vs Ex-situ, Hotspots, IUCN")
            )
        ),
        BookCatalogItem(
            bookId = "ncert_phy_chem_upsc",
            section = SyllabusSection.NCERT_FOUNDATION,
            subject = "Physics & Chemistry (UPSC Topics)",
            classOrPaper = "Class 11–12 Selected",
            title = "Applied Physics & Chemistry for UPSC S&T",
            titleHi = "यूपीएससी विज्ञान-प्रौद्योगिकी हेतु भौतिकी एवं रसायन",
            sourceInfo = "NCERT Class 11–12 Selected Modules",
            chapters = listOf(
                ch("ncert_phy_chem_upsc", 1, "Electromagnetic Spectrum, Space & Satellites", "विद्युत-चुंबकीय स्पेक्ट्रम, अंतरिक्ष एवं उपग्रह", "Orbits, PSLV/GSLV, Communication waves"),
                ch("ncert_phy_chem_upsc", 2, "Nuclear Physics, Semiconductors & Quantum Basics", "नाभिकीय भौतिकी, अर्धचालक और क्वांटम", "Fission vs Fusion, Chips, Quantum computing"),
                ch("ncert_phy_chem_upsc", 3, "Environmental Chemistry, Polymers & Green Hydrogen", "पर्यावरणीय रसायन, बहुलक और हरित हाइड्रोजन", "Ozone, Photochemical smog, Batteries")
            )
        ),

        // ==================== B1. UPSC PRELIMS ====================
        BookCatalogItem(
            bookId = "upsc_prelims_gs1",
            section = SyllabusSection.UPSC_PRELIMS,
            subject = "Prelims GS I",
            classOrPaper = "UPSC CSE Prelims",
            title = "General Studies Paper I (Official Syllabus)",
            titleHi = "सामान्य अध्ययन पेपर I (प्रारंभिक परीक्षा)",
            sourceInfo = "Official UPSC CSE Notification",
            chapters = listOf(
                ch("upsc_prelims_gs1", 1, "Current Events of National & International Importance", "राष्ट्रीय एवं अंतर्राष्ट्रीय महत्व की समसामयिक घटनाएँ", "PIB, Policies, Summits"),
                ch("upsc_prelims_gs1", 2, "History of India & Indian National Movement", "भारत का इतिहास एवं भारतीय राष्ट्रीय आंदोलन", "Ancient, Medieval, Modern, Art & Culture"),
                ch("upsc_prelims_gs1", 3, "Indian and World Geography", "भारत एवं विश्व भूगोल", "Physical, Social, Economic Geography"),
                ch("upsc_prelims_gs1", 4, "Indian Polity and Governance", "भारतीय राजव्यवस्था एवं शासन", "Constitution, Panchayati Raj, Public Policy"),
                ch("upsc_prelims_gs1", 5, "Economic and Social Development", "आर्थिक एवं सामाजिक विकास", "Sustainable Development, Poverty, Inclusion"),
                ch("upsc_prelims_gs1", 6, "Environmental Ecology, Biodiversity & Climate Change", "पर्यावरण पारिस्थितिकी, जैव विविधता और जलवायु परिवर्तन", "General issues (no subject specialization)"),
                ch("upsc_prelims_gs1", 7, "General Science", "सामान्य विज्ञान", "Everyday science & emerging tech")
            )
        ),
        BookCatalogItem(
            bookId = "upsc_prelims_csat",
            section = SyllabusSection.UPSC_PRELIMS,
            subject = "CSAT Paper II",
            classOrPaper = "Qualifying (33%)",
            title = "CSAT Paper II (Comprehension, Maths & Reasoning)",
            titleHi = "सीसैट पेपर II (बोधगम्यता, गणित एवं तर्कशक्ति)",
            sourceInfo = "Official UPSC CSE Notification",
            chapters = listOf(
                ch("upsc_prelims_csat", 1, "Reading Comprehension & Inference", "अपठित गद्यांश और निष्कर्ष", "Assumptions, Corollaries, Main Idea"),
                ch("upsc_prelims_csat", 2, "Number System, Percentages & Ratio", "संख्या पद्धति, प्रतिशत एवं अनुपात", "High-frequency UPSC CSAT numeracy"),
                ch("upsc_prelims_csat", 3, "Permutation, Probability & Data Interpretation", "क्रमचय, प्रायिकता एवं आंकड़ा निर्वचन", "Recent 2022–2024 CSAT trends"),
                ch("upsc_prelims_csat", 4, "Logical Reasoning & Analytical Ability", "तार्किक एवं विश्लेषणात्मक क्षमता", "Syllogism, Blood relations, Seating")
            )
        ),

        // ==================== B2. UPSC MAINS ====================
        BookCatalogItem(
            bookId = "upsc_mains_gs",
            section = SyllabusSection.UPSC_MAINS,
            subject = "Mains GS I–IV",
            classOrPaper = "UPSC Mains Papers",
            title = "General Studies I, II, III & IV + Essay + Languages",
            titleHi = "मुख्य परीक्षा GS I–IV, निबंध एवं अर्हक भाषा पेपर",
            sourceInfo = "Official UPSC CSE Mains Syllabus",
            chapters = listOf(
                ch("upsc_mains_gs", 1, "GS Paper I: Heritage, Culture, History, Society & Geography", "GS I: विरासत, संस्कृति, इतिहास, समाज और भूगोल", "250 Marks — Analytical Mains"),
                ch("upsc_mains_gs", 2, "GS Paper II: Governance, Constitution, Polity, Social Justice & IR", "GS II: शासन, संविधान, राजव्यवस्था, सामाजिक न्याय और IR", "250 Marks — Articles & Judgments"),
                ch("upsc_mains_gs", 3, "GS Paper III: Tech, Economy, Bio-diversity, Security & Disaster Mgmt", "GS III: अर्थव्यवस्था, प्रौद्योगिकी, पर्यावरण, सुरक्षा एवं आपदा प्रबंधन", "250 Marks — Data & Schemes"),
                ch("upsc_mains_gs", 4, "GS Paper IV: Ethics, Integrity and Aptitude", "GS IV: नीतिशास्त्र, सत्यनिष्ठा और अभिरुचि", "250 Marks — Theory + Case Studies"),
                ch("upsc_mains_gs", 5, "Essay Paper (Section A & Section B)", "निबंध पेपर (खंड A एवं खंड B)", "250 Marks — 2 Essays (1000–1200 words)"),
                ch("upsc_mains_gs", 6, "Qualifying Language Papers (Hindi & English)", "अर्हक भाषा पेपर (हिंदी एवं अंग्रेज़ी)", "300 Marks each — 25% qualifying")
            )
        ),

        // ==================== C. HISTORY OPTIONAL ====================
        BookCatalogItem(
            bookId = "hist_opt_p1",
            section = SyllabusSection.HISTORY_OPTIONAL,
            subject = "History Optional Paper I",
            classOrPaper = "Optional Paper I (250M)",
            title = "Ancient & Medieval India + Map Work + Historiography",
            titleHi = "इतिहास वैकल्पिक पेपर I (प्राचीन एवं मध्यकालीन भारत)",
            sourceInfo = "Official UPSC History Optional Syllabus",
            chapters = listOf(
                ch("hist_opt_p1", 1, "Sources & Historiography: Archaeological & Literary", "स्रोत एवं इतिहासलेखन: पुरातात्विक व साहित्यिक", "Orientalist, Nationalist, Marxist, Subaltern interpretations"),
                ch("hist_opt_p1", 2, "Pre-history, Proto-history & Indus Civilization", "प्रागैतिहास, आद्य-इतिहास और हड़प्पा सभ्यता", "Origins, town planning, decline debates"),
                ch("hist_opt_p1", 3, "Vedic Age, Mahajanapadas, Buddhism & Mauryas", "वैदिक युग, महाजनपद, बौद्ध धर्म और मौर्य", "State formation, Dhamma, Arthashastra"),
                ch("hist_opt_p1", 4, "Guptas, Post-Guptas & Early Medieval Feudalism Debate", "गुप्त काल, उत्तर-गुप्त एवं भारतीय सामंतवाद बहस", "R.S. Sharma vs Harbans Mukhia"),
                ch("hist_opt_p1", 5, "Delhi Sultanate, Vijayanagara & Mughal Empire", "दिल्ली सल्तनत, विजयनगर और मुग़ल साम्राज्य", "Iqta, Mansab, Jagirdari crisis, Bhakti-Sufi"),
                ch("hist_opt_p1", 6, "20 Historical Map Sites Practice", "20 ऐतिहासिक मानचित्र स्थल अभ्यास", "50 compulsory marks in Paper I Q1")
            )
        ),
        BookCatalogItem(
            bookId = "hist_opt_p2",
            section = SyllabusSection.HISTORY_OPTIONAL,
            subject = "History Optional Paper II",
            classOrPaper = "Optional Paper II (250M)",
            title = "Modern India & World History (1750–1991)",
            titleHi = "इतिहास वैकल्पिक पेपर II (आधुनिक भारत एवं विश्व इतिहास)",
            sourceInfo = "Official UPSC History Optional Syllabus",
            chapters = listOf(
                ch("hist_opt_p2", 1, "European Penetration & British Expansion in India", "यूरोपीय आगमन और भारत में ब्रिटिश विस्तार", "Mercantilism, Plassey, Drain of Wealth"),
                ch("hist_opt_p2", 2, "Social Reform, 1857 & Indian Freedom Struggle", "सामाजिक सुधार, 1857 और स्वतंत्रता संग्राम", "Historiography of 1857, Gandhian mass politics"),
                ch("hist_opt_p2", 3, "Post-Independence Consolidation (1947–1964)", "स्वतंत्रता के बाद भारत का एकीकरण", "Linguistic states, Planning, NAM"),
                ch("hist_opt_p2", 4, "Enlightenment, Industrial Revolution & World Revolutions", "प्रबोधन, औद्योगिक क्रांति एवं विश्व क्रांतियाँ", "American, French, Russian Revolutions"),
                ch("hist_opt_p2", 5, "World Wars, Cold War, Decolonisation & 1991", "विश्व युद्ध, शीत युद्ध, विउपनिवेशीकरण और 1991", "UN, USSR collapse, Globalisation")
            )
        ),

        // ==================== D. OTHER AREAS ====================
        BookCatalogItem(
            bookId = "upsc_other_areas",
            section = SyllabusSection.OTHER_AREAS,
            subject = "IR, Security, Governance & English",
            classOrPaper = "GS II, III, IV & Personality",
            title = "IR, Internal Security, Disaster Mgmt, Governance & English",
            titleHi = "अंतर्राष्ट्रीय संबंध, आंतरिक सुरक्षा, आपदा प्रबंधन एवं अंग्रेज़ी",
            sourceInfo = "Official UPSC Mains & Interview Framework",
            chapters = listOf(
                ch("upsc_other_areas", 1, "India and Its Neighbourhood & Foreign Policy", "भारत एवं पड़ोसी देश तथा विदेश नीति", "Neighbourhood First, Act East, QUAD, Global South"),
                ch("upsc_other_areas", 2, "Internal Security & Border Management", "आंतरिक सुरक्षा और सीमा प्रबंधन", "Cyber security, LWE, Money laundering"),
                ch("upsc_other_areas", 3, "Disaster Management & NDMA Framework", "आपदा प्रबंधन और NDMA ढांचा", "Sendai Framework, Climate resilience"),
                ch("upsc_other_areas", 4, "Governance, Transparency, RTI & Citizen Charters", "शासन, पारदर्शिता, RTI और नागरिक चार्टर", "E-governance, Social Audit"),
                ch("upsc_other_areas", 5, "English Communication & Personality Test Readiness", "अंग्रेज़ी संवाद एवं साक्षात्कार तैयारी", "Daily 45-min bilingual fluency builder")
            )
        )
    )

    fun totalChaptersCount(): Int = allBooks.sumOf { it.chapters.size }

    fun fullContentChaptersCount(): Int =
        allBooks.sumOf { book -> book.chapters.count { it.contentStatus == ContentStatus.FULL_CONTENT } }

    fun findChapter(chapterId: String): ChapterCatalogItem? =
        allBooks.asSequence().flatMap { it.chapters.asSequence() }.firstOrNull { it.chapterId == chapterId }

    fun findBook(bookId: String): BookCatalogItem? =
        allBooks.firstOrNull { it.bookId == bookId }
}
