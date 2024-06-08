package just.utc

import java.time.LocalDateTime

import hedgehog._
import hedgehog.runner._

/** @author Kevin Lee
  * @since 2018-10-04
  */
object JDateTimeInUtcSpec extends Properties with CrossVersion {
  override def tests: List[Test] = List(
    property(
      "round-trip test of JDateTimeInUtc.toLocalDateTime and JDateTimeInUtc.toEpochMilli",
      testToLocalDateTime
    )
  )

  def testToLocalDateTime: Property = for {
    expected <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).log("expected")
  } yield {
    val localDateTime: Either[DateTimeError, LocalDateTime] = JDateTimeInUtc.toLocalDateTime(expected)
    val actual: Either[DateTimeError, Long]                 = localDateTime.flatMap(JDateTimeInUtc.toEpochMilli)
    actual ==== Right(expected)
  }
}
