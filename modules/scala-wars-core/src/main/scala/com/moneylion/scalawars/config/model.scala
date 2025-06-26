package com.moneylion.scalawars.config

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.given

import com.moneylion.scalawars.util.OpaqueNewtypeWrapped
import com.moneylion.scalawars.util.WithConfigDecoder

import cats.syntax.option.given
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import fs2.kafka.AutoOffsetReset

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

type PauseDuration = PauseDuration.Type

object PauseDuration
  extends OpaqueNewtypeWrapped[FiniteDuration]
  with WithConfigDecoder[FiniteDuration](100.milliseconds.some)

type KafkaBootstrapServers = KafkaBootstrapServers.Type

object KafkaBootstrapServers extends OpaqueNewtypeWrapped[String] with WithConfigDecoder[String]("127.0.0.1:9091".some)

type KafkaConsumerGroup = KafkaConsumerGroup.Type

object KafkaConsumerGroup extends OpaqueNewtypeWrapped[String] with WithConfigDecoder[String]("identity-service".some)

type KafkaConsumerMaxPollRecords = KafkaConsumerMaxPollRecords.Type

object KafkaConsumerMaxPollRecords extends OpaqueNewtypeWrapped[Int] with WithConfigDecoder[Int](50.some)

type KafkaProducerBatchSize = KafkaProducerBatchSize.Type

object KafkaProducerBatchSize extends OpaqueNewtypeWrapped[Int] with WithConfigDecoder[Int](50.some)

type KafkaProducerLinger = KafkaProducerLinger.Type

object KafkaProducerLinger
  extends OpaqueNewtypeWrapped[FiniteDuration]
  with WithConfigDecoder[FiniteDuration](0.seconds.some)

type RequestTopic = RequestTopic.Type

object RequestTopic extends OpaqueNewtypeWrapped[String] with WithConfigDecoder[String]("request".some)

type ResponseTopic = ResponseTopic.Type

object ResponseTopic extends OpaqueNewtypeWrapped[String] with WithConfigDecoder[String]("response".some)

final case class HttpServerConfig(
  host: Host,
  port: Port
)

final case class RetryConfig(
  totalRetries: TotalRetries,
  baseBackoffDuration: BaseBackoffDuration,
  maxBackoffDuration: MaxBackoffDuration
)

final case class KafkaProducerConfig(
  bootstrapServers: KafkaBootstrapServers,
  batchSize: KafkaProducerBatchSize,
  linger: KafkaProducerLinger
)

final case class KafkaConsumerConfig(
  bootstrapServers: KafkaBootstrapServers,
  groupId: KafkaConsumerGroup,
  maxPollRecords: KafkaConsumerMaxPollRecords,
  autoOffsetReset: AutoOffsetReset
)

final case class KafkaTopicConfig(
  requestTopic: RequestTopic,
  responseTopic: ResponseTopic
)

final case class ConfigError(message: String) extends Exception(message)
