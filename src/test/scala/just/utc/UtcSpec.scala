package just.utc

import java.time.Instant

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

  val validInstantMin: Long = Instant.MIN.getEpochSecond
  val validInstantMax: Long = Instant.MAX.getEpochSecond

  // TODO: Do invalid range handling test as well
  def testFromUtcLocalDateTime: Property = for {
    expected <- Gen.long(Range.linear(validInstantMin, validInstantMax)).log("expected")
  } yield {
    val localDateTime = JDateTimeInUtc.toLocalDateTime(expected)
    val actual = Utc.fromUtcLocalDateTime(localDateTime)
    actual.epochMillis ==== expected
  }

  def testCompareToLess: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
    y <- Gen.long(Range.linear(1L, validInstantMax)).forAll
  } yield Utc.unsafeFromEpochMillis(x).compare(Utc.unsafeFromEpochMillis(x + y)) ==== -1

  def testCompareToMore: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, validInstantMax)).forAll
  } yield Utc.unsafeFromEpochMillis(x).compare(Utc.unsafeFromEpochMillis(x - y)) ==== 1

  def testCompareToEqual: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
  } yield Utc.unsafeFromEpochMillis(x).compare(Utc.unsafeFromEpochMillis(x)) ==== 0

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def testEqual: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
  } yield {
    Result.assert(Utc.unsafeFromEpochMillis(x) == Utc.unsafeFromEpochMillis(x))
  }

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def testNotEqual: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
    y <- Gen.long(Range.linear(validInstantMin, validInstantMax)).filter(_ != x).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) != Utc.unsafeFromEpochMillis(y))

  def testLess: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
    y <- Gen.long(Range.linear(1L, validInstantMax)).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) < Utc.unsafeFromEpochMillis(x + y))

  def testLessThanOrEqualTo_LessCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
    y <- Gen.long(Range.linear(1L, validInstantMax)).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) <= Utc.unsafeFromEpochMillis(x + y))

  def testLessThanOrEqualTo_EqualCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) <= Utc.unsafeFromEpochMillis(x))

  def testMore: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, validInstantMax)).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) > Utc.unsafeFromEpochMillis(x - y))

  def testMoreThanOrEqualTo_MoreCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, validInstantMax)).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) >= Utc.unsafeFromEpochMillis(x - y))

  def testMoreThanOrEqualTo_EqualCase: Property = for {
    x <- Gen.long(Range.linear(validInstantMin, validInstantMax)).forAll
  } yield Result.assert(Utc.unsafeFromEpochMillis(x) >= Utc.unsafeFromEpochMillis(x))

}
