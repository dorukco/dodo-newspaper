package com.dodo.newspaper.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.Lob
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "article")
data class Article(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        var id: Long? = null,

        @Column(name = "header", nullable = false)
        @field:NotNull
        val header: String,

        @Column(name = "short_description", nullable = false)
        @field:NotNull
        val shortDescription: String,

        @Lob
        @Column(name = "text", nullable = false)
        @field:NotNull
        val text: String,

        @Column(name = "publish_date", nullable = false)
        val publishDate: Instant = Instant.now()
) {
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
            name = "article_authors",
            joinColumns = [JoinColumn(name = "article_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "author_id", referencedColumnName = "id")]
    )
    val authors: MutableSet<Author> = mutableSetOf()

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
            name = "article_keywords",
            joinColumns = [JoinColumn(name = "article_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "keyword_id", referencedColumnName = "id")]
    )
    val keywords: MutableSet<Keyword> = mutableSetOf()
}
