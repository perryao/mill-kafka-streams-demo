# Overview
This sample project demonstrates using Mill to build Kafka Streams applications
written in Scala.

Work on an accompanying blog post is in progress.


# Requirements
[Scala][1] 2.12.6 or later is required, as well as [Mill][2] 0.2.6 or later.

# Tests
To run the unit tests, execute:

```
    $ mill streamsdemo.test
```

# Running
To run the Kafka Streams app, execute:

```
    $ mill streamsdemo.run
```

[1]: https://www.scala-lang.org/download/
[2]: https://www.lihaoyi.com/mill/
