package com.dodo.newspaper.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "keyword")
data class Keyword(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonIgnore
        val id: Long? = null,

        @Column(name = "keyword", nullable = false)
        val keyword: String
) {
    @JsonIgnore
    @ManyToMany(mappedBy = "keywords")
    val articles: MutableSet<Article> = mutableSetOf()
}
