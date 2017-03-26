# Grails JDBC Transaction Commit vs. HTTP Response Flush

As I wrote in the grails-community Slack:

> I’ve been chasing a hard-to-reproduce transaction bug in our Grails app where sometimes, when making two
> REST requests in short succession, the changes from the first requests were not “visible” to GORM/Hibernate
> in the 2nd request. E.g. create an object in REST request 1, return the new object ID, then do something
> related to that object ID in REST request 2 -> returns 404. I have suspected that this is either because the
> transaction of request 1 is committed after the response is sent to the client, or the transaction of request 2
> is stale for some reason. I am using RestfulController
> (https://github.com/grails/grails-core/blob/master/grails-plugin-rest/src/main/groovy/grails/rest/RestfulController.groovy)
> the relevant actions are all annotated @Transactional… which does seem to go against the usual
> “Don’t make your controllers transactional, use services” best practice - which by design eliminates my problem,
> because the changes of the transaction would be committed when the service method returns, and before render()
> is called. In RestfulController, render() is called first and the transaction is committed later,
> after the controller action returns (?).

This project is meant to reproduce this issue in an empty Grails app.

The repository contains branches `grails31` with an app based on Grails 3.1.16 and `grails32` with an app
based on Grails 3.2.6.

In both cases, the app can be started with `./gradlew bootRun` and a PUT request that shows the problem (nor not) can be executed like this:

```
curl -v -X "PUT" "http://localhost:8080/books/1" -H "Content-Type: application/json; charset=utf-8" -d "{\"author\": \"Author 1 - Changed `date +%s`\"}"
```

Both branches include an `application.log` snippet of everything that happens when executing the PUT request
and should provide some insight into the order of the JDBC transaction commit vs. the HTTP response flush.
