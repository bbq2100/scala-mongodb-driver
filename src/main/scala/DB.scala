package qa.scala.mongodbdriver

import com.mongodb.{DB => MongoDB}

import scala.collection.convert.Wrappers.JSetWrapper

class DB private(val underlyingDb: MongoDB) {
  def collectionNames = for (names <- new JSetWrapper(underlyingDb.getCollectionNames)) yield names
}

object DB {
  def apply(underlyingDb: MongoDB) = new DB(underlyingDb)
}
