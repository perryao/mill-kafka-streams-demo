import java.util.{ Properties }
import java.util.concurrent.TimeUnit
import scala.util.Try
import scala.language.implicitConversions

import org.apache.kafka.streams.scala.{ StreamsBuilder }
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

object Topics {
  val BinaryTopic = "BinaryNumbersTopic"
  val IntegerTopic = "IntegerNumbersTopic"
  val FailureTopic = "FailureTopic"
}

object Main extends App {
  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.Serdes._
  import Topics._

  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-demo")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p
  }

  def topology(): Topology = {
    val builder = new StreamsBuilder()

    val binaryNumbers: KStream[Array[Byte], String] = builder.stream(BinaryTopic)
    val branches: Array[KStream[Array[Byte], String]] = binaryNumbers.branch(
      (k, v) => Try(java.lang.Integer.parseInt(v, 2)).isSuccess,
      (k, v) => Try(java.lang.Integer.parseInt(v, 2)).isFailure
    )
    val integerNumbers: KStream[Array[Byte], Integer] = branches.head
      .mapValues(x => java.lang.Integer.parseInt(x, 2))
    integerNumbers.to(IntegerTopic)
    branches.last.to(FailureTopic)

    builder.build()
  }

  val streams = new KafkaStreams(topology(), props)
  streams.cleanUp()
  streams.start()
  sys.ShutdownHookThread {
    streams.close(10, TimeUnit.SECONDS)
  }
}
