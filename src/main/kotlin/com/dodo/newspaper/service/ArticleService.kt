package com.dodo.newspaper.service

import com.dodo.newspaper.domain.Article
import com.dodo.newspaper.exception.ArticleNotFoundException
import com.dodo.newspaper.repository.ArticleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

    @Transactional
    fun create(article: Article) {
        articleRepository.save(article)
    }

    @Transactional
    fun update(article: Article, articleId: Long) {
        findArticle(articleId).also {
            articleRepository.save(article.apply {
                id = articleId
            })
        }
    }

    @Transactional
    fun deleteArticle(articleId: Long) {
        findArticle(articleId).also {
            articleRepository.deleteById(articleId)
        }
    }

    fun getArticle(articleId: Long): Article? {
        return findArticle(articleId)
    }

    fun getArticles(firstName: String, lastName: String): List<Article>? {
        return articleRepository.getArticles(firstName, lastName)
    }

    fun getArticles(keyword: String): List<Article>? {
        return articleRepository.getArticles(keyword)
    }

    fun getArticles(from: Instant, to: Instant): List<Article>? {
        return articleRepository.getArticles(from, to)
    }

    private fun findArticle(articleId: Long): Article? {
        return articleRepository.findByIdOrNull(articleId)
                ?: throw ArticleNotFoundException("Article with id $articleId cannot be found.")
    }

}
