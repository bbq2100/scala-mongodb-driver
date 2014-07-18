import com.mongodb.MongoClient

class MongoClientWrapper(val host: String = "127.0.0.1", val port: Int = 27017) {
  private val underlyingMongoDriver = new MongoClient(host, port)

  def version = underlyingMongoDriver.getVersion

  def dropDatabase(databaseName: String) = underlyingMongoDriver.dropDatabase(databaseName)

  def createOrGetExistingDatabase(databaseName: String) = DB(underlyingMongoDriver.getDB(databaseName))
}
