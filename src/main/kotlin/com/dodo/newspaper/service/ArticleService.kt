package com.dodo.newspaper.service

import com.dodo.newspaper.domain.Article
import com.dodo.newspaper.exception.ArticleNotFoundException
import com.dodo.newspaper.exception.ArticleSearchException
import com.dodo.newspaper.repository.ArticleRepository
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
        getArticle(articleId).also {
            articleRepository.save(article.apply {
                id = articleId
            })
        }
    }

    @Transactional
    fun deleteArticle(articleId: Long) {
        getArticle(articleId).also {
            articleRepository.deleteById(articleId)
        }
    }

    fun getArticle(articleId: Long): Article? {
        return articleRepository.findFirstById(articleId)
                ?: throw ArticleNotFoundException("Article with id $articleId cannot be found.")
    }

    fun getArticles(firstName: String? = null, lastName: String? = null, keyword: String? = null,
                    from: Instant? = null, to: Instant? = null): List<Article>? {
        return if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
            articleRepository.getArticles(firstName, lastName)
        } else if (!keyword.isNullOrEmpty()) {
            articleRepository.getArticles(keyword)
        } else if (from != null && to != null) {
            articleRepository.getArticles(from, to)
        } else {
            throw ArticleSearchException("Parameter conditions 'keyword' OR 'firstName', 'lastName' OR 'from', 'to' do not meet for actual request parameters.")
        }
    }
}
