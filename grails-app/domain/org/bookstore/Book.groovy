package org.bookstore

import grails.rest.Resource
import grails.rest.RestfulController

@Resource(formats = ['json'], superClass = RestfulController)
class Book {

    String title
    String author

    static constraints = {
    }

}
