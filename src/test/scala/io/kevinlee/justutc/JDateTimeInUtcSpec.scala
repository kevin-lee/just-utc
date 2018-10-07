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
object JDateTimeInUtcSpec extends Properties {
  override def tests: List[Prop] = List(
    Prop("testToLocalDateTime", testToLocalDateTime)
  )

  def testToLocalDateTime: Property[Unit] = for {
    expected <- Gen.long(Range.linear(Long.MinValue, Long.MaxValue)).log("expected")
    localDateTime = JDateTimeInUtc.toLocalDateTime(expected)
    actual = JDateTimeInUtc.toEpochMilli(localDateTime)
    _ <- actual === expected
  } yield ()
}
