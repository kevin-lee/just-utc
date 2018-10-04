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
  , Prop("testLess", testLess)
  , Prop("testLessThanOrEqual_LessCase", testLessThanOrEqual_LessCase)
  , Prop("testLessThanOrEqual_EqualCase", testLessThanOrEqual_EqualCase)
  , Prop("testMore", testMore)
  , Prop("testMoreThanOrEqual_MoreCase", testMoreThanOrEqual_MoreCase)
  , Prop("testMoreThanOrEqual_EqualCase", testMoreThanOrEqual_EqualCase)
  )

  def testFromUtcLocalDateTime: Property[Unit] = for {
    expected <- Gen.integral(Range.linear(1L, Long.MaxValue)).log("expected")
    localDateTime = JDateTimeInUtc.toLocalDateTime(expected)
    actual = UtcDateTime.fromUtcLocalDateTime(localDateTime)
    _ <- actual.epochMillis === expected
  } yield ()

  def testCompareToLess: Property[Unit] = for {
    x <- Gen.integral(Range.linear(1L, 100000L)).log("x")
    y <- Gen.integral(Range.linear(1L, 1000L)).log("y")
    _ <- UtcDateTime(x).compareTo(UtcDateTime(x + y)) === -1
  } yield ()

  def testCompareToMore: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    y <- Gen.integral(Range.linear(1L, 1000L)).log("x")
    _ <- UtcDateTime(x).compareTo(UtcDateTime(x - y)) === 1
  } yield ()

  def testCompareToEqual: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    _ <- UtcDateTime(x).compareTo(UtcDateTime(x)) === 0
  } yield ()

  def testEqual: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    _ <- assert(UtcDateTime(x) == UtcDateTime(x))
  } yield ()

  def testLess: Property[Unit] = for {
    x <- Gen.integral(Range.linear(1L, 100000L)).log("x")
    y <- Gen.integral(Range.linear(1L, 1000L)).log("y")
    _ <- assert(UtcDateTime(x) < UtcDateTime(x + y))
  } yield ()

  def testLessThanOrEqual_LessCase: Property[Unit] = for {
    x <- Gen.integral(Range.linear(1L, 100000L)).log("x")
    y <- Gen.integral(Range.linear(1L, 1000L)).log("y")
    _ <- assert(UtcDateTime(x) <= UtcDateTime(x + y))
  } yield ()

  def testLessThanOrEqual_EqualCase: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    _ <- assert(UtcDateTime(x) <= UtcDateTime(x))
  } yield ()

  def testMore: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    y <- Gen.integral(Range.linear(1L, 1000L)).log("x")
    _ <- assert(UtcDateTime(x) > UtcDateTime(x - y))
  } yield ()

  def testMoreThanOrEqual_MoreCase: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    y <- Gen.integral(Range.linear(1L, 1000L)).log("x")
    _ <- assert(UtcDateTime(x) >= UtcDateTime(x - y))
  } yield ()

  def testMoreThanOrEqual_EqualCase: Property[Unit] = for {
    x <- Gen.integral(Range.linear(10000L, 100000L)).log("y")
    _ <- assert(UtcDateTime(x) >= UtcDateTime(x))
  } yield ()

}
