package com.dodo.newspaper.repository

import com.dodo.newspaper.domain.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {

    @Query("SELECT ar FROM Author au JOIN au.articles ar WHERE au.firstName = :firstName and au.lastName = :lastName")
    fun getArticles(@Param("firstName") firstName: String?,
                     @Param("lastName") lastName: String?): List<Article>?

    @Query("SELECT ar FROM Keyword ke JOIN ke.articles ar WHERE ke.keyword = :keyword")
    fun getArticles(@Param("keyword") keyword: String?): List<Article>?

    @Query("SELECT ar FROM Article ar WHERE (ar.publishDate >= :from) AND (ar.publishDate <= :to)")
    fun getArticles(@Param("from") from: Instant?,
                    @Param("to") to: Instant?): List<Article>?

    fun findFirstById(id: Long): Article?
}
