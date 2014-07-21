package qa.scala.mongodbdriver

import java.util.Locale

import com.mongodb.{DBCollection => MongoDBCollection, DBCursor, DBObject}

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

  def find(document: DBObject): DBCursor = underlyingCollectionUtility find document

  def findOne(document: DBObject): DBObject = underlyingCollectionUtility findOne document

  /**
   * A tail-recursive implementation which utilizes pattern matching to apply query options on the result cursor.
   */
  def find(queryArg: Query): DBCursor = {
    def applyOptions(cursor: DBCursor, option: QueryOption): DBCursor = {
      option match {
        case Skip(skip, nextOption) => applyOptions(cursor.skip(skip), nextOption)
        case Sort(sorting, nextOption) => applyOptions(cursor.sort(sorting), nextOption)
        case Limit(limit, nextOption) => applyOptions(cursor.limit(limit), nextOption)
        case NoOption => cursor
      }
    }
    applyOptions(find(queryArg.query), queryArg.option)
  }

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

/**
 * Defines the root QueryOption.
 */
sealed trait QueryOption

/**
 * Indicates the end of the QueryOptionChaining.
 * Used as default value parameter in Query.
 */
case object NoOption extends QueryOption

/**
 * @param sorting Used for defining sorting criteria
 * @param anotherOption
 */
case class Sort(sorting: DBObject, anotherOption: QueryOption) extends QueryOption

case class Skip(number: Int, anotherOption: QueryOption) extends QueryOption

case class Limit(limit: Int, anotherOption: QueryOption) extends QueryOption

/**
 * Query provides a fluent API to make arbitrary complex queries.
 * Used patterns Compositions pattern (GoF), pattern matching
 */
case class Query(query: DBObject, option: QueryOption = NoOption) {
  def sort(sorting: DBObject) = Query(query, Sort(sorting, option))
  def skip(skip: Int) = Query(query, Skip(skip, option))
  def limit(limit: Int) = Query(query, Limit(limit, option))
}