package com.example

import com.example.data.ResourcesData
import com.example.data.SyllabusData
import com.example.data.ZatonaData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ChapterResourcesTest {

    private val chapters = 1..6

    @Test
    fun `resource catalog count matches verified inventory and categories include repos`() {
        assertEquals(131, ResourcesData.RESOURCES.size)
        assertTrue("ملخصات ومستودعات" in ResourcesData.RES_CATS)
        assertTrue(ResourcesData.RESOURCES.count { it.cat == "ملخصات ومستودعات" } >= 10)
    }

    @Test
    fun `every chapter has at least 4 chapter-specific resources including Arabic`() {
        chapters.forEach { c ->
            val list = ResourcesData.forChapter(c, limit = Int.MAX_VALUE)
            assertTrue("Chapter $c has only ${list.size} specific resources", list.size >= 4)
            assertTrue("Chapter $c must include at least 1 Arabic resource", list.any { it.lang == "ar" })
        }
    }

    @Test
    fun `general syllabus-wide resources never appear inside a chapter or topic`() {
        val generalIds = ResourcesData.RESOURCES.filter { it.isGeneral }.map { it.id }.toSet()
        assertTrue("expected some general resources", generalIds.isNotEmpty())
        chapters.forEach { c ->
            val shown = ResourcesData.forChapter(c).map { it.id }
            assertTrue("Chapter $c shows general: ${shown.filter { it in generalIds }}", shown.none { it in generalIds })
        }
        SyllabusData.ALL_TOPICS.forEach { t ->
            val shown = ResourcesData.forTopic(t.chapter, t.id).map { it.id }
            assertTrue("Topic ${t.id} shows general: ${shown.filter { it in generalIds }}", shown.none { it in generalIds })
            assertTrue("Topic ${t.id} has empty resources", shown.isNotEmpty())
        }
    }

    @Test
    fun `chapters do not share the same resource list`() {
        val lists = chapters.map { c -> ResourcesData.forChapter(c).map { it.id } }
        assertEquals("two chapters show an identical list", lists.size, lists.toSet().size)
        val usage = lists.flatten().groupingBy { it }.eachCount()
        usage.forEach { (id, n) -> assertTrue("$id appears in $n chapters", n <= 2) }
    }

    @Test
    fun `forTopic prioritizes topic-tagged resources first`() {
        val keyTopics = listOf(
            "1.1", "1.3", "1.4",
            "2.1", "2.2",
            "3.1", "3.2",
            "4.2.1", "4.2.2", "4.2.3", "4.3", "4.4", "4.5",
            "5.1", "5.2", "5.5",
            "6.1"
        )
        keyTopics.forEach { topicId ->
            val topic = SyllabusData.findTopic(topicId)!!
            val directMatches = ResourcesData.RESOURCES.filter {
                topic.chapter in it.ch && !it.isGeneral && topicId in it.topics
            }
            assertTrue("Expected direct resources for topic $topicId", directMatches.isNotEmpty())

            val result = ResourcesData.forTopic(topic.chapter, topicId)
            assertTrue("forTopic($topicId) must not exceed 8", result.size <= 8)
            assertTrue("All items must belong to chapter ${topic.chapter}", result.all { topic.chapter in it.ch && !it.isGeneral })
            assertTrue("First resource for $topicId should be tagged for $topicId", topicId in result.first().topics)
        }
    }

    @Test
    fun `topic tags reference valid syllabus topics in matching chapters`() {
        val topicMap = SyllabusData.ALL_TOPICS.associateBy { it.id }
        ResourcesData.RESOURCES.forEach { r ->
            r.topics.forEach { tid ->
                val topic = topicMap[tid]
                assertTrue("${r.id} references unknown topic '$tid'", topic != null)
                assertTrue(
                    "${r.id} tags topic $tid (chapter ${topic!!.chapter}) without matching chapter tag ${r.ch}",
                    topic.chapter in r.ch
                )
            }
        }
    }

    @Test
    fun `every chapter has a non-empty Arabic Zatona cheat sheet`() {
        chapters.forEach { c ->
            val z = ZatonaData.forChapter(c)
            assertNotNull("Missing Zatona cheat sheet for chapter $c", z)
            assertTrue(z!!.coreSummary.isNotEmpty())
            assertTrue(z.keyFormulasAndDiffs.isNotEmpty())
            assertTrue(z.examTraps.isNotEmpty())
        }
    }

    @Test
    fun `resource ids are unique, https, and metadata is valid`() {
        val all = ResourcesData.RESOURCES
        assertEquals(all.size, all.map { it.id }.toSet().size)
        all.forEach { r ->
            assertTrue("${r.id} url must be https", r.url.startsWith("https://"))
            assertTrue("${r.id} has an invalid chapter tag", r.ch.all { it in chapters })
            assertFalse("${r.id} blank title/desc", r.title.isBlank() || r.desc.isBlank())
            assertTrue("${r.id} unknown category ${r.cat}", r.cat in ResourcesData.RES_CATS)
        }
    }
}
