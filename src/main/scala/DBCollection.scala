package qa.scala.mongodbdriver

import com.mongodb.{DBCollection => MongoDBCollection, DBObject}

class DBCollection(override val underlyingCollectionUtility: MongoDBCollection) extends ReadOnly

trait ReadOnly {

  val underlyingCollectionUtility: MongoDBCollection

  def name = underlyingCollectionUtility getName

  def fullName = underlyingCollectionUtility getFullName

  def find(document: DBObject) = underlyingCollectionUtility find document

  def findOne(document: DBObject) = underlyingCollectionUtility findOne document

  def getCount(document: DBObject) = underlyingCollectionUtility getCount document
}

trait Administrable extends ReadOnly {
  def drop = underlyingCollectionUtility drop

  def dropIndexes = underlyingCollectionUtility dropIndexes
}


trait Updateable extends ReadOnly {
  def -=(document: DBObject) = underlyingCollectionUtility remove document

  def +=(document: DBObject) = underlyingCollectionUtility save document
}
