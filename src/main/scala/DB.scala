package qa.scala.mongodbdriver

import com.mongodb.{DB => MongoDB}

import scala.collection.convert.Wrappers.JSetWrapper

class DB private(val underlyingDb: MongoDB) {

  private def collection(name: String) = underlyingDb getCollection name

  def readOnlyCollection(name: String) = new DBCollection(collection(name)) with Memoizer

  def administrableCollection(name: String) = new DBCollection(collection(name)) with Administrable with Memoizer

  def updateableCollection(name: String) = new UpdateableCollection(collection(name))

  def collectionNames = for (names <- new JSetWrapper(underlyingDb.getCollectionNames)) yield names
}

object DB {
  def apply(underlyingDb: MongoDB) = new DB(underlyingDb)
}
