package com.moneylion.scalawars.httpservice.config

import com.moneylion.scalawars.config.HttpServerConfig
import com.moneylion.scalawars.config.PauseDuration
import com.moneylion.scalawars.config.RetryConfig

final case class AppConfig(
  httpServer: HttpServerConfig,
  retryConfig: RetryConfig,
  pauseDuration: PauseDuration
)
