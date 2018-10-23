import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.collection.JavaConverters._

object flood {
  val threads   = Integer.getInteger("threads",  10)
  val rampup    = Integer.getInteger("rampup",   10).toLong
  val duration  = Integer.getInteger("duration", 15).toLong

  val httpConfigFlood = http
    .disableResponseChunksDiscarding
    .extraInfoExtractor((extraInfo) =>
      // Status Code
      extraInfo.response.statusCode.get.toString ::
      // Bytes
      Option(extraInfo.response.header("Content-Length")).getOrElse("0") ::
      // URL
      extraInfo.response.uri.get.toString ::
      // Headers
      (if (extraInfo.request.getHeaders.isEmpty) "" else extraInfo.request.getHeaders.entries.asScala.toList.mkString(";")) ::
      (if (extraInfo.response.headers.isEmpty) "" else extraInfo.response.headers.entries.asScala.toList.mkString(";")) ::
      // Request Data
      (if (extraInfo.response.statusCode.get.toInt != 200) Option(extraInfo.request.getStringData).getOrElse("") else "") ::
      // Response Data
      (if (extraInfo.response.statusCode.get.toInt != 200) Option(extraInfo.response.body.string.toString).getOrElse("") else "") ::
      List()
    )
}
