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
@Table(name = "author")
data class Author(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonIgnore
        val id: Long? = null,

        @Column(name = "first_name", nullable = false)
        val firstName: String,

        @Column(name = "last_name", nullable = false)
        val lastName: String
) {
        @JsonIgnore
        @ManyToMany(mappedBy = "authors")
        val articles: MutableSet<Article> = mutableSetOf()
}
