package qa.scala.mongodbdriver

import com.mongodb.MongoClient

/**
 * Wraps MongoClient impl from com.mongodb.
 * Provides a simple Scala API.
 * Developed for learning purposes. Example taken from "Scala in Action by Nilanjan Raychaudhuri"
 * Not scaled for real usage !!!
 * @param host default 127.0.0.1
 * @param port default 27017
 * @see http://goo.gl/nI4IUY Scala in Action
 * @see https://github.com/nraychaudhuri
 */
class MongoClientWrapper(val host: String = "127.0.0.1",val port: Int = 27017) {
  private val underlyingMongoDriver = new MongoClient(host, port)

  def version = underlyingMongoDriver.getVersion

  def dropDatabase(databaseName: String) = underlyingMongoDriver.dropDatabase(databaseName)

  def createOrGetExistingDatabase(databaseName: String) = DB(underlyingMongoDriver.getDB(databaseName))
}
