package com.example

import com.example.data.ResourcesData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChapterResourcesTest {

    private val chapters = 1..6

    @Test
    fun `every chapter has at least 4 chapter-specific resources`() {
        chapters.forEach { c ->
            val n = ResourcesData.forChapter(c, limit = Int.MAX_VALUE).size
            assertTrue("Chapter $c has only $n specific resources", n >= 4)
        }
    }

    @Test
    fun `general syllabus-wide resources never appear inside a chapter`() {
        val generalIds = ResourcesData.RESOURCES.filter { it.isGeneral }.map { it.id }.toSet()
        assertTrue("expected some general resources", generalIds.isNotEmpty())
        chapters.forEach { c ->
            val shown = ResourcesData.forChapter(c).map { it.id }
            assertTrue("Chapter $c shows general: ${shown.filter { it in generalIds }}", shown.none { it in generalIds })
        }
    }

    @Test
    fun `chapters do not share the same resource list`() {
        val lists = chapters.map { c -> ResourcesData.forChapter(c).map { it.id } }
        assertEquals("two chapters show an identical list", lists.size, lists.toSet().size)
        // المصدر الخاص يظهر في فصلين على الأكثر (وإلا فهو عام)
        val usage = lists.flatten().groupingBy { it }.eachCount()
        usage.forEach { (id, n) -> assertTrue("$id appears in $n chapters", n <= 2) }
    }

    @Test
    fun `resource ids are unique, https, and chapter tags are valid`() {
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
