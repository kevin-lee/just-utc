package io.kevinlee.justutc

import hedgehog._
import hedgehog.Gen._
import hedgehog.runner._
import hedgehog.Property. _
import hedgehog.core.{PropertyT, PropertyTReporting}
import hedgehog.predef.Monad

/**
  * @author Kevin Lee
  * @since 2018-10-04
  */
object UtcDateTimeSpec extends Properties {
  override def tests: List[Prop] = List(
    Prop("testFromUtcLocalDateTime", testFromUtcLocalDateTime)
  , Prop("testCompareToLess", testCompareToLess)
  , Prop("testCompareToMore", testCompareToMore)
  , Prop("testCompareToEqual", testCompareToEqual)
  , Prop("testEqual", testEqual)
  , Prop("testNotEqual", testNotEqual)
  , Prop("testLess", testLess)
  , Prop("testLessThanOrEqualTo_LessCase", testLessThanOrEqualTo_LessCase)
  , Prop("testLessThanOrEqualTo_EqualCase", testLessThanOrEqualTo_EqualCase)
  , Prop("testMore", testMore)
  , Prop("testMoreThanOrEqualTo_MoreCase", testMoreThanOrEqualTo_MoreCase)
  , Prop("testMoreThanOrEqualTo_EqualCase", testMoreThanOrEqualTo_EqualCase)
  )

  def testFromUtcLocalDateTime: Property[Unit] = for {
    expected <- Gen.long(Range.linear(1L, Long.MaxValue)).log("expected")
    localDateTime = JDateTimeInUtc.toLocalDateTime(expected)
    actual = UtcDateTime.fromUtcLocalDateTime(localDateTime)
    _ <- actual.epochMillis ==== expected
  } yield ()

  def testCompareToLess: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue >> 1)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
    _ <- UtcDateTime(x).compare(UtcDateTime(x + y)) ==== -1
  } yield ()

  def testCompareToMore: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue >> 1, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
    _ <- UtcDateTime(x).compare(UtcDateTime(x - y)) ==== 1
  } yield ()

  def testCompareToEqual: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
    _ <- UtcDateTime(x).compare(UtcDateTime(x)) ==== 0
  } yield ()

  def testEqual: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
    _ <- assert(UtcDateTime(x) == UtcDateTime(x))
  } yield ()

  def testNotEqual: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).filter(_ != x).forAll
    _ <- assert(UtcDateTime(x) != UtcDateTime(y))
  } yield ()

  def testLess: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue >> 1)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
    _ <- assert(UtcDateTime(x) < UtcDateTime(x + y))
  } yield ()

  def testLessThanOrEqualTo_LessCase: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue >> 1)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
    _ <- assert(UtcDateTime(x) <= UtcDateTime(x + y))
  } yield ()

  def testLessThanOrEqualTo_EqualCase: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
    _ <- assert(UtcDateTime(x) <= UtcDateTime(x))
  } yield ()

  def testMore: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue >> 1, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
    _ <- assert(UtcDateTime(x) > UtcDateTime(x - y))
  } yield ()

  def testMoreThanOrEqualTo_MoreCase: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue >> 1, Long.MaxValue)).forAll
    y <- Gen.long(Range.linear(1L, Long.MaxValue >> 1)).forAll
    _ <- assert(UtcDateTime(x) >= UtcDateTime(x - y))
  } yield ()

  def testMoreThanOrEqualTo_EqualCase: Property[Unit] = for {
    x <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).forAll
    _ <- assert(UtcDateTime(x) >= UtcDateTime(x))
  } yield ()

}
