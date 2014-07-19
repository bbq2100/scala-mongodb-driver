package qa.scala.mongodbdriver

import java.util.Locale

import com.mongodb.{DBObject, DBCollection => MongoDBCollection}

/**
 * Default DB Collection result.
 * @param underlyingCollectionUtility
 */
class DBCollection(override val underlyingCollectionUtility: MongoDBCollection) extends ReadOnly

/**
 * Represents root trait. Contains basic functionality.
 */
trait ReadOnly {

  val underlyingCollectionUtility: MongoDBCollection

  def name = underlyingCollectionUtility getName

  def fullName = underlyingCollectionUtility getFullName

  def find(document: DBObject) = underlyingCollectionUtility find document

  def findOne(document: DBObject) = underlyingCollectionUtility findOne document

  def getCount(document: DBObject) = underlyingCollectionUtility getCount document
}

/**
 * Provides administrative operations to manage the db.
 */
trait Administrable extends ReadOnly {
  def drop = underlyingCollectionUtility drop

  def dropIndexes = underlyingCollectionUtility dropIndexes
}

/**
 * Defines Create and Delete operation on the document.
 */
trait Updateable extends ReadOnly {
  def -=(document: DBObject) = underlyingCollectionUtility remove document

  def +=(document: DBObject) = underlyingCollectionUtility save document
}

/**
 * Memoizer backs fetched db results in a inner hashmap.
 * If findOne is called already with same args, then the result will be taken from the cache.
 */
trait Memoizer extends ReadOnly {
  val callHistory = Map[Int, DBObject]()

  override def findOne(document: DBObject) = callHistory.getOrElse(document.hashCode, super.findOne(document))
}

/**
 * Injecting locale sensitive information before running the find operation.
 */
trait LocalAware extends ReadOnly {
  override def findOne(document: DBObject) = {
    document put ("locale" , Locale.getDefault().getLanguage)
    super.findOne(document)
  }

  override def find(document: DBObject) = {
    document put ("locale" , Locale.getDefault().getLanguage)
    super.find(document)
  }
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