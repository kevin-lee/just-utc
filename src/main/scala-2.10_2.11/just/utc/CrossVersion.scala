package just.utc

import just.fp.syntax.EitherOps.RightBiasedEither

trait CrossVersion {
  import scala.language.implicitConversions

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitConversion"))
  implicit def rightBiasedEither[A, B](e: Either[A, B]): RightBiasedEither[A, B] =
    just.fp.syntax.rightBiasedEither[A, B](e)
}
