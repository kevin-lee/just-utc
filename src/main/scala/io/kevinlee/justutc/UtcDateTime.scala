package io.kevinlee.justutc

import java.time.temporal.WeekFields
import java.time.{Clock, LocalDateTime}
import java.util.Locale

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
case class UtcDateTime(epochMillis: Long) extends Comparable[UtcDateTime] {

  lazy val jLocalDateTime: LocalDateTime = JDateTimeInUtc.toLocalDateTime(epochMillis)

  def dayOfWeek: Int = jLocalDateTime.getDayOfWeek.getValue

  def month: Int = jLocalDateTime.getMonth.getValue

  def dayOfMonth: Int = jLocalDateTime.getDayOfMonth

  def hour: Int = jLocalDateTime.getHour

  def minute: Int = jLocalDateTime.getMinute

  /**
    * Returns the week of a week based year using Locale.ROOT
    *
    * @return The week of a week based year using Locale.ROOT
    */
  def weekOfYear: Int = jLocalDateTime.get(WeekFields.of(Locale.ROOT).weekOfWeekBasedYear())

  override def compareTo(that: UtcDateTime): Int = this.epochMillis.compareTo(that.epochMillis)

  def <(that: UtcDateTime): Boolean = this.epochMillis < that.epochMillis
  def <=(that: UtcDateTime): Boolean = this.epochMillis <= that.epochMillis
  def >(that: UtcDateTime): Boolean = this.epochMillis > that.epochMillis
  def >=(that: UtcDateTime): Boolean = this.epochMillis >= that.epochMillis

  override lazy val toString: String = s"UtcDateTime(${jLocalDateTime.toString})"
}

object UtcDateTime {
  def now(): UtcDateTime = UtcDateTime(Clock.systemUTC().millis())

  def fromUtcLocalDateTime(localDateTime: LocalDateTime): UtcDateTime =
    UtcDateTime(JDateTimeInUtc.toEpochMilli(localDateTime))
}
