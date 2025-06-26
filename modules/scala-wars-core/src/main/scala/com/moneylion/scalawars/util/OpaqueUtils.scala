package com.moneylion.scalawars.util

import java.time.Instant
import java.time.LocalDate
import scala.concurrent.duration.FiniteDuration

import cats.Show
import ciris.ConfigDecoder
import monix.newtypes.NewtypeWrapped
import org.http4s.ParseFailure
import org.http4s.Uri

val stringToUri: String => Uri = s =>
  Uri.fromString(s) match {
    case Right(uri)                       => uri
    case Left(parseFailure: ParseFailure) => throw parseFailure
  }

given Show[Instant] = Show.show(_.toString)
given Show[LocalDate] = Show.show(_.toString)

trait OpaqueNewtypeWrapped[S: Show] extends NewtypeWrapped[S] {
  given Show[Type] = derive
}

given ConfigDecoder[String, String] = ConfigDecoder.identity
given ConfigDecoder[String, Int] = ConfigDecoder.stringIntConfigDecoder
given ConfigDecoder[String, Long] = ConfigDecoder.stringLongConfigDecoder
given ConfigDecoder[String, Boolean] = ConfigDecoder.stringBooleanConfigDecoder
given ConfigDecoder[String, FiniteDuration] = ConfigDecoder.stringFiniteDurationConfigDecoder
given ConfigDecoder[String, Uri] = ConfigDecoder.identity.map(stringToUri)

// throwing is ok here since it's only used for configuration, so halting the application is acceptable
trait WithConfigDecoder[S](defaultValue: Option[S] = None)(using decoder: ConfigDecoder[String, S]) {
  self: OpaqueNewtypeWrapped[S] =>
  lazy val default: Type =
    defaultValue.map(apply).getOrElse(throw new IllegalArgumentException("Default value must be provided"))

  given ConfigDecoder[String, Type] = derive

  given ConfigDecoder[String, Option[Type]] =
    ConfigDecoder.identity[String].mapOption("Option[Type]") { str =>
      if (str.trim.isEmpty) Some(None)
      else decoder.decode(None, str).map(apply).toOption.map(Some(_))
    }
}
