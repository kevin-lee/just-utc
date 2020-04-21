package just.utc

import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.time.{Clock, DateTimeException, Instant, LocalDateTime}
import java.util.Locale

import just.utc.JDateTimeInUtc.ZoneIdUtc

import scala.math.Ordered

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
final class Utc private (val instant: Instant) extends Ordered[Utc] {

  lazy val epochMillis: Long = instant.toEpochMilli

  override lazy val hashCode: Int = epochMillis.hashCode()

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  override def equals(that: Any): Boolean = that match {
    case thatUtc: Utc => this.epochMillis == thatUtc.epochMillis
    case _ => false
  }

  @SuppressWarnings(Array("org.wartremover.warts.StringPlusAny"))
  override def toString: String = s"Utc($epochMillis)"

  lazy val jLocalDateTime: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneIdUtc)

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

  def unapply(utc: Utc): Option[Instant] =
    Option(utc).map(_.instant)

  def now(): Utc = new Utc(Instant.ofEpochMilli(Clock.systemUTC().millis()))

  def fromEpochMillis(epochMillis: Long): Either[DateTimeError, Utc] =
    try {
      Right(new Utc(Instant.ofEpochMilli(epochMillis)))
    } catch {
      case dateTimeException: DateTimeException =>
        Left(
          DateTimeError.exceededInstantRange(
            epochMillis
            , Instant.MIN.getEpochSecond
            , Instant.MAX.getEpochSecond
            , dateTimeException)
        )
    }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeFromEpochMillis(epochMillis: Long): Utc =
    fromEpochMillis(epochMillis) match {
      case Right(utc) => utc
      case Left(DateTimeError.ExceededInstantRange(_, _, _, cause)) => throw cause
      case Left(DateTimeError.ExceededLocalDateTimeRange(_, cause)) => throw cause

    }


  def fromUtcLocalDateTime(localDateTime: LocalDateTime): Utc =
    new Utc(localDateTime.toInstant(JDateTimeInUtc.ZoneOffsetUtc))
}
