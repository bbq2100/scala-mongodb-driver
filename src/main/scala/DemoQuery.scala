package qa.scala.mongodbdriver

import com.mongodb.{DBCursor, BasicDBObject}

object DemoQuery {
  def main(args: Array[String]) {
    def client = new MongoClientWrapper
    val database: DB = client.createOrGetExistingDatabase(databaseName = "query-test")
    val collectionName: String = "test"


    val updateableCollection: UpdateableCollection = database.updateableCollection(name = collectionName)

    for (x <- 1 to 100) {
      updateableCollection += new BasicDBObject("i", x)
    }

    val rangeQuery: BasicDBObject = new BasicDBObject("i", new BasicDBObject("$gt", 20))
    val richQuery: Query = Query(rangeQuery).skip(20).limit(10)

    val readOnlyCollection = database readOnlyCollection (name = collectionName)
    val resultCursor: DBCursor = readOnlyCollection find richQuery
    while (resultCursor.hasNext) {
      /**
       *{ "_id" : { "$oid" : "53cabf68c2601ff502552bda"} , "i" : 41}
        { "_id" : { "$oid" : "53cabf68c2601ff502552bdb"} , "i" : 42}
        { "_id" : { "$oid" : "53cabf68c2601ff502552bdc"} , "i" : 43}
        { "_id" : { "$oid" : "53cabf68c2601ff502552bdd"} , "i" : 44}
        { "_id" : { "$oid" : "53cabf68c2601ff502552bde"} , "i" : 45}
        { "_id" : { "$oid" : "53cabf68c2601ff502552bdf"} , "i" : 46}
        { "_id" : { "$oid" : "53cabf68c2601ff502552be0"} , "i" : 47}
        { "_id" : { "$oid" : "53cabf68c2601ff502552be1"} , "i" : 48}
        { "_id" : { "$oid" : "53cabf68c2601ff502552be2"} , "i" : 49}
        { "_id" : { "$oid" : "53cabf68c2601ff502552be3"} , "i" : 50}
       */
      println(resultCursor.next)
    }
  }
}
