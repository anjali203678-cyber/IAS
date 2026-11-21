package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.BtsMember
import com.example.data.model.FlashcardItem
import com.example.data.model.MasteryStatus
import com.example.data.model.QuoteSlot
import com.example.data.model.UiState
import com.example.data.model.UserProfile
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.PurpleDreamCard
import com.example.ui.theme.SoftLilac
import com.example.ui.theme.SuccessMint
import com.example.ui.viewmodel.CivilsViewModel

@Composable
fun FlashcardsAndMotivationScreen(
    viewModel: CivilsViewModel,
    initialTab: Int = 0
) {
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("7 BTS Flashcard Decks") },
                modifier = Modifier.testTag("flashcards_tab")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("7 Members Motivation") },
                modifier = Modifier.testTag("motivation_tab")
            )
        }

        if (selectedTab == 0) {
            FlashcardDecksTab(viewModel = viewModel)
        } else {
            MotivationCornerTab(viewModel = viewModel)
        }
    }
}

@Composable
private fun FlashcardDecksTab(viewModel: CivilsViewModel) {
    val flashcardsState by viewModel.flashcardsState.collectAsStateWithLifecycle()
    val dailyTarget by viewModel.dailyFlashcardTarget.collectAsStateWithLifecycle()
    val allCards = (flashcardsState as? UiState.Success)?.data ?: emptyList()

    var selectedMember by remember { mutableStateOf<BtsMember?>(null) }
    var filterMode by remember { mutableStateOf("ALL") } // ALL, DUE, FAV, DIFFICULT
    var flippedCardIds by remember { mutableStateOf(setOf<String>()) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val now = System.currentTimeMillis()
    val filteredCards = remember(allCards, selectedMember, filterMode) {
        allCards.filter { card ->
            val memberOk = selectedMember == null || card.deckMember.equals(selectedMember!!.name, ignoreCase = true)
            val modeOk = when (filterMode) {
                "DUE" -> card.nextReviewEpochMs <= now
                "FAV" -> card.favourite
                "DIFFICULT" -> card.masteryStatus == MasteryStatus.DIFFICULT.name
                else -> true
            }
            memberOk && modeOk
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_flashcard_garden),
                        contentDescription = "BTS Flashcard Garden",
                        modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("BTS Spaced Repetition Decks", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = "${allCards.size} total cards • Reviewed today: ${allCards.count { it.reviewCount > 0 }} / $dailyTarget daily goal",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier.testTag("add_flashcard_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Create flashcard")
                            Spacer(Modifier.width(4.dp))
                            Text("New")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedMember == null,
                            onClick = { selectedMember = null },
                            label = { Text("All 7 Decks") }
                        )
                        BtsMember.entries.forEach { member ->
                            FilterChip(
                                selected = selectedMember == member,
                                onClick = { selectedMember = member },
                                label = { Text("${member.emblemSymbol} ${member.deckName}") }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("ALL" to "All", "DUE" to "Due Now", "FAV" to "Favourites", "DIFFICULT" to "Difficult").forEach { (key, label) ->
                            FilterChip(
                                selected = filterMode == key,
                                onClick = { filterMode = key },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }
        }

        items(filteredCards, key = { it.id }) { card ->
            val member = BtsMember.safeValueOf(card.deckMember)
            val isFlipped = card.id in flippedCardIds
            PurpleDreamCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        flippedCardIds = if (isFlipped) flippedCardIds - card.id else flippedCardIds + card.id
                    }
                    .testTag("flashcard_${card.id}"),
                borderColor = member.accentColor.copy(alpha = 0.65f)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(color = member.accentColor.copy(alpha = 0.16f), shape = RoundedCornerShape(8.dp)) {
                            Text(
                                text = "${member.emblemSymbol} ${member.deckName} • ${card.subject}",
                                style = MaterialTheme.typography.labelMedium,
                                color = member.accentColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.toggleFlashcardFavourite(card) }) {
                                Icon(
                                    imageVector = if (card.favourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favourite card",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Icon(Icons.Default.Flip, contentDescription = "Tap to flip card", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Text(
                        text = if (!isFlipped) card.front else card.back,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (isFlipped) {
                        if (card.explanation.isNotBlank()) {
                            Text(card.explanation, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            text = "Source: ${card.sourceRef} • Interval: ${card.intervalDays}d (${card.masteryStatus})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.reviewFlashcard(card, MasteryStatus.KNOWN) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Know It", maxLines = 1)
                            }
                            OutlinedButton(
                                onClick = { viewModel.reviewFlashcard(card, MasteryStatus.REVISION) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Revise", maxLines = 1)
                            }
                            OutlinedButton(
                                onClick = { viewModel.reviewFlashcard(card, MasteryStatus.DIFFICULT) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Hard", maxLines = 1)
                            }
                        }
                    } else {
                        Text(
                            text = "Tap card to reveal answer & Hindi explanation",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateFlashcardDialog(
            defaultMember = selectedMember ?: BtsMember.RM,
            onDismiss = { showCreateDialog = false },
            onSave = { member, subj, front, back, exp, src ->
                viewModel.saveCustomFlashcard(null, member, subj, front, back, exp, src)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun MotivationCornerTab(viewModel: CivilsViewModel) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val historyState by viewModel.quoteHistoryState.collectAsStateWithLifecycle()
    val activeBanner by viewModel.activeMotivation.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGeneratingQuotes.collectAsStateWithLifecycle()
    val pool by viewModel.quotePool.collectAsStateWithLifecycle()

    val profile = (profileState as? UiState.Success)?.data ?: UserProfile()
    val history = (historyState as? UiState.Success)?.data ?: emptyList()
    var filterMember by remember { mutableStateOf<BtsMember?>(null) }
    var showFavsOnly by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_bts_motivation_stage),
                        contentDescription = "Purple Dream 7-Star Motivation Stage",
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Brush.verticalGradient(listOf(Color.Transparent, DeepPurple.copy(alpha = 0.88f))))
                    )
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                    ) {
                        Text("7 Members Motivation Corner", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                        Text(
                            text = "Non-repeating 140+ quotes • Distinct Verified Quotes vs Original Motivation",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftLilac
                        )
                    }
                }
            }
        }

        // Member selector & favourite bias
        item {
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Choose Member Theme or Set Your Favourite (${profile.favouriteMember})", style = MaterialTheme.typography.titleSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filterMember == null,
                            onClick = { filterMember = null },
                            label = { Text("All 7") }
                        )
                        BtsMember.entries.forEach { m ->
                            FilterChip(
                                selected = filterMember == m,
                                onClick = {
                                    filterMember = m
                                    viewModel.showNextMotivation(slot = QuoteSlot.SHUFFLE, preferredMember = m)
                                },
                                label = { Text("${m.emblemSymbol} ${m.displayName}") }
                            )
                        }
                    }
                    filterMember?.let { selected ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selected.motivationTheme, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                            OutlinedButton(onClick = { viewModel.selectFavouriteMember(selected) }) {
                                Icon(Icons.Default.Star, contentDescription = "Set favourite")
                                Spacer(Modifier.width(4.dp))
                                Text("Set Favourite")
                            }
                        }
                    }
                }
            }
        }

        // Active Quote Card
        item {
            val active = activeBanner
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${active?.quote?.member?.displayName ?: "RM"} • ${active?.quote?.labelType?.badgeText ?: "Original Motivation"}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("${active?.remainingUnseenCount ?: pool.size} unseen left", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(active?.quote?.text ?: "Tap Shuffle to draw a fresh non-repeating message.", style = MaterialTheme.typography.bodyLarge)
                    Text(active?.quote?.sourceNote ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.showNextMotivation(slot = QuoteSlot.SHUFFLE, preferredMember = filterMember) },
                            modifier = Modifier.weight(1f).testTag("motivation_shuffle_button")
                        ) {
                            Icon(Icons.Default.Shuffle, contentDescription = "Next unseen quote")
                            Spacer(Modifier.width(6.dp))
                            Text("Next Unseen")
                        }
                        OutlinedButton(
                            onClick = { viewModel.generateNewMotivationBatchWithAi() },
                            enabled = !isGenerating,
                            modifier = Modifier.weight(1f).testTag("motivation_ai_batch_button")
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = "Generate AI batch")
                                Spacer(Modifier.width(6.dp))
                                Text("AI Batch")
                            }
                        }
                    }
                }
            }
        }

        // Quote History Filter + List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Seen Quote History (${history.size})", style = MaterialTheme.typography.titleMedium)
                FilterChip(
                    selected = showFavsOnly,
                    onClick = { showFavsOnly = !showFavsOnly },
                    label = { Text("Favourites Only") }
                )
            }
        }

        val displayedHistory = history.filter {
            (!showFavsOnly || it.favourite) &&
                (filterMember == null || it.member.equals(filterMember!!.name, ignoreCase = true))
        }
        items(displayedHistory, key = { it.id }) { entry ->
            PurpleDreamCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "${entry.member} • ${entry.displayedDate} • ${entry.slot}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(entry.text, style = MaterialTheme.typography.bodyMedium)
                        Text(entry.sourceNote, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { viewModel.toggleQuoteHistoryFavourite(entry) }) {
                        Icon(
                            imageVector = if (entry.favourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favourite history quote",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateFlashcardDialog(
    defaultMember: BtsMember,
    onDismiss: () -> Unit,
    onSave: (BtsMember, String, String, String, String, String) -> Unit
) {
    var member by remember { mutableStateOf(defaultMember) }
    var subject by remember { mutableStateOf("History") }
    var front by remember { mutableStateOf("") }
    var back by remember { mutableStateOf("") }
    var explanation by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("NCERT Notes") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Flashcard") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    BtsMember.entries.forEach { m ->
                        FilterChip(selected = member == m, onClick = { member = m }, label = { Text(m.displayName) })
                    }
                }
                OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, singleLine = true)
                OutlinedTextField(value = front, onValueChange = { front = it }, label = { Text("Front (Question / Concept)") })
                OutlinedTextField(value = back, onValueChange = { back = it }, label = { Text("Back (Answer)") })
                OutlinedTextField(value = explanation, onValueChange = { explanation = it }, label = { Text("Hindi Explanation / Memory Trick") })
                OutlinedTextField(value = source, onValueChange = { source = it }, label = { Text("Source Reference") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(member, subject, front, back, explanation, source) },
                enabled = front.isNotBlank() && back.isNotBlank()
            ) {
                Text("Save Card")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
