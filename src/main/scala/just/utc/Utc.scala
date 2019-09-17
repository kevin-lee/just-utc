package just.utc

import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.time.{Clock, LocalDateTime}
import java.util.Locale

import scala.math.Ordered

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
final case class Utc(epochMillis: Long) extends Ordered[Utc] {

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

  def compare(that: Utc): Int = this.epochMillis.compareTo(that.epochMillis)

  lazy val render: String = jLocalDateTime.toString

  def renderWith(dateTimeFormatter: DateTimeFormatter): String =
    jLocalDateTime.format(dateTimeFormatter)

}

object Utc {

  implicit object UtcDateTimeOrdering extends Ordering[Utc] {
    override def compare(x: Utc, y: Utc): Int = x.compare(y)
  }

  def now(): Utc = Utc(Clock.systemUTC().millis())

  def fromUtcLocalDateTime(localDateTime: LocalDateTime): Utc =
    Utc(JDateTimeInUtc.toEpochMilli(localDateTime))
}
