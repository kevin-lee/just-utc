package just.utc

import hedgehog._
import hedgehog.runner._

import java.time.{Instant, LocalDateTime, ZoneOffset}

/** @author Kevin Lee
  * @since 2018-10-04
  */
object UtcSpec extends Properties {
  override def tests: List[Test] = List(
    property("testFromUtcLocalDateTime", testFromUtcLocalDateTime),
    property("testCompareToLess", testCompareToLess),
    property("testCompareToMore", testCompareToMore),
    property("testCompareToEqual", testCompareToEqual),
    property("testEqual", testEqual),
    property("testNotEqual", testNotEqual),
    property("testLess", testLess),
    property("testLessThanOrEqualTo_LessCase", testLessThanOrEqualTo_LessCase),
    property("testLessThanOrEqualTo_EqualCase", testLessThanOrEqualTo_EqualCase),
    property("testMore", testMore),
    property("testMoreThanOrEqualTo_MoreCase", testMoreThanOrEqualTo_MoreCase),
    property("testMoreThanOrEqualTo_EqualCase", testMoreThanOrEqualTo_EqualCase),
    property("test Utc.fromInstant", testFromInstant),
    property("testMoreThanOrEqualTo_EqualCase", testMoreThanOrEqualTo_EqualCase),
    property("test epochMillisWithNanos", testEpochMillisWithNanos),
    property("test minute", testMinute),
    property("test seconds", testSeconds),
    property("test milliSeconds", testMilliSeconds)
  )

  val validInstantMin: Long = Long.MinValue + 1
  val validInstantMax: Long = Long.MaxValue - 1

  def computeMaxForMinus(l: Long): Long =
    if (l < 0)
      l - Long.MinValue
    else
      Long.MaxValue

  def computeMaxForPlus(l: Long): Long =
    if (l < 0)
      Long.MaxValue
    else
      Long.MaxValue - l

  def testFromUtcLocalDateTime: Property = for {
    expected <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("expected")
  } yield {
    val localDateTime = JDateTimeInUtc.unsafeToLocalDateTime(expected)
    val actual        = Utc.fromUtcLocalDateTime(localDateTime)
    actual.epochMillis ==== expected
  }

  def testCompareToLess: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
    y <- Gen.long(Range.linear(1L, computeMaxForPlus(x))).log("y")
  } yield Utc.unsafeFromEpochMillis(x).compare(Utc.unsafeFromEpochMillis(x + y)) ==== -1

  def testCompareToMore: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, Long.MaxValue)).log("x")
    y <- Gen.long(Range.linear(1L, computeMaxForMinus(x))).log("y")
  } yield Utc.unsafeFromEpochMillis(x).compare(Utc.unsafeFromEpochMillis(x - y)) ==== 1

  def testCompareToEqual: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
  } yield Utc.unsafeFromEpochMillis(x).compare(Utc.unsafeFromEpochMillis(x)) ==== 0

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def testEqual: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
  } yield {
    Result.assert(Utc.unsafeFromEpochMillis(x) == Utc.unsafeFromEpochMillis(x))
  }

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def testNotEqual: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
    y <- Gen.long(Range.linear(validInstantMin, validInstantMax)).filter(_ != x).log("y")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) != Utc.unsafeFromEpochMillis(y))

  def testLess: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
    y <- Gen.long(Range.linear(1L, computeMaxForPlus(x))).log("y")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) < Utc.unsafeFromEpochMillis(x + y))

  def testLessThanOrEqualTo_LessCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
    y <- Gen.long(Range.linear(1L, computeMaxForPlus(x))).log("y")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) <= Utc.unsafeFromEpochMillis(x + y))

  def testLessThanOrEqualTo_EqualCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) <= Utc.unsafeFromEpochMillis(x))

  def testMore: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, Long.MaxValue)).log("x")
    y <- Gen.long(Range.linear(1L, computeMaxForMinus(x))).log("y")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) > Utc.unsafeFromEpochMillis(x - y))

  def testMoreThanOrEqualTo_MoreCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, Long.MaxValue)).log("x")
    y <- Gen.long(Range.linear(1L, computeMaxForMinus(x))).log("y")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) >= Utc.unsafeFromEpochMillis(x - y))

  def testMoreThanOrEqualTo_EqualCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("x")
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) >= Utc.unsafeFromEpochMillis(x))

  def testFromInstant: Property =
    for {
      now <- Gen.constant(Instant.now()).log("now")
    } yield {
      val utc = Utc.fromInstant(now)

      val expected = now
      val actual   = utc.instant
      actual ==== expected
    }

  def testEpochMillisWithNanos: Property = for {
    now      <- Gen.constant(Instant.now()).log("now")
    expected <- Gen.constant(now.toEpochMilli * 1000000 + now.getNano % 1000000).log("expected")
  } yield {
    val actual = Utc.fromInstant(now)
    actual.epochMillisWithNanos ==== expected
  }

  def testMinute: Property = for {
    now           <- Gen.constant(Instant.now()).log("now")
    localDateTime <- Gen.constant(LocalDateTime.ofInstant(now, ZoneOffset.UTC)).log("localDateTime")
    expected      <- Gen.constant(localDateTime.getMinute).log("expected")
  } yield {
    val actual = Utc.fromInstant(now)
    actual.minute ==== expected
  }

  def testSeconds: Property = for {
    now           <- Gen.constant(Instant.now()).log("now")
    localDateTime <- Gen.constant(LocalDateTime.ofInstant(now, ZoneOffset.UTC)).log("localDateTime")
    expected      <- Gen.constant(localDateTime.getSecond).log("expected")
  } yield {
    val actual = Utc.fromInstant(now)
    actual.seconds ==== expected
  }

  def testMilliSeconds: Property = for {
    now      <- Gen.constant(Instant.now()).log("now")
    expected <- Gen.constant(now.getNano / 1000000).log("expected")
  } yield {
    val actual = Utc.fromInstant(now)
    actual.milliSeconds ==== expected
  }

}
