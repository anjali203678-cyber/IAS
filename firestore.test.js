const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_ias_2029";
const BOB_UID = "bob_ias_2029";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: denied all reads and writes", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("study_tasks").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Anjali",
    })
  );
});

test("Authenticated owner: UserProfile create, partial update, and shadow field rejection", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const past = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Anjali UPSC 2029",
      targetAttemptYear: 2029,
      syllabusTargetDate: "December 2028",
      optionalSubject: "History",
      primaryLanguage: "Hindi",
      dailyStudyTargetMinutes: 540,
      englishTargetMinutes: 45,
      themeMode: "LIGHT_LAVENDER",
      favouriteMember: "RM",
      streakDays: 1,
      totalStars: 7,
      lastStudyDate: "2026-11-21",
      createdAt: past,
      updatedAt: past,
    })
  );

  // Partial update allowed fields
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      themeMode: "DARK_GALAXY",
      streakDays: 2,
      totalStars: 14,
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Reject ghost field & immutable createdAt overwrite
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isAdmin: true,
      updatedAt: new Date(Date.now() - 500),
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      createdAt: new Date(Date.now() - 100),
      updatedAt: new Date(Date.now() - 100),
    })
  );
});

test("Cross-user isolation: Bob cannot read or write Alice's chapter_progress or study_tasks", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const past = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("chapter_progress")
      .doc("ncert_hist_6_ch1")
      .set({
        chapterId: "ncert_hist_6_ch1",
        bookId: "ncert_hist_6",
        completed: false,
        downloadedOffline: true,
        notesRead: true,
        memoryDone: false,
        quizDone: false,
        bestScorePercent: 85,
        revisionStage: 1,
        lastRevisedEpochMs: 1700000000000,
        nextRevisionEpochMs: 1700086400000,
        weakConcepts: ["Manuscripts vs Inscriptions"],
        createdAt: past,
        updatedAt: past,
      })
  );

  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("chapter_progress")
      .doc("ncert_hist_6_ch1")
      .get()
  );
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("chapter_progress")
      .get()
  );
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("chapter_progress")
      .doc("ncert_hist_6_ch1")
      .update({ completed: true, updatedAt: new Date(Date.now() - 500) })
  );
});

test("Subcollections: flashcards, quote_history, study_tasks, test_attempts, english_logs, reminder_configs", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const past = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("study_tasks")
      .doc("task_morning_history")
      .set({
        title: "NCERT History Class 6 Ch 1",
        titleHi: "एनसीईआरटी इतिहास कक्षा 6 अध्याय 1",
        category: "FOUNDATION",
        durationMinutes: 90,
        actualMinutes: 0,
        scheduledTime: "06:00",
        status: "PENDING",
        taskDate: "2026-11-21",
        custom: false,
        orderIndex: 0,
        createdAt: past,
        updatedAt: past,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("study_tasks")
      .doc("task_morning_history")
      .update({
        status: "COMPLETED",
        actualMinutes: 90,
        updatedAt: new Date(Date.now() - 500),
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("flashcards")
      .doc("fc_rm_1")
      .set({
        deckMember: "RM",
        subject: "History",
        chapterId: "ncert_hist_6_ch1",
        front: "Where did people first begin growing wheat and barley around 8000 years ago?",
        back: "Sulaiman and Kirthar hills (northwest).",
        explanation: "NCERT Class 6 History Ch 1: Early agriculture began near Sulaiman & Kirthar.",
        difficulty: "MEDIUM",
        sourceRef: "NCERT Class 6 Our Pasts I, Ch 1, p.2",
        masteryStatus: "NEW",
        favourite: false,
        reviewCount: 0,
        intervalDays: 1,
        nextReviewEpochMs: 1700086400000,
        custom: false,
        createdAt: past,
        updatedAt: past,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("quote_history")
      .doc("qh_1")
      .set({
        quoteId: "rm_01",
        member: "RM",
        text: "Knowledge grows step by step, just like reading one NCERT chapter with full focus.",
        labelType: "INSPIRED_BY_MEMBER",
        sourceNote: "Original study motivation inspired by RM",
        contextTag: "MORNING",
        displayedDate: "2026-11-21",
        slot: "MORNING",
        favourite: false,
        createdAt: past,
        updatedAt: past,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("test_attempts")
      .doc("att_1")
      .set({
        testTitle: "NCERT History Ch 1 Quiz",
        testType: "CHAPTER_TEST",
        chapterId: "ncert_hist_6_ch1",
        totalQuestions: 10,
        correctCount: 8,
        wrongCount: 2,
        skippedCount: 0,
        netScore: 14.66,
        maxScore: 20.0,
        accuracyPercent: 80,
        timeTakenSeconds: 420,
        weakTopics: ["Manuscripts"],
        createdAt: past,
        updatedAt: past,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("english_logs")
      .doc("eng_2026_11_21")
      .set({
        dateStr: "2026-11-21",
        minutesPracticed: 45,
        vocabLearnedCount: 15,
        mistakesReviewed: 3,
        confidenceScore: 7,
        notes: "Practiced introducing my UPSC optional subject in English.",
        createdAt: past,
        updatedAt: past,
      })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("reminder_configs")
      .doc("MORNING_START")
      .set({
        category: "MORNING_START",
        title: "Morning Study Start",
        hour: 6,
        minute: 0,
        enabled: true,
        createdAt: past,
        updatedAt: past,
      })
  );

  // List queries succeed for owner
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("study_tasks").get());
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("flashcards").get());
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("quote_history").get());

  // Reject future timestamp spoofing
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("reminder_configs")
      .doc("BAD_TIME")
      .set({
        category: "BAD_TIME",
        title: "Future spoof",
        hour: 6,
        minute: 0,
        enabled: true,
        createdAt: new Date(Date.now() + 60000),
        updatedAt: new Date(Date.now() + 60000),
      })
  );
});
