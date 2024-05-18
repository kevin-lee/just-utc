package just.utc

import just.fp.syntax._
import just.utc.JDateTimeInUtc.ZoneIdUtc

import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoField, WeekFields}
import java.time.{Clock, DateTimeException, Instant, LocalDateTime}
import java.util.Locale

/** @author Kevin Lee
  * @since 2018-09-12
  */
final class Utc private (val instant: Instant) extends Ordered[Utc] {

  lazy val epochMillisWithNanos: Long = instant.toEpochMilli / 1000L * 1000000000L + instant.getNano

  lazy val epochMillis: Long = instant.toEpochMilli

  override lazy val hashCode: Int = epochMillisWithNanos.hashCode()

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  override def equals(that: Any): Boolean = that match {
    case thatUtc: Utc => this.instant == thatUtc.instant
    case _ => false
  }

  override def toString: String = s"Utc(${epochMillisWithNanos.toString})"

  lazy val jLocalDateTime: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneIdUtc)

  def dayOfWeekValue: Int = jLocalDateTime.getDayOfWeek.getValue

  def month: Int = jLocalDateTime.getMonth.getValue

  def dayOfMonth: Int = jLocalDateTime.getDayOfMonth

  def hour: Int = jLocalDateTime.getHour

  def minute: Int = jLocalDateTime.getMinute

  def seconds: Int = jLocalDateTime.getSecond

  def milliSeconds: Int = instant.get(ChronoField.MILLI_OF_SECOND)

  /** Returns the week of a week based year using Locale.ROOT
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

  def unapply(utc: Utc): Option[Instant] =
    Option(utc).map(_.instant)

  def now(): Utc = new Utc(Clock.systemUTC().instant())

  def fromInstant(instant: Instant): Utc = new Utc(instant)

  def fromEpochMillis(epochMillis: Long): Either[DateTimeError, Utc] =
    try {
      new Utc(Instant.ofEpochMilli(epochMillis)).right
    } catch {
      case dateTimeException: DateTimeException =>
        DateTimeError
          .epochMillisDateTime(
            epochMillis,
            dateTimeException
          )
          .left
    }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeFromEpochMillis(epochMillis: Long): Utc =
    fromEpochMillis(epochMillis) match {
      case Right(utc) => utc
      case Left(DateTimeError.EpochMillisDateTime(_, cause)) => throw cause
      case Left(DateTimeError.InstantDateTime(_, cause)) => throw cause
      case Left(DateTimeError.ArithmeticError(_, cause)) => throw cause
      case Left(DateTimeError.ZoneRules(_, cause)) => throw cause
      case Left(DateTimeError.UnsupportedTemporalType(_, cause)) => throw cause
      case Left(DateTimeError.LocalDateTimeError(_, cause)) => throw cause
    }

  def fromUtcLocalDateTime(localDateTime: LocalDateTime): Utc =
    new Utc(localDateTime.toInstant(JDateTimeInUtc.ZoneOffsetUtc))
}
