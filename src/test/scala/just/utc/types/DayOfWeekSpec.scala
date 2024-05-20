package just.utc.types

import hedgehog._
import hedgehog.runner._

import java.time.{DayOfWeek => JDayOfWeek}

/** @author Kevin Lee
  * @since 2024-05-20
  */
object DayOfWeekSpec extends Properties {
  override def tests: List[Test] =
    List(
      example("test monday", testMonday),
      example("test tuesday", testTuesday),
      example("test wednesday", testWednesday),
      example("test thursday", testThursday),
      example("test friday", testFriday),
      example("test saturday", testSaturday),
      example("test sunday", testSunday),
      property("test value", testValue),
      property("test fromJava", testFromJava),
      property("test fromDayOfWeekInt", testFromDayOfWeekInt),
      property("test fromDayOfWeekInt with invalid input", testFromDayOfWeekIntInvalid),
      property("test unsafeFromDayOfWeekInt", testUnsafeFromDayOfWeekInt),
      property("test unsafeFromDayOfWeekInt with invalid input", testUnsafeFromDayOfWeekIntInvalid),
      property("test toJava", testToJava)
    )

  def testMonday: Result =
    DayOfWeek.monday ==== DayOfWeek.Monday

  def testTuesday: Result =
    DayOfWeek.tuesday ==== DayOfWeek.Tuesday

  def testWednesday: Result =
    DayOfWeek.wednesday ==== DayOfWeek.Wednesday

  def testThursday: Result =
    DayOfWeek.thursday ==== DayOfWeek.Thursday

  def testFriday: Result =
    DayOfWeek.friday ==== DayOfWeek.Friday

  def testSaturday: Result =
    DayOfWeek.saturday ==== DayOfWeek.Saturday

  def testSunday: Result =
    DayOfWeek.sunday ==== DayOfWeek.Sunday

  def testValue: Property =
    for {
      dayOfWeekPair <- Gen
                         .element1(
                           JDayOfWeek.MONDAY    -> DayOfWeek.monday,
                           JDayOfWeek.TUESDAY   -> DayOfWeek.tuesday,
                           JDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday,
                           JDayOfWeek.THURSDAY  -> DayOfWeek.thursday,
                           JDayOfWeek.FRIDAY    -> DayOfWeek.friday,
                           JDayOfWeek.SATURDAY  -> DayOfWeek.saturday,
                           JDayOfWeek.SUNDAY    -> DayOfWeek.sunday
                         )
                         .log("(dayOfWeek, jDayOfWeek)")
    } yield {
      val (jDayOfWeek, dayOfWeek) = dayOfWeekPair
      val expected                = jDayOfWeek.getValue
      val actual                  = dayOfWeek.value
      actual ==== expected
    }

  def testFromJava: Property =
    for {
      dayOfWeekPair <- Gen
                         .element1(
                           JDayOfWeek.MONDAY    -> DayOfWeek.monday,
                           JDayOfWeek.TUESDAY   -> DayOfWeek.tuesday,
                           JDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday,
                           JDayOfWeek.THURSDAY  -> DayOfWeek.thursday,
                           JDayOfWeek.FRIDAY    -> DayOfWeek.friday,
                           JDayOfWeek.SATURDAY  -> DayOfWeek.saturday,
                           JDayOfWeek.SUNDAY    -> DayOfWeek.sunday
                         )
                         .log("dayOfWeekPair")
    } yield {
      val (jDayOfWeek, dayOfWeek) = dayOfWeekPair

      val input    = jDayOfWeek
      val expected = dayOfWeek
      val actual   = DayOfWeek.fromJava(input)
      (actual ==== expected)
        .log(
          s"""jDayOfWeek=$jDayOfWeek
             | dayOfWeek=$dayOfWeek
             |     input=$input
             |    actual=$actual
             |  expected=$expected
             |""".stripMargin
        )
    }

