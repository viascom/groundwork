GroundWork - RESTClient - HTTP-Component
========================================

The RESTClient http-component implementation module provides an easy way to call REST endpoints. It is part of the GroundWork Project by Viascom.

### Version:
[![release](https://img.shields.io/badge/release-v1.0--SNAPSHOT-red.svg)](https://github.com/Viascom/groundwork/tree/master/restclient-http)<br/>
[![develop](https://img.shields.io/badge/develop-v1.0--SNAPSHOT-red.svg)](https://github.com/Viascom/groundwork/tree/develop/restclient-http)

### Quick Start:
To add this implementation into your project:

#### Dependency

##### maven
```xml
<dependency>
    <groupId>ch.viascom.groundwork</groupId>
    <artifactId>restclient-http</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

##### gradle
```
compile 'ch.viascom.groundwork:restclient-http:1.0-SNAPSHOT'
```

#### Get-Request with SimpleGetRequest
```java
GenericResponse response = new SimpleGetRequest("https://jsonplaceholder.typicode.com/posts/1","application/json").execute();
System.out.println(response.getResponseHeader());
System.out.println(EntityUtils.toString((HttpEntity) response.getEntity()));
```

#### Get-Request with SimpleGetRequest (the long way)
```java
String url = "https://jsonplaceholder.typicode.com";
String path = "/posts/1";

SimpleGetRequest request = new SimpleGetRequest();
request.setMediaType("application/json");
request.setUrl(url);
request.setPath(path);
GenericResponse response = request.execute();
System.out.println(response.getResponseHeader());
System.out.println(EntityUtils.toString((HttpEntity) response.getEntity()));
```



---