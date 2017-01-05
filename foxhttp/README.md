![FoxHttp-Logo](https://github.com/Viascom/groundwork/blob/develop/foxhttp/FoxHttp.png?raw=true)

The FoxHttp provides a fast and easy http client for java and android. It is part of the GroundWork Project by Viascom.

[![release](https://img.shields.io/badge/release-v1.1-brightgreen.svg)](https://github.com/Viascom/groundwork/tree/master)
[![develop](https://img.shields.io/badge/develop-v1.1-brightgreen.svg)](https://github.com/Viascom/groundwork/tree/foxhttp-develop)<br/>
[![coverage](https://img.shields.io/badge/test--coverage-80%25-brightgreen.svg)](https://github.com/Viascom/groundwork/tree/foxhttp-develop)
[![Snap CI branch](https://img.shields.io/snap-ci/Viascom/groundwork/foxhttp-develop.svg)]()
[![Maven Central](https://img.shields.io/maven-central/v/ch.viascom.groundwork/foxhttp.svg)]()
[![Bintray](https://img.shields.io/bintray/v/viascom/GroundWork/ch.viascom.groundwork%3Afoxhttp.svg)]()<br/><br/>
Request against httpbin which was installed on localhost:<br/>
[![get](https://img.shields.io/badge/GET--Request-35.4 ms-brightgreen.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#get-request)
[![post](https://img.shields.io/badge/POST--Request-47.3 ms-brightgreen.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#post-request-with-string-body)
[![Basic-Auth](https://img.shields.io/badge/BasicAuth--Request-43.4 ms-brightgreen.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#get-request-with-basicauth)
[![Post-Parsing](https://img.shields.io/badge/POST--Parsing--Request-53.3 ms-green.svg)](https://github.com/Viascom/groundwork/wiki/GroundWork-FoxHttp-Examples#post-request-with-object-body-and-object-response)


## Functions
* HTTP / HTTPS method support
* GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE request support
* Builders for fast and easy request execution
* Automatic request and response parsing
* Integrated Object, URL-Encoded & Multipart-Body support
* Custom and predefined interceptors
* Powerful authorization strategy
* Fully customizable cookie store
* Host and SSL trust strategy
* Easy proxy strategy
* Android support
* GroundWork ServiceResult support
* Faster than other HttpClient-Frameworks (such as httpComponents,okhttp)
* Gson and XStream support
* _Advanced cache strategy (coming soon)_
* _GroundWork Server-Security support (coming soon)_


## Last Updates
* Wildcards in FoxHttpAuthorizationScope
* Add GZipResponseInterceptor and DeflateResponseInterceptor
* Intercepter weighting for better flow control