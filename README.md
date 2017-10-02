<img src="http://static.nfl.com/static/content/public/static/img/logos/nfl-engineering-light.svg" width="300" />

# graphql-mediator

[ ![Download](https://api.bintray.com/packages/nfl/maven/mediator/images/download.svg) ](https://bintray.com/nfl/maven/mediator/_latestVersion) [![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nfl/graphql-mediator/blob/master/LICENSE)

A library that lets co-opt objects from other GraphQL services.

This library converts the result of a GraphQL introspection query into graph-ql usable objects for inclusion into
other GraphQL exported objects.

## Binaries

Example for Maven:

```xml
<dependency>
    <groupId>com.nfl.graphql</groupId>
    <artifactId>mediator</artifactId>
    <version>1.0.1</version>
</dependency>
```

Example for gradle:

```
gradle compile("com.nfl.graphql:mediator:1.0.1")
```

Change history can be found here: [CHANGELOG.md](https://github.com/nfl/graphql-mediator/blob/master/CHANGELOG.md)

### How to use the latest build with Gradle

Add the repositories:

```groovy
repositories {
    maven { url  "http://dl.bintray.com/nfl/maven" }
}
```

Dependency:

```groovy
dependencies {
  compile 'com.nfl.graphql-mediator:INSERT_LATEST_VERSION_HERE'
}
```

### How to use the latest build with Maven

Add the repository:

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>bintray-nfl-maven</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/nfl/maven</url>
</repository>

```

Dependency:

```xml
<dependency>
    <groupId>com.nfl.graphql-mediator</groupId>
    <artifactId>graphql-mediator</artifactId>
    <version>INSERT_LATEST_VERSION_HERE</version>
</dependency>

```

### How to publish a new release of the library to JCenter
See [deploy.md](deploy.md)

## How to use it

TBD


## Full Documentation

See the [Wiki](https://github.com/NFL/graphql-mediator/wiki/) for full documentation, examples, operational details and other information.

## Build

To build:

```
$ git clone git@github.com:NFL/graphql-mediator.git
$ cd graphql-mediator/
$ ./gradlew build
```

Further details on building can be found on the [Getting Started](https://github.com/NFL/graphql-mediator/wiki/Getting-Started) page of the wiki.

## Requirements

 - >= Java 8

## Examples 

## Contact Info

- Twitter: [@nflengineers](http://twitter.com/nflengineers)
- [GitHub Issues](https://github.com/NFL/graphql-mediator/issues)


## LICENSE

graphql-mediator is licensed under the MIT License. See [LICENSE](LICENSE) for more details.
