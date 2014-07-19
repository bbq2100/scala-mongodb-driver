package  qa.scala.mongodbdriver

import java.util.Locale

import com.mongodb.BasicDBObject

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


    val localeCollection: UpdateableCollection with LocalAware = db updateableCollectionWithLocaleAwareness  databaseName
    val localeDocument: BasicDBObject = new BasicDBObject
    localeDocument put ("name", "LOL")
    localeDocument put ("locale", Locale.getDefault().getLanguage)
    localeCollection += localeDocument

    //{ "_id" : { "$oid" : "53ca7bdec2602f841efc99f8"} , "name" : "LOL" , "locale" : "en"}
    println(localeCollection findOne(localeDocument))
  }
}

