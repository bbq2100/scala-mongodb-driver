import qa.scala.mongodbdriver._

object test {
  def main(args: Array[String]) {
    def client = new MongoClientWrapper
    def db = client.createOrGetExistingDatabase("test")

    for (collectionName <- db.collectionNames) println(collectionName)
  }
}

