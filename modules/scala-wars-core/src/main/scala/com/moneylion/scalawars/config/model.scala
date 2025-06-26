package com.moneylion.scalawars.config

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.given

import com.moneylion.scalawars.util.OpaqueNewtypeWrapped
import com.moneylion.scalawars.util.WithConfigDecoder

import cats.syntax.option.given
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port

type TotalRetries = TotalRetries.Type

object TotalRetries extends OpaqueNewtypeWrapped[Int] with WithConfigDecoder[Int](5.some)

type BaseBackoffDuration = BaseBackoffDuration.Type

object BaseBackoffDuration
  extends OpaqueNewtypeWrapped[FiniteDuration]
  with WithConfigDecoder[FiniteDuration](100.milliseconds.some)

type MaxBackoffDuration = MaxBackoffDuration.Type

object MaxBackoffDuration
  extends OpaqueNewtypeWrapped[FiniteDuration]
  with WithConfigDecoder[FiniteDuration](30.seconds.some)

type PauseDuration = MaxBackoffDuration.Type

object PauseDuration
  extends OpaqueNewtypeWrapped[FiniteDuration]
  with WithConfigDecoder[FiniteDuration](100.milliseconds.some)

final case class HttpServerConfig(
  host: Host,
  port: Port
)

final case class RetryConfig(
  totalRetries: TotalRetries,
  baseBackoffDuration: BaseBackoffDuration,
  maxBackoffDuration: MaxBackoffDuration
)

final case class AppConfig(
  httpServer: HttpServerConfig,
  retryConfig: RetryConfig,
  pauseDuration: PauseDuration
)
