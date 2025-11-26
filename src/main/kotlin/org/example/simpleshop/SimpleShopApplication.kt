package org.example.simpleshop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class SimpleShopApplication

fun main(args: Array<String>) {
    runApplication<SimpleShopApplication>(*args)
}
