package just.utc.types

import java.time.{DayOfWeek => JDayOfWeek}

/** @author Kevin Lee
  * @since 2024-05-20
  */
sealed abstract class DayOfWeek(val value: Int)
object DayOfWeek {
  case object Monday extends DayOfWeek(1)
  case object Tuesday extends DayOfWeek(2)
  case object Wednesday extends DayOfWeek(3)
  case object Thursday extends DayOfWeek(4)
  case object Friday extends DayOfWeek(5)
  case object Saturday extends DayOfWeek(6)
  case object Sunday extends DayOfWeek(7)

  def monday: DayOfWeek    = Monday
  def tuesday: DayOfWeek   = Tuesday
  def wednesday: DayOfWeek = Wednesday
  def thursday: DayOfWeek  = Thursday
  def friday: DayOfWeek    = Friday
  def saturday: DayOfWeek  = Saturday
  def sunday: DayOfWeek    = Sunday

  def fromJava(jDayOfWeek: JDayOfWeek): DayOfWeek =
    jDayOfWeek match {
      case JDayOfWeek.MONDAY => Monday
      case JDayOfWeek.TUESDAY => Tuesday
      case JDayOfWeek.WEDNESDAY => Wednesday
      case JDayOfWeek.THURSDAY => Thursday
      case JDayOfWeek.FRIDAY => Friday
      case JDayOfWeek.SATURDAY => Saturday
      case JDayOfWeek.SUNDAY => Sunday
    }

  def fromDayOfWeekInt(dayOfWeekInt: Int): Option[DayOfWeek] = dayOfWeekInt match {
    case 1 => Some(monday)
    case 2 => Some(tuesday)
    case 3 => Some(wednesday)
    case 4 => Some(thursday)
    case 5 => Some(friday)
    case 6 => Some(saturday)
    case 7 => Some(sunday)
    case _ => None
  }

  def unsafeFromDayOfWeekInt(dayOfWeekInt: Int): DayOfWeek =
    fromDayOfWeekInt(dayOfWeekInt).getOrElse(
      throw new IllegalArgumentException(
        "Invalid day of week Int. It should be an Int value between 1 to 7 (1=Monday, 7=Sunday)"
      )
    )

  implicit final class DayOfWeekOps(private val dayOfWeek: DayOfWeek) extends AnyVal {

    def toJava: JDayOfWeek =
      dayOfWeek match {
        case Monday => JDayOfWeek.MONDAY
        case Tuesday => JDayOfWeek.TUESDAY
        case Wednesday => JDayOfWeek.WEDNESDAY
        case Thursday => JDayOfWeek.THURSDAY
        case Friday => JDayOfWeek.FRIDAY
        case Saturday => JDayOfWeek.SATURDAY
        case Sunday => JDayOfWeek.SUNDAY
      }

  }
}
