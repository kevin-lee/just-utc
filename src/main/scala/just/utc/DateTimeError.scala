package just.utc

import java.time.DateTimeException

/**
  * @author Kevin Lee
  * @since 2020-04-21
  */
sealed trait DateTimeError

object DateTimeError {
  final case class ExceededInstantRange(
    millis: Long
  , min: Long
  , max: Long
  , cause: DateTimeException
  ) extends DateTimeError

  final case class ExceededLocalDateTimeRange(millis: Long, cause: DateTimeException) extends DateTimeError

  def exceededInstantRange(
    millis: Long
  , min: Long
  , max: Long
  , cause: DateTimeException
  ): DateTimeError =
    ExceededInstantRange(millis, min, max, cause)

  def exceededLocalDateTimeRange(millis: Long, cause: DateTimeException): DateTimeError =
    ExceededLocalDateTimeRange(millis, cause)

}
