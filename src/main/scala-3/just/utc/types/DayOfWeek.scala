package just.utc.types

import java.time.{DayOfWeek => JDayOfWeek}

/** @author Kevin Lee
  * @since 2024-05-20
  */
enum DayOfWeek(val value: Int) derives CanEqual {
  case Monday extends DayOfWeek(1)
  case Tuesday extends DayOfWeek(2)
  case Wednesday extends DayOfWeek(3)
  case Thursday extends DayOfWeek(4)
  case Friday extends DayOfWeek(5)
  case Saturday extends DayOfWeek(6)
  case Sunday extends DayOfWeek(7)
}
object DayOfWeek {

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

  extension (dayOfWeek: DayOfWeek) {
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
