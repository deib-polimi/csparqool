csparqool
=========

#Javadoc

Click [here](http://deib-polimi.github.io/csparqool/) to see the Javadoc.

#Installation

The library is released on deib-polimi maven repository on GitHub.

Releases repository:
```xml
<repositories>
	...
	<repository>
        <id>deib-polimi-releases</id>
        <url>https://github.com/deib-polimi/deib-polimi-mvn-repo/raw/master/releases</url>
	</repository>
	...
</repositories>
```

Dependency:
```xml
<dependencies>
	<dependency>
		<groupId>it.polimi.csparqool</groupId>
		<artifactId>csparqool</artifactId>
		<version>VERSION</version>
	</dependency>
</dependencies>
```

#Change List

v1.3:
* bug fixes and refactoring

v1.2.2:
* selectFunctions can now be used for reassigning variables (e.g.: ?var1 AS ?var2) using `null` as aggregateFunction 
* minor bug fix

v1.2.1:
* COUNT function added

v1.2:
* SUM function implemented

v1.1:
* FILTER implemented