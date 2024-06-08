package just.utc

import java.time.temporal.UnsupportedTemporalTypeException
import java.time.{DateTimeException, Instant, LocalDateTime, ZoneId}
import java.time.zone.ZoneRulesException

/** @author Kevin Lee
  * @since 2020-04-21
  */
sealed trait DateTimeError

object DateTimeError {

  final case class ZoneRules(zoneId: ZoneId, cause: ZoneRulesException) extends DateTimeError

  final case class EpochMillisDateTime(
    millis: Long,
    cause: DateTimeException
  ) extends DateTimeError

  final case class InstantDateTime(
    instant: Instant,
    cause: DateTimeException
  ) extends DateTimeError

  final case class ArithmeticError(message: String, cause: ArithmeticException) extends DateTimeError

  final case class LocalDateTimeError(
    localDateTime: LocalDateTime,
    cause: DateTimeException
  ) extends DateTimeError

  final case class UnsupportedTemporalType(
    localDateTime: LocalDateTime,
    cause: UnsupportedTemporalTypeException
  ) extends DateTimeError

  def zoneRules(zoneId: ZoneId, cause: ZoneRulesException): DateTimeError = ZoneRules(zoneId, cause)

  def epochMillisDateTime(
    millis: Long,
    cause: DateTimeException
  ): DateTimeError =
    EpochMillisDateTime(millis, cause)

  def instantDateTime(
    instant: Instant,
    cause: DateTimeException
  ): DateTimeError =
    InstantDateTime(instant, cause)

  def arithmeticError(message: String, cause: ArithmeticException): DateTimeError =
    ArithmeticError(message, cause)

  def localDateTimeError(
    localDateTime: LocalDateTime,
    cause: DateTimeException
  ): DateTimeError =
    LocalDateTimeError(localDateTime, cause)

  def unsupportedTemporalType(
    localDateTime: LocalDateTime,
    cause: UnsupportedTemporalTypeException
  ): DateTimeError =
    UnsupportedTemporalType(localDateTime, cause)

}
