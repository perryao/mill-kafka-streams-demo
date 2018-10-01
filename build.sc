import mill._, scalalib._, scalafmt._

object streamsdemo extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.12.6"
  def ivyDeps = Agg(
    ivy"org.slf4j:slf4j-log4j12:1.7.5",
    ivy"org.apache.kafka:kafka-streams:2.0.0",
    ivy"org.apache.kafka:kafka-clients:2.0.0",
    ivy"org.apache.kafka::kafka-streams-scala:2.0.0",
  )
  object test extends Tests {
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.0.5",
      ivy"org.apache.kafka:kafka-streams-test-utils:2.0.0"
    )
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}
