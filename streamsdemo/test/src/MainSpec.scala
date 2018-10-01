import java.util.{ Properties }

import org.scalatest.{ FlatSpec }
import org.apache.kafka.streams.{ StreamsConfig, TopologyTestDriver }
import org.apache.kafka.streams.test.{ ConsumerRecordFactory, OutputVerifier }

import Main._
import Topics._

object TestUtils {
  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092") // value not used
    p
  }
}

class MainSpec extends FlatSpec {
  import org.apache.kafka.streams.scala.Serdes._
  import TestUtils._

  val recordFactory = new ConsumerRecordFactory(ByteArray.serializer(), String.serializer())
  val testDriver = new TopologyTestDriver(topology(), props)

  it should "convert binary strings to base 2 integers" in {
    val binaryRecord = recordFactory.create(BinaryTopic, 2.toBinaryString)
    testDriver.pipeInput(binaryRecord)

    val integerResultRecord = testDriver.readOutput(IntegerTopic, ByteArray.deserializer(), Integer.deserializer())
    assert(integerResultRecord.value == 2)
  }

  it should "safely handle invalid binary strings" in {
    val invalidBinaryValue = "12345abcde"
    val invalidBinaryRecord = recordFactory.create(BinaryTopic, invalidBinaryValue)
    testDriver.pipeInput(invalidBinaryRecord)

    val failureResult = testDriver.readOutput(FailureTopic, ByteArray.deserializer(), String.deserializer())
    assert(failureResult.value == invalidBinaryValue)
  }
}
