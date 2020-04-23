package just.utc

import java.time.{DateTimeException, Instant, LocalDateTime, ZoneId, ZoneOffset}

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
object JDateTimeInUtc extends CrossVersion {
  final val ZoneOffsetUtc: ZoneOffset = ZoneOffset.UTC
  final val ZoneIdUtc: ZoneId = ZoneOffsetUtc

  def toLocalDateTime(millis: Long): Either[DateTimeError, LocalDateTime] = for {
    instant <- try {
          Right(Instant.ofEpochMilli(millis))
        } catch {
          case dateTimeException: DateTimeException =>
            Left(
              DateTimeError.exceededInstantRange(
                millis
              , Instant.MIN.getEpochSecond
              , Instant.MAX.getEpochSecond
              , dateTimeException)
            )
        }
    localDateTime <- try {
          Right(LocalDateTime.ofInstant(instant, ZoneIdUtc))
        } catch {
          case dateTimeException: DateTimeException =>
          Left(DateTimeError.exceededLocalDateTimeRange(millis, dateTimeException))
        }
  } yield localDateTime

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeToLocalDateTime(millis: Long): LocalDateTime =
    toLocalDateTime(millis) match {
      case Right(localDateTime) => localDateTime
      case Left(DateTimeError.ExceededInstantRange(_, _, _, cause)) => throw cause
      case Left(DateTimeError.ExceededLocalDateTimeRange(_, cause)) => throw cause
    }

  def toEpochMilli(localDateTime: LocalDateTime): Long =
    localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli
}
