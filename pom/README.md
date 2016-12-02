GroundWork - POM
================

The POM module provides a parent pom which is also use in the GroundWork Project. It is part of the GroundWork Project by Viascom.

### Version:
[![release](https://img.shields.io/badge/release-v1.0--SNAPSHOT-red.svg)](https://github.com/Viascom/groundwork/tree/master/pom)<br/>
[![develop](https://img.shields.io/badge/develop-v1.0--SNAPSHOT-red.svg)](https://github.com/Viascom/groundwork/tree/develop/pom)

### Profiles:

#### JavaDoc (javadoc)
This profile generates in addition to the build also a file with the extension *-javadoc* which contains all javadoc's from the project.

#### Source (source)
This profile generates in addition to the build also a file with the extension *-source* which contains all source code from the project.

#### Coverage (coverage)
This profile generates a code coverage file based on your unit tests.

#### Executable-Jar
This profile creates a executable jar with the name of the build + -jar-with-dependencies
You have to define the following properties.
- `<executable.jar.main.class>Your Main-Class</executable.jar.main.class>`
- `<executable.jar.finalName>executable-output</executable.jar.finalName>`
- `<executable.jar.appendAssemblyId>true</executable.jar.appendAssemblyId>`

#### Wildfly (wildfly)
This profile automaticaly (if *install* goal is used) deploys your build to a wildfly applicationserver.
If you have a multi module project you have to define `<applicationserver.wildfly.deploy.skip>true</applicationserver.wildfly.deploy.skip>`
in every module you won't deploy.

If you activate this profile make sure your define the following properties according to your setup.
- `<applicationserver.wildfly.hostname>localhost</applicationserver.wildfly.hostname>`
- `<applicationserver.wildfly.username>admin</applicationserver.wildfly.username>`
- `<applicationserver.wildfly.password>password</applicationserver.wildfly.password>`

#### JBoss (jboss)
This profile automaticaly (if *install* goal is used) deploys your build to a jboss applicationserver.
If you activate this profile make sure your define the following properties according to your setup.
- `<applicationserver.jboss.hostname>localhost</applicationserver.jboss.hostname>`
- `<applicationserver.jboss.port>9990</applicationserver.jboss.port>`
- `<applicationserver.jboss.username>admin</applicationserver.jboss.username>`
- `<applicationserver.jboss.password>password</applicationserver.jboss.password>`