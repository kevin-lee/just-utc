package just.utc

import hedgehog._
import hedgehog.runner._

/**
  * @author Kevin Lee
  * @since 2018-10-04
  */
object JDateTimeInUtcSpec extends Properties {
  override def tests: List[Test] = List(
    property("testToLocalDateTime", testToLocalDateTime)
  )

  def testToLocalDateTime: Property = for {
    expected <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).log("expected")
    localDateTime = JDateTimeInUtc.toLocalDateTime(expected)
    actual = JDateTimeInUtc.toEpochMilli(localDateTime)
  } yield actual ==== expected
}
