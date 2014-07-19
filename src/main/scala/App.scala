package  qa.scala.mongodbdriver
import com.mongodb.BasicDBObject
import qa.scala.mongodbdriver._

object App {
  def main(args: Array[String]) {
    val databaseName = "test"

    def client = new MongoClientWrapper
    def db = client createOrGetExistingDatabase databaseName

    for (collectionName <- db collectionNames) println(collectionName)

    val qaiserDocument = new BasicDBObject
    qaiserDocument put ("name", "Qaiser")
    qaiserDocument put ("age", 23)
    qaiserDocument put ("info", "Scala rocks! Also does MongoDB")

    val updateableCollection = db updateableCollection databaseName
    updateableCollection += qaiserDocument

    // { "_id" : { "$oid" : "53ca2fb1c26073a4d83b132f"} , "name" : "Qaiser" , "age" : 23 , "info" : "Scala rocks! Also MongoDB"}
    println(updateableCollection findOne qaiserDocument)
  }
}

