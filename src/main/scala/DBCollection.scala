package qa.scala.mongodbdriver

import com.mongodb.{DBCollection => MongoDBCollection, WriteResult, DBCursor, DBObject}

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

trait Memoizer extends ReadOnly {
  val callHistory = Map[Int, DBObject]()

  override def findOne(document: DBObject) = callHistory.getOrElse(document.hashCode, super.findOne(document))
}

/**
 * Decorating the -= method from trait Updateable -> removing documents from inner-cache in trait Memoizer in order to remain consistent.
 * @param underlyingCollectionUtility
 * @see Updateable.-=()
 */
class UpdateableCollection(override val underlyingCollectionUtility: MongoDBCollection)
  extends DBCollection(underlyingCollectionUtility) with Updateable with Memoizer {
  override def -=(document: DBObject) = {
    callHistory - document.hashCode
    super.-=(document)
  }
}