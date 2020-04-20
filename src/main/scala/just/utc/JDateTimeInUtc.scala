package just.utc

import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
object JDateTimeInUtc {
  final val ZoneOffsetUtc: ZoneOffset = ZoneOffset.UTC
  final val ZoneIdUtc: ZoneId = ZoneOffsetUtc

  def toLocalDateTime(millis: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneIdUtc)

  def toEpochMilli(localDateTime: LocalDateTime): Long =
    localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli
}