  def testFromDayOfWeekInt: Property =
    for {
      dayOfWeekPair <- Gen
                         .element1(
                           JDayOfWeek.MONDAY    -> DayOfWeek.monday,
                           JDayOfWeek.TUESDAY   -> DayOfWeek.tuesday,
                           JDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday,
                           JDayOfWeek.THURSDAY  -> DayOfWeek.thursday,
                           JDayOfWeek.FRIDAY    -> DayOfWeek.friday,
                           JDayOfWeek.SATURDAY  -> DayOfWeek.saturday,
                           JDayOfWeek.SUNDAY    -> DayOfWeek.sunday
                         )
                         .log("(dayOfWeek, jDayOfWeek)")
    } yield {
      val (jDayOfWeek, dayOfWeek) = dayOfWeekPair

      val input    = jDayOfWeek.getValue
      val expected = Option(dayOfWeek)
      val actual   = DayOfWeek.fromDayOfWeekInt(input)
      (actual ==== expected)
        .log(
          s"""jDayOfWeek=$jDayOfWeek
             | dayOfWeek=$dayOfWeek
             |     input=$input
             |    actual=$actual
             |  expected=$expected
             |""".stripMargin
        )
    }

  def testFromDayOfWeekIntInvalid: Property =
    for {
      input <- Gen.int(Range.linear(8, 1000)).log("input")
    } yield {
      val expected = Option.empty[DayOfWeek]
      val actual   = DayOfWeek.fromDayOfWeekInt(input)
      actual ==== expected
    }

  def testUnsafeFromDayOfWeekInt: Property =
    for {
      dayOfWeekPair <- Gen
                         .element1(
                           JDayOfWeek.MONDAY    -> DayOfWeek.monday,
                           JDayOfWeek.TUESDAY   -> DayOfWeek.tuesday,
                           JDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday,
                           JDayOfWeek.THURSDAY  -> DayOfWeek.thursday,
                           JDayOfWeek.FRIDAY    -> DayOfWeek.friday,
                           JDayOfWeek.SATURDAY  -> DayOfWeek.saturday,
                           JDayOfWeek.SUNDAY    -> DayOfWeek.sunday
                         )
                         .log("(dayOfWeek, jDayOfWeek)")
    } yield {
      val (jDayOfWeek, dayOfWeek) = dayOfWeekPair

      val input    = jDayOfWeek.getValue
      val expected = dayOfWeek
      val actual   = DayOfWeek.unsafeFromDayOfWeekInt(input)
      (actual ==== expected)
        .log(
          s"""jDayOfWeek=$jDayOfWeek
             | dayOfWeek=$dayOfWeek
             |     input=$input
             |    actual=$actual
             |  expected=$expected
             |""".stripMargin
        )
    }

  def testUnsafeFromDayOfWeekIntInvalid: Property =
    for {
      input <- Gen.int(Range.linear(8, 1000)).log("input")
    } yield {

      val expected = new IllegalArgumentException(
        "Invalid day of week Int. It should be an Int value between 1 to 7 (1=Monday, 7=Sunday)"
      )
      try {
        val actual = DayOfWeek.unsafeFromDayOfWeekInt(input)
        Result
          .failure
          .log(
            s"""throwing IllegalArgumentException was expected but got $actual instead.
             |""".stripMargin
          )
      } catch {
        case ex: IllegalArgumentException =>
          ex.getMessage ==== expected.getMessage
        case ex: Exception =>
          Result
            .failure
            .log(
              s"""throwing IllegalArgumentException was expected but got ${ex.getClass.getTypeName} instead
               |""".stripMargin
            )
      }
    }

  def testToJava: Property =
    for {
      dayOfWeekPair <- Gen
                         .element1(
                           JDayOfWeek.MONDAY    -> DayOfWeek.monday,
                           JDayOfWeek.TUESDAY   -> DayOfWeek.tuesday,
                           JDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday,
                           JDayOfWeek.THURSDAY  -> DayOfWeek.thursday,
                           JDayOfWeek.FRIDAY    -> DayOfWeek.friday,
                           JDayOfWeek.SATURDAY  -> DayOfWeek.saturday,
                           JDayOfWeek.SUNDAY    -> DayOfWeek.sunday
                         )
                         .log("dayOfWeekPair")
    } yield {
      val (jDayOfWeek, dayOfWeek) = dayOfWeekPair

      val expected = jDayOfWeek
      val actual   = dayOfWeek.toJava
      (actual ==== expected)
        .log(
          s"""jDayOfWeek=$jDayOfWeek
             | dayOfWeek=$dayOfWeek
             |    actual=$actual
             |  expected=$expected
             |""".stripMargin
        )
    }

}
