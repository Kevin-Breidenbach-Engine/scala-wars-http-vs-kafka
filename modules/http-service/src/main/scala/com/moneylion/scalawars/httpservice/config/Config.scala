package com.moneylion.scalawars.httpservice.config

import com.moneylion.scalawars.config.BaseBackoffDuration
import com.moneylion.scalawars.config.ConfigError
import com.moneylion.scalawars.config.HttpServerConfig
import com.moneylion.scalawars.config.MaxBackoffDuration
import com.moneylion.scalawars.config.PauseDuration
import com.moneylion.scalawars.config.RetryConfig
import com.moneylion.scalawars.config.TotalRetries

import cats.syntax.all.given
import ciris.ConfigDecoder
import ciris.ConfigValue
import ciris.env
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import com.comcast.ip4s.ip
import com.comcast.ip4s.port

object Config {
  def make[F[_]]: Config[F] = new Config[F]()
}

class Config[F[_]] {
  given ConfigDecoder[String, Host] =
    ConfigDecoder[String, String].map(Host.fromString).map {
      case None       => throw ConfigError("Invalid host")
      case Some(host) => host
    }

  given ConfigDecoder[String, Port] =
    ConfigDecoder[String, Int].map(Port.fromInt).map {
      case None       => throw ConfigError("Invalid port")
      case Some(port) => port
    }

  private val httpServerConfig: ConfigValue[F, HttpServerConfig] = (
    env("HTTP_SERVER_HOST").as[Host].default(ip"0.0.0.0"),
    env("HTTP_SERVER_PORT").as[Port].default(port"8080")
  ).mapN(HttpServerConfig.apply)

  private val retryConfig: ConfigValue[F, RetryConfig] = (
    env("RETRY_TOTAL").as[TotalRetries].default(TotalRetries.default),
    env("RETRY_BASE_BACKOFF_DURATION").as[BaseBackoffDuration].default(BaseBackoffDuration.default),
    env("RETRY_MAX_BACKOFF_DURATION").as[MaxBackoffDuration].default(MaxBackoffDuration.default)
  ).mapN(RetryConfig.apply)

  private val pauseDuration: ConfigValue[F, PauseDuration] =
    env("PAUSE_DURATION").as[PauseDuration].default(PauseDuration.default)

  val appConfig: ConfigValue[F, AppConfig] =
    (
      httpServerConfig,
      retryConfig,
      pauseDuration
    ).mapN(
      AppConfig.apply
    )
}
