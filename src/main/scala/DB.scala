package qa.scala.mongodbdriver

import com.mongodb.{DB => MongoDB}

import scala.collection.convert.Wrappers.JSetWrapper

class DB private(val underlyingDb: MongoDB) {

  private def collection(name: String) = underlyingDb getCollection name

  def readOnlyCollection(name: String) = new DBCollection(collection(name))

  def administrableCollection(name: String) = new DBCollection(collection(name)) with Administrable

  def updateableCollection(name: String) = new DBCollection(collection(name)) with Updateable

  def collectionNames = for (names <- new JSetWrapper(underlyingDb.getCollectionNames)) yield names
}

object DB {
  def apply(underlyingDb: MongoDB) = new DB(underlyingDb)
}
