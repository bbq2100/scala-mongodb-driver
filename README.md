scala-mongodb-driver
====================

What's this?
-----------
Provides a simple Scala API to be able to use [MongoDb] (http://www.mongodb.org). Developed solely for learning purposes. Example is taken from "Scala in Action by Nilanjan Raychaudhuri". Not scaled for real usage !!!

References:

* https://github.com/nraychaudhuri
* http://goo.gl/nI4IUY Scala in Action

Requirements
------------
* [JDK 1.8.0](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Scala 2.10.3](http://www.scala-lang.org/downloads)
* [sbt](http://code.google.com/p/simple-build-tool/) 
* [Git](http://git-scm.com/)
* [MongoDb 2.12.2](http://www.mongodb.org) 

How to build?
-------------
Clone repository from this [repository](https://github.com/qabbasi/scala-mongodb-driver) or fork:

    % git clone git@github.com:qabbasi/scala-mongodb-driver.git
    % cd scala-mongodb-driver

Run *sbt update* to download dependencies

    % sbt update

Run *sbt package* to create war file

    % sbt package

License
-------
scala-monogdb-driver
Copyright 2014 Qaiser Abbasi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
