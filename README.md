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

The general form of a predicate is:
```json
{ 
	"<path1>" : { "$<operator>": "<value> | @<path2>" } 
}
```
where:
- ```path1``` and ```path2```: stands for references like that the ones provided by Jakarta ```BeanUtils|PropertyUtils``` expressions (you can change it by implementing the interface ``IAccess``);
- ```operator``` : is one of the possible operators predefined, or loaded by your code;
- ```value```: is a string to be converted for comparison;

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
	IPredicateFactory factory = new PredicateFactoryJson();

	String filter = "{\"name\":{\"$contains\": \"project\"}}";
	Predicate<Object> p = factory.read(filter.getBytes());

	List<Project> projects = ...// loaded list from somewhere
	return projects.stream().filter(p).collect(Collectors.toList());

```
In this example, if we provide the filter value using a GUI the underlying Java code remains unchanged.

## Syntatic suggars (rewrite engine)
There is an interface named ``IRewriter`` to 
rewrite a Json structure before processing.
There is a default implementation,
 by suggestion of [@vampireslove](https://github.com/vampireslove) a predicate like:``{"name":"json-predicate"}`` is automatically rewritten to the normal form as ``{"name": {"$eq":"json-predicate"} }``.

To replace this rewriter set your instante to the ``PredicateJsonFactory`` instante.

## Converters
Based on ```path1``` type the ```value``` is converted according to it. i.e for comparison with date the [`IConverter`](https://github.com/thiagolvlsantos/json-predicate/blob/master/src/main/java/io/github/thiagolvlsantos/json/predicate/value/impl/ConverterDefault.java) takes place and convert for types:
| Attribute type | Value converted to compariosn using |
| -- | -- |
| ```java.util.Date``` | ```yyyy-MM-dd HH:mm:ss.SSS``` |
| ```java.time.LocalDate``` | ```yyyy-MM-dd``` |
| ```java.time.LocalDateTime``` | ```yyyy-MM-dd HH:mm:ss.SSS``` |

These converters can be replaced using respective set method in the predicate manager describe bellow.

## Predefined constructors

There is a list of the built-in provided predicates, 
you can register you own predicate. Checkout the interface 
[`IPredicateManager`](https://github.com/thiagolvlsantos/json-predicate/blob/master/src/main/java/io/github/thiagolvlsantos/json/predicate/impl/PredicateManagerDefault.java) implementation which load operators from properties. 

Operators names are case-insensitive, table camel-case names are only to help on reading.

```properties
$and,$&=io.github.thiagolvlsantos.json.predicate.array.impl.PredicateAnd
```

This class loads files in classpath (```json-predicate.properties```) with operators mappings, such as the example bellow. A default mapping ([json-predicate_default.properties]((https://github.com/thiagolvlsantos/json-predicate/blob/master/src/main/resources/json-predicate_default.properties))) is provided with the following built-in operators.

### Logical operators

| Type | Example |
| -- | -- |
|and, & | ``` { "$and": [ {"name": {"$contains": "project"} }, { "created": {"$gt": "2021-06-29 00:31:45.000"} ] }``` |
|or, \\| | ``` { "$or": [ {"name": {"$contains": "project"} }, { "id": {"$gt": "10"} ] }``` |
|not, ! | ``` { "$not": {"name": {"$eq": "null"} } }``` |

### Relational operators
| Type | Example |
| -- | -- |
|eq, ==, equals | ``` {"name": {"$eq": "projectA"} } ```|
|ne, !=, notEquals | ``` {"name": {"$ne": "projectB"} }```|
|lt, \<, lowerThan | ``` {"revision": {"$lt": 10} }```|
|le, lte, \<=, lowerThanEquals, lowerEqualsThan | ``` {"revision": {"$le": 1} }```|
|gt, \>, greaterThan | ``` {"revision": {"$gt": 1} }```|
|ge, gte, \>=, greaterThanEquals, greaterEqualsThan | ``` {"revision": {"$ge": 2} }```|

### String operators
| Type | Example |
| -- | -- |
|contains, c, regex | ``` {"name": {"$contains": "proj"} }```|
|nContains, nc, notContains, nRegex, !contains, !c, !regex  | ``` {"name": {"$ncontains": "A"} }```|
|match, m | ``` {"name": {"$match": "\d{8}"} }```|
|nMatch, nm, notMatch, !match, !m | ``` {"name": {"$nmatch": "\d{8}"} }```|

### Set operators 
| | |
| -- | -- |
|contains, c | ``` {"tags": {"$contains": "debug"} }``` |
|ncontains, nc, notContains, !contains, !c  | ``` {"tags": {"$ncontains": "git"} }``` |
|memberOf, mo, in | ``` {"role": {"$memberOf": ["admin","user"]} }``` |
|nMemberOf, nmo, notMemberOf, !memberOf, !mo, !in | ``` {"role": {"$notMemberOf": ["po"]} }``` |

### Variable operators 
You can use values referring to another variables. i.e. if project changed date is greater than project creation date.

|  |  |
| -- | -- |
| "path1" { "operator":"@path2" }  | ``` {"changed": {"$>": "@created"} }```|

## Overriding operators
If you want to override an operator add its mapping to the file ```json-predicate.properties``` together with an ```order``` key which is used to define precedence. The default file has ```order=0```.

For example, if you want to override ```$and``` in your file ```json-predicate.properties``` do:

```properties
order=1
$and,$&=mypackage.MyPredicate
```

## Deserialization

A class can have an attribute annotated with `@JsonDeserializer` to read a `Predicate<Object>` straightforward.

```java
@Data
public class Rule {

	private String name;

	@JsonDeserialize(using = PredicateDeserializer.class)
	private Predicate<Object> condition;
}
```

A file `example_rule.json`:

```json
{
	"name": "Filter projects with A",
	"condition": {
		"name": {
			"$contains": "A"
		}
	}
}
```
can be read by a Jackson `ObjectMapper` just like this:
```java
	ObjectMapper mapper = new ObjectMapper();
	Rule rule = mapper.readValue(Files.readAllBytes(Paths.get("example_rule.json")), Rule.class);
	Predicate<Object> condition = rule.getCondition(); // condition ready to apply
```
 The resulting instance of `Rule` has the `condition` attribute already set to a `Predicate<Object>`.
 
 Notice that this approach can be used for deserializing REST calls where `@Payload` is an object of type `Rule`. On the other hand the serialization process is not defined, unless you write a serializer for a generic predicate (next steps?). 
 
 As a generic solution there could be a `Rule` class with `condition` as `String` for storage/serialization/deserialization in CRUD features, and a `RuleExec` with `condition` as `Predicate<Object>` to the moments where the rule is expected to be processed. It`s up to you use what fit your needs.

## Building

Localy, from this root directory call Maven commands or `bin/<script name>` at your will.
