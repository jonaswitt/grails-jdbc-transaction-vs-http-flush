package my.api

import org.bookstore.Book

class BootStrap {

    def init = { servletContext ->
        new Book(title: 'Book 1', author: 'Book 1').save(failOnError: true)
    }
    def destroy = {
    }
}
