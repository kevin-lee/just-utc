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

  def testFromUtcLocalDateTime: Property = for {
    expected <- Gen.long(Range.linear(1L, Long.MaxValue)).log("expected")
  } yield {
    val localDateTime = JDateTimeInUtc.toLocalDateTime(expected)
    val actual = Utc.fromUtcLocalDateTime(localDateTime)
    actual.epochMillis ==== expected
  }

  def testCompareToLess: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue >> 1)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
  } yield Utc(x).compare(Utc(x + y)) ==== -1

  def testCompareToMore: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue >> 1, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
  } yield Utc(x).compare(Utc(x - y)) ==== 1

  def testCompareToEqual: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
  } yield Utc(x).compare(Utc(x)) ==== 0

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def testEqual: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
  } yield Result.assert(Utc(x) == Utc(x))

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def testNotEqual: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).filter(_ != x).forAll
  } yield Result.assert(Utc(x) != Utc(y))

  def testLess: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue >> 1)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
  } yield Result.assert(Utc(x) < Utc(x + y))

  def testLessThanOrEqualTo_LessCase: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue >> 1)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
  } yield Result.assert(Utc(x) <= Utc(x + y))

  def testLessThanOrEqualTo_EqualCase: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
  } yield Result.assert(Utc(x) <= Utc(x))

  def testMore: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue >> 1, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
  } yield Result.assert(Utc(x) > Utc(x - y))

  def testMoreThanOrEqualTo_MoreCase: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue >> 1, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
  } yield Result.assert(Utc(x) >= Utc(x - y))

  def testMoreThanOrEqualTo_EqualCase: Property = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
  } yield Result.assert(Utc(x) >= Utc(x))

}
