# json-predicate

[![CI with Maven](https://github.com/thiagolvlsantos/json-predicate/actions/workflows/maven.yml/badge.svg)](https://github.com/thiagolvlsantos/json-predicate/actions/workflows/maven.yml)
[![CI with CodeQL](https://github.com/thiagolvlsantos/json-predicate/actions/workflows/codeql.yml/badge.svg)](https://github.com/thiagolvlsantos/json-predicate/actions/workflows/codeql.yml)
[![CI with Sonar](https://github.com/thiagolvlsantos/json-predicate/actions/workflows/sonar.yml/badge.svg)](https://github.com/thiagolvlsantos/json-predicate/actions/workflows/sonar.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=thiagolvlsantos_json-predicate&metric=alert_status)](https://sonarcloud.io/dashboard?id=thiagolvlsantos_json-predicate)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=thiagolvlsantos_json-predicate&metric=coverage)](https://sonarcloud.io/dashboard?id=thiagolvlsantos_json-predicate)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.thiagolvlsantos/json-predicate/badge.svg)](https://repo1.maven.org/maven2/io/github/thiagolvlsantos/json-predicate/)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Specify a predicate in a JSON format and check it against a given object.

## Usage

Include latest version [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.thiagolvlsantos/json-predicate/badge.svg)](https://repo1.maven.org/maven2/io/github/thiagolvlsantos/json-predicate/) to your project.

```xml
		<dependency>
			<groupId>io.github.thiagolvlsantos</groupId>
			<artifactId>json-predicate</artifactId>
			<version>${latestVersion}</version>
		</dependency>
```

## Predicates

The objective of this library is to create a `Predicate<Object>` based on a JSON specification which includes different types of predicates. The general idea is that these JSON predicates can be build using a GUI and be used in `filter` operations. 

An example of a JSON predicate filtering objects (or maps) whose field/key called 'name' contains the String 'project':
```json
{
   "name": {
     "$contains": "project"
   }
}
```

Suppose there is a `List<Project>` where each project has a `String:name` attribute, the following code will filter only those with `project` in its attribute 'name'.

```java
	String filter = "{\"name\":{\"$contains\": \"project\"}}";
	IPredicateFactory factory = new PredicateFactoryJson();
	Predicate<Object> p = factory.read(filter);
	List<Project> projects = ...// loaded list from somewhere
	return projects.stream().filter(p).collect(Collectors.toList());

```
In this example, if we provide the filter value using a GUI the underlying Java code remains unchanged.

## Predefined constructors

Bellow a list of the built-in provided predicates, you can register you own predicate. Checkout the interface [`IPredicateManager`](https://github.com/thiagolvlsantos/json-predicate/blob/master/src/main/java/io/github/thiagolvlsantos/json/predicate/impl/PredicateManagerDefault.java) implementation.

### Logical operators

| Type | Example |
| -- | -- |
|$and | ``` { "$and": [ {"name": {"$contains": "project"} }, { "created": {"$gt": "2021-06-29 00:31:45.0000"} ] }``` |
|$or | ``` { "$or": [ {"name": {"$contains": "project"} }, { "id": {"$gt": "10"} ] }``` |
|$not| ``` { "$not": {"name": {"$eq": "null"} } }``` |

### Relational operators
| Type | Example |
| -- | -- |
|$eq | ``` {"name": {"$eq": "projectA"} } ```|
|$ne | ``` {"name": {"$ne": "projectB"} }```|
|$lt | ``` {"revision": {"$lt": 10} }```|
|$le | ``` {"revision": {"$le": 1} }```|
|$gt | ``` {"revision": {"$gt": 1} }```|
|$ge | ``` {"revision": {"$ge": 2} }```|

### String operators
| Type | Example |
| -- | -- |
|$contains or $c | ``` {"name": {"$contains": "proj"} }```|
|$ncontains or $nc | ``` {"name": {"$ncontains": "A"} }```|
|$match or $m | ``` {"name": {"$match": "\d{8}"} }```|
|$nmatch or $nm | ``` {"name": {"$nmatch": "\d{8}"} }```|

## Build

Localy, from this root directory call Maven commands or `bin/<script name>` at our will.
