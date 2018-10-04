package io.kevinlee.justutc

import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

/**
  * @author Kevin Lee
  * @since 2018-09-12
  */
object JDateTimeInUtc {
  val zoneId: ZoneId = ZoneId.ofOffset("UTC", ZoneOffset.UTC)

  def toLocalDateTime(millis: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId)

  def toEpochMilli(localDateTime: LocalDateTime): Long =
    localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli
}
