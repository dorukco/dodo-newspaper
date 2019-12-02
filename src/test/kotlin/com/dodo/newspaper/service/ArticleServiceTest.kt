package com.dodo.newspaper.service

import com.dodo.newspaper.domain.Article
import com.dodo.newspaper.domain.Author
import com.dodo.newspaper.domain.Keyword
import com.dodo.newspaper.exception.ArticleNotFoundException
import com.dodo.newspaper.repository.ArticleRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import java.time.Instant

class ArticleServiceTest {

    private val articleRepository: ArticleRepository = Mockito.mock(ArticleRepository::class.java)

    private val articleService: ArticleService = ArticleService(articleRepository)

    private val article = Article(header = "header", text = "text",
            shortDescription = "short description", publishDate = Instant.MIN).apply {
        authors.add(Author(firstName = "firstname", lastName = "lastname"))
        keywords.add(Keyword(keyword = "keyword"))
    }

    @Test
    fun `create an article`() {
        `when`(articleRepository.save(article)).thenReturn(article)

        articleService.create(article)

        verify(articleRepository).save(article)
    }

    @Test
    fun `get an article`() {
        `when`(articleRepository.findFirstById(1L)).thenReturn(article)

        val returnedArticle = articleService.getArticle(1L)

        assertEquals(returnedArticle, article)
        verify(articleRepository).findFirstById(1L)
    }

    @Test
    fun `get an unknown article`() {
        `when`(articleRepository.findFirstById(1L)).thenReturn(null)

        assertThrows(ArticleNotFoundException::class.java) {
            articleService.getArticle(1L)
        }

        verify(articleRepository).findFirstById(1L)
    }

    @Test
    fun `update an article`() {
        `when`(articleRepository.findFirstById(1L)).thenReturn(article)
        `when`(articleRepository.save(article)).thenReturn(article)

        articleService.update(article, 1L)

        verify(articleRepository).findFirstById(1L)
        verify(articleRepository).save(article)
    }

    @Test
    fun `update an unknown article`() {
        `when`(articleRepository.findFirstById(1L)).thenReturn(null)

        assertThrows(ArticleNotFoundException::class.java) {
            articleService.update(article, 1L)
        }

        verify(articleRepository).findFirstById(1L)
    }

    @Test
    fun `delete an article`() {
        `when`(articleRepository.findFirstById(1L)).thenReturn(article)
        doNothing().`when`(articleRepository).deleteById(1L)

        articleService.deleteArticle(1L)

        verify(articleRepository).findFirstById(1L)
        verify(articleRepository).deleteById(1L)
    }

    @Test
    fun `delete an unknown article`() {
        `when`(articleRepository.findFirstById(1L)).thenReturn(null)

        assertThrows(ArticleNotFoundException::class.java) {
            articleService.deleteArticle(1L)
        }

        verify(articleRepository).findFirstById(1L)
    }

    @Test
    fun `get an article via author`() {
        `when`(articleRepository.getArticles("firstname", "lastname")).thenReturn(listOf(article))

        val returnedArticles = articleService.getArticles("firstname", "lastname")

        assertEquals(returnedArticles?.size, 1)
        verify(articleRepository).getArticles("firstname", "lastname")
    }

    @Test
    fun `get an article via keyword`() {
        `when`(articleRepository.getArticles("keyword")).thenReturn(listOf(article))

        val returnedArticles = articleService.getArticles(keyword =  "keyword")

        assertEquals(returnedArticles?.size, 1)
        verify(articleRepository).getArticles("keyword")
    }

    @Test
    fun `get an article via publication period`() {
        `when`(articleRepository.getArticles(Instant.MIN, Instant.MAX)).thenReturn(listOf(article))

        val returnedArticles = articleService.getArticles(from = Instant.MIN, to = Instant.MAX)

        assertEquals(returnedArticles?.size, 1)
        verify(articleRepository).getArticles(Instant.MIN, Instant.MAX)
    }
}
