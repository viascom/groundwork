GroundWork - RESTClient - HTTP-Component
========================================

The RESTClient http-component implementation module provides an easy way to call REST endpoints. It is part of the GroundWork Project by Viascom.

### Version:
[![release](https://img.shields.io/badge/release-v1.1--SNAPSHOT-red.svg)](https://github.com/Viascom/groundwork/tree/master/restclient-http)<br/>
[![develop](https://img.shields.io/badge/develop-v1.1--SNAPSHOT-red.svg)](https://github.com/Viascom/groundwork/tree/develop/restclient-http)

### Quick Start:
To add this implementation into your project:

#### Dependency

##### maven
```xml
<dependency>
    <groupId>ch.viascom.groundwork</groupId>
    <artifactId>restclient-http</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

##### gradle
```
compile 'ch.viascom.groundwork:restclient-http:1.0-SNAPSHOT'
```

#### How to use

##### Get-Request with SimpleGetRequest
```java
JSONResponse response = new SimpleGetRequest<>("https://jsonplaceholder.typicode.com/posts/1", JSONResponse.class).execute();
System.out.println(response.getResponseHeader());
System.out.println(response.toJson());
```

##### Get-Request with SimpleGetRequest (the long way)
```java
String url = "https://jsonplaceholder.typicode.com";
String path = "/posts/1";

SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>(url, JSONResponse.class);
request.setPath(path);
JSONResponse response = request.execute();
System.out.println(response.getResponseHeader());
System.out.println(response.Json());
```

##### How to use filters
With filters you can inject your own override code at specific points during the request.
Possible injection points are:
- Before the request definition (RequestFilter)
- Before the request execution (RequestWriteFilter)
- On an exception during the request (RequestExceptionFilter)
- After the request (ResponseReadFilter)
- After the deserialization of the response (ResponseFilter)
- On an exception during the response processing (ResponseExceptionFilter)
- On a successful response code (SuccessResponseCodeFilter)
- On a error response code (ErrorResponseCodeFilter)

Preinstalled filter: 
- BasicAuthFilter
- BearerTokenFilter
- DefaultSuccessResponseCodeFilter
- DefaultErrorResponseCodeFilter

To register/unregister a filter you can user `request.register(<Your Filter>)` and `request.unregister(<Your Filter-Class>)`

Example:
```java
String url = "http://httpbin.org";
String path = "/basic-auth/user/passwd";

SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>(url, JSONResponse.class);
request.setPath(path);

request.register(new BasicAuthFilter("user","passwd"));

JSONResponse response = request.execute();
System.out.println(response.getResponseHeader());
System.out.println(response.Json());
```

##### Response code handling
By default every status code that doesn't match >= 200 and < 300 is handled as a RESTClientException (based on DefaultErrorResponseCodeFilter) and the body will not be parsed.
You can override/extend this definition by using this two methods:
```java
//Handle a port as successful port
request.addAdditionalAllowedStatusCode(404);

//Handle a port as error port
request.addAdditionalDeniedStatusCode(200);
```