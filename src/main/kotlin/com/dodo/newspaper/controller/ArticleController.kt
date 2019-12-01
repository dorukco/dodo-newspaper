package com.dodo.newspaper.controller

import com.dodo.newspaper.controller.ArticleController.Companion.BASE_URL
import com.dodo.newspaper.domain.Article
import com.dodo.newspaper.service.ArticleService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import javax.validation.Valid

@RestController
@RequestMapping(BASE_URL)
@Validated
class ArticleController(private val articleService: ArticleService) {

    @ApiOperation(value = "Create a newspaper article")
    @RequestMapping(method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createArticle(
            @Valid @RequestBody article: Article) = articleService.create(article)

    @ApiOperation(value = "Update a newspaper article for given article id")
    @RequestMapping(ARTICLE_ID_URL, method = [RequestMethod.PUT],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateArticle(
            @Valid @RequestBody article: Article,
            @PathVariable("Article-Id") articleId: Long) = articleService.update(article, articleId)

    @ApiOperation(value = "Delete a newspaper article for given article id")
    @RequestMapping(ARTICLE_ID_URL, method = [RequestMethod.DELETE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(
            @PathVariable("Article-Id") articleId: Long) = articleService.deleteArticle(articleId)

    @ApiOperation(value = "Get a newspaper article for given article id")
    @RequestMapping(ARTICLE_ID_URL, method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getArticle(
            @PathVariable("Article-Id") articleId: Long) = articleService.getArticle(articleId)

    @ApiOperation(value = "Get newspaper articles for given author name")
    @RequestMapping(method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE], params = ["firstName", "lastName"])
    @ResponseStatus(HttpStatus.OK)
    fun getArticles(
            @RequestParam("firstName") firstName: String,
            @RequestParam("lastName") lastName: String) = articleService.getArticles(firstName, lastName)

    @ApiOperation(value = "Get newspaper articles for given keyword")
    @RequestMapping(method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE], params = ["keyword"])
    @ResponseStatus(HttpStatus.OK)
    fun getArticles(
            @RequestParam("keyword") keyword: String) = articleService.getArticles(keyword)

    @ApiOperation(value = "Get newspaper articles for given period")
    @RequestMapping(method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE], params = ["from", "to"])
    @ResponseStatus(HttpStatus.OK)
    fun getArticles(
            @RequestParam("from") from: Instant,
            @RequestParam("to") to: Instant) = articleService.getArticles(from, to)

    companion object {
        const val BASE_URL = "article"
        const val ARTICLE_ID_URL = "/{Article-Id}"
    }
}
