package just.utc

import java.time.{DateTimeException, Instant, ZoneId}
import java.time.zone.ZoneRulesException

/**
  * @author Kevin Lee
  * @since 2020-04-21
  */
sealed trait DateTimeError

object DateTimeError {

  final case class ZoneRules(zoneId: ZoneId, cause: ZoneRulesException) extends DateTimeError

  final case class EpochMillisDateTime(
    millis: Long
  , cause: DateTimeException
  ) extends DateTimeError

  final case class InstantDateTime(
    instant: Instant
  , cause: DateTimeException
  ) extends DateTimeError

  final case class ArithmeticError(message: String, cause: ArithmeticException) extends DateTimeError

  final case class ExceededLocalDateTimeRange(millis: Long, cause: DateTimeException) extends DateTimeError


  def zoneRules(zoneId: ZoneId, cause: ZoneRulesException): DateTimeError = ZoneRules(zoneId, cause)

  def epochMillisDateTime(
    millis: Long
  , cause: DateTimeException
  ): DateTimeError =
    EpochMillisDateTime(millis, cause)

  def instantDateTime(
    instant: Instant
  , cause: DateTimeException
  ): DateTimeError =
    InstantDateTime(instant, cause)

  def arithmeticError(message: String, cause: ArithmeticException): DateTimeError = ArithmeticError(message, cause)

  def exceededLocalDateTimeRange(millis: Long, cause: DateTimeException): DateTimeError =
    ExceededLocalDateTimeRange(millis, cause)


}
