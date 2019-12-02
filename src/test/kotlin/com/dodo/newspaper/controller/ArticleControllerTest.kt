package com.dodo.newspaper.controller

import com.dodo.newspaper.domain.Article
import com.dodo.newspaper.domain.Author
import com.dodo.newspaper.domain.Keyword
import com.dodo.newspaper.exception.ArticleNotFoundException
import com.dodo.newspaper.exception.ArticleSearchException
import com.dodo.newspaper.service.ArticleService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@ExtendWith(MockitoExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val article = Article(header = "header", text = "text",
            shortDescription = "short description", publishDate = Instant.MIN).apply {
        authors.add(Author(firstName = "firstname", lastName = "lastname"))
        keywords.add(Keyword(keyword = "keyword"))
    }

    @Test
    fun `upon a create article request, http status 201 is returned`() {
        doNothing().`when`(articleService).create(article)

        mockMvc.perform(post("/article").content(objectMapper.writeValueAsString(article))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated)
                .andReturn()
    }

    @Test
    fun `upon a get article request, the article is returned`() {
        `when`(articleService.getArticle(1L)).thenReturn(article)

        mockMvc.perform(get("/article/1").accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(content().string(objectMapper.writeValueAsString(article)))
    }

    @Test
    fun `upon an update article request, http status 204 is returned`() {
        val updatedArticle = article.apply { keywords.add(Keyword(keyword = "keyword1")) }
        doNothing().`when`(articleService).update(updatedArticle, 1L)

        mockMvc.perform(put("/article/1").content(objectMapper.writeValueAsString(updatedArticle))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent)
                .andReturn()
    }

    @Test
    fun `upon an unknown update article request, http status 400 is returned`() {
        val updatedArticle = article.apply { keywords.add(Keyword(keyword = "keyword1")) }
        doThrow(ArticleNotFoundException("error")).`when`(articleService).update(updatedArticle, 1L)

        mockMvc.perform(put("/article/1").content(objectMapper.writeValueAsString(updatedArticle))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
                .andReturn()
    }

    @Test
    fun `upon a delete article request, http status 204 is returned`() {
        doNothing().`when`(articleService).deleteArticle(1L)

        mockMvc.perform(delete("/article/1"))
                .andExpect(status().isNoContent)
                .andReturn()
    }

    @Test
    fun `upon an unknown delete article request, http status 400 is returned`() {
        doThrow(ArticleNotFoundException("error")).`when`(articleService).deleteArticle(1L)

        mockMvc.perform(delete("/article/1"))
                .andExpect(status().isBadRequest)
                .andReturn()
    }

    @Test
    fun `upon a get article request via author, articles are returned`() {
        `when`(articleService.getArticles(firstName = "firstname", lastName = "lastname")).thenReturn(listOf(article))

        mockMvc.perform(get("/article?firstName=firstname&lastName=lastname").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(content().string(objectMapper.writeValueAsString(listOf(article))))
    }

    @Test
    fun `upon a get article request via keyword, articles are returned`() {
        `when`(articleService.getArticles(keyword = "keyword")).thenReturn(listOf(article))

        mockMvc.perform(get("/article?keyword=keyword").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(content().string(objectMapper.writeValueAsString(listOf(article))))
    }

    @Test
    fun `upon a get article request via publication period, articles are returned`() {
        val from = Instant.ofEpochMilli(1575327273L)
        val to = Instant.ofEpochMilli(1575327473L)
        `when`(articleService.getArticles(from = from, to = to)).thenReturn(listOf(article))

        mockMvc.perform(get("/article?from=$from&to=$to").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andExpect(content().string(objectMapper.writeValueAsString(listOf(article))))
    }

    @Test
    fun `upon a get article request via missing parameters, http status 400 is returned`() {
        `when`(articleService.getArticles(firstName = "firstname", keyword = "keyword")).thenThrow(ArticleSearchException("error"))

        mockMvc.perform(get("/article?firstName=firstname&keyword=keyword").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest)
    }
}
