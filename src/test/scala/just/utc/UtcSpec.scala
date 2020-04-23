package just.utc

import hedgehog._
import hedgehog.runner._

/**
  * @author Kevin Lee
  * @since 2018-10-04
  */
object UtcSpec extends Properties {
  override def tests: List[Test] = List(
    property("testFromUtcLocalDateTime", testFromUtcLocalDateTime)
  , property("testCompareToLess", testCompareToLess)
  , property("testCompareToMore", testCompareToMore)
  , property("testCompareToEqual", testCompareToEqual)
  , property("testEqual", testEqual)
  , property("testNotEqual", testNotEqual)
  , property("testLess", testLess)
  , property("testLessThanOrEqualTo_LessCase", testLessThanOrEqualTo_LessCase)
  , property("testLessThanOrEqualTo_EqualCase", testLessThanOrEqualTo_EqualCase)
  , property("testMore", testMore)
  , property("testMoreThanOrEqualTo_MoreCase", testMoreThanOrEqualTo_MoreCase)
  , property("testMoreThanOrEqualTo_EqualCase", testMoreThanOrEqualTo_EqualCase)
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
    val actual = Utc.fromUtcLocalDateTime(localDateTime)
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

}
