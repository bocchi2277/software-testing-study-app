package com.example.data

import java.util.Locale

data class Question(
    val id: String,
    val ch: Int,
    val q: String,
    val options: List<String>, // index 0 is always correct before shuffling
    val exp: String,
    val qEn: String = q,
    val optionsEn: List<String> = options,
    val expEn: String = exp
)

data class PreparedQuestion(
    val question: Question,
    val order: List<Int>,
    val correctIdx: Int
)

internal data class RawQ(
    val id: String,
    val ch: Int,
    val q: String,
    val correct: String,
    val wrong: List<String>,
    val exp: String,
    val qEn: String,
    val correctEn: String,
    val wrongEn: List<String>,
    val expEn: String
)

object QuestionsData {
    private val raw: List<RawQ> =
        (QuestionsCh1To3.items + QuestionsCh4.items + QuestionsCh5To6.items)
            .sortedBy { it.id }

    val QUESTIONS: List<Question> = raw.map { r ->
        Question(
            id = r.id,
            ch = r.ch,
            q = r.q,
            options = listOf(r.correct) + r.wrong,
            exp = r.exp,
            qEn = r.qEn,
            optionsEn = listOf(r.correctEn) + r.wrongEn,
            expEn = r.expEn
        )
    }

    val QMAP: Map<String, Question> = QUESTIONS.associateBy { it.id }

    fun prepare(q: Question): PreparedQuestion {
        val order = q.options.indices.shuffled()
        return PreparedQuestion(
            question = q,
            order = order,
            correctIdx = order.indexOf(0)
        )
    }

    fun <T> shuffle(items: List<T>): List<T> = items.shuffled()

    fun idForIndex(index: Int): String = "q" + "%03d".format(Locale.US, index + 1)
}
