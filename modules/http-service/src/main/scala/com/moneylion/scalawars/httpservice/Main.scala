package com.moneylion.scalawars.httpservice

import com.moneylion.scalawars.httpservice.config.Config

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.given

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Config.make[IO].appConfig.load.flatMap { _ =>
      ExitCode.Success.pure[IO]
    }
}
