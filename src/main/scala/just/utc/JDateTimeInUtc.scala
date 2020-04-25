package just.utc

import java.time.temporal.UnsupportedTemporalTypeException
import java.time.zone.ZoneRulesException
import java.time.{DateTimeException, Instant, LocalDateTime, ZoneId, ZoneOffset}

import just.fp.syntax._

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
object JDateTimeInUtc extends CrossVersion {
  final val ZoneOffsetUtc: ZoneOffset = ZoneOffset.UTC
  final val ZoneIdUtc: ZoneId = ZoneOffsetUtc

  def toLocalDateTime(millis: Long): Either[DateTimeError, LocalDateTime] = for {
    instant <- try {
          Instant.ofEpochMilli(millis).right
        } catch {
          case dateTimeException: DateTimeException =>
            DateTimeError.epochMillisDateTime(
              millis
            , dateTimeException
            ).left
        }
    localDateTime <- try {
          LocalDateTime.ofInstant(instant, ZoneIdUtc).right
        } catch {
          case zoneRulesException: ZoneRulesException =>
            DateTimeError.zoneRules(ZoneIdUtc, zoneRulesException).left
          case dateTimeException: DateTimeException =>
            DateTimeError.instantDateTime(instant, dateTimeException).left
        }
  } yield localDateTime

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeToLocalDateTime(millis: Long): LocalDateTime =
    toLocalDateTime(millis) match {
      case Right(localDateTime) => localDateTime
      case Left(DateTimeError.EpochMillisDateTime(_, cause)) => throw cause
      case Left(DateTimeError.InstantDateTime(_, cause)) => throw cause
      case Left(DateTimeError.ArithmeticError(_, cause)) => throw cause
      case Left(DateTimeError.ZoneRules(_, cause)) => throw cause
      case Left(DateTimeError.UnsupportedTemporalType(_, cause)) => throw cause
      case Left(DateTimeError.LocalDateTimeError(_, cause)) => throw cause
    }

  def toEpochMilli(localDateTime: LocalDateTime): Either[DateTimeError, Long] = try {
    localDateTime.toInstant(ZoneOffsetUtc).toEpochMilli.right
  } catch {
    case zoneRulesException: ZoneRulesException =>
      DateTimeError.zoneRules(ZoneOffsetUtc, zoneRulesException).left
    case unsupportedTemporalTypeException: UnsupportedTemporalTypeException =>
      DateTimeError.unsupportedTemporalType(localDateTime, unsupportedTemporalTypeException).left
    case dateTimeException: DateTimeException =>
      DateTimeError.localDateTimeError(localDateTime, dateTimeException).left
    case arithmeticException: ArithmeticException =>
      DateTimeError.arithmeticError(
        s"""ArithmeticException when localDateTime.toInstant($ZoneOffsetUtc)
           | - localDateTime: $localDateTime
           |""".stripMargin
      , arithmeticException
      ).left
  }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeToEpochMilli(localDateTime: LocalDateTime): Long =
    toEpochMilli(localDateTime) match {
      case Right(localDateTime) => localDateTime
      case Left(DateTimeError.EpochMillisDateTime(_, cause)) => throw cause
      case Left(DateTimeError.InstantDateTime(_, cause)) => throw cause
      case Left(DateTimeError.ArithmeticError(_, cause)) => throw cause
      case Left(DateTimeError.ZoneRules(_, cause)) => throw cause
      case Left(DateTimeError.UnsupportedTemporalType(_, cause)) => throw cause
      case Left(DateTimeError.LocalDateTimeError(_, cause)) => throw cause
    }

}
