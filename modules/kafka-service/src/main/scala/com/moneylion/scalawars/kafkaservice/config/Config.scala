package com.moneylion.scalawars.kafkaservice.config

import com.moneylion.scalawars.config.*
import com.moneylion.scalawars.config.ResponseTopic

import cats.syntax.all.given
import ciris.ConfigDecoder
import ciris.ConfigValue
import ciris.env
import fs2.kafka.AutoOffsetReset

object Config {
  def make[F[_]]: Config[F] = new Config[F]()
}

class Config[F[_]] {
  given ConfigDecoder[String, AutoOffsetReset] =
    ConfigDecoder[String, String].map(string =>
      string.toLowerCase match {
        case "none"     => AutoOffsetReset.None
        case "earliest" => AutoOffsetReset.Earliest
        case "latest"   => AutoOffsetReset.Latest
        case _          => throw ConfigError(s"$string is an invalid AutoOffsetReset value")
      }
    )

  private val kafkaConsumerConfig: ConfigValue[F, KafkaConsumerConfig] = (
    env("KAFKA_BOOTSTRAP_SERVERS").as[KafkaBootstrapServers].default(KafkaBootstrapServers.default),
    env("KAFKA_CONSUMER_GROUP").as[KafkaConsumerGroup].default(KafkaConsumerGroup.default),
    env("KAFKA_MAX_POLL_RECORDS").as[KafkaConsumerMaxPollRecords].default(KafkaConsumerMaxPollRecords.default),
    env("KAFKA_CONSUMER_AUTO_OFFSET_RESET").as[AutoOffsetReset].default(AutoOffsetReset.None)
  ).mapN(KafkaConsumerConfig.apply)

  private val kafkaProducerConfig: ConfigValue[F, KafkaProducerConfig] = (
    env("KAFKA_BOOTSTRAP_SERVERS").as[KafkaBootstrapServers].default(KafkaBootstrapServers.default),
    env("KAFKA_EWS_PRODUCER_BATCH_SIZE")
      .as[KafkaProducerBatchSize]
      .default(KafkaProducerBatchSize.default),
    env("KAFKA_PROFILE_PRODUCER_LINGER").as[KafkaProducerLinger].default(KafkaProducerLinger.default)
  ).mapN(KafkaProducerConfig.apply)

  private val kafkaTopicConfig: ConfigValue[F, KafkaTopicConfig] = (
    env("KAFKA_REQUEST_TOPIC")
      .as[RequestTopic]
      .default(RequestTopic.default),
    env("KAFKA_RESPONSE_TOPIC")
      .as[ResponseTopic]
      .default(ResponseTopic.default)
  ).mapN(KafkaTopicConfig.apply)

  private val retryConfig: ConfigValue[F, RetryConfig] = (
    env("RETRY_TOTAL").as[TotalRetries].default(TotalRetries.default),
    env("RETRY_BASE_BACKOFF_DURATION").as[BaseBackoffDuration].default(BaseBackoffDuration.default),
    env("RETRY_MAX_BACKOFF_DURATION").as[MaxBackoffDuration].default(MaxBackoffDuration.default)
  ).mapN(RetryConfig.apply)

  private val pauseDuration: ConfigValue[F, PauseDuration] =
    env("PAUSE_DURATION").as[PauseDuration].default(PauseDuration.default)

  val appConfig: ConfigValue[F, AppConfig] =
    (
      kafkaConsumerConfig,
      kafkaProducerConfig,
      kafkaTopicConfig,
      retryConfig,
      pauseDuration
    ).mapN(
      AppConfig.apply
    )
}
