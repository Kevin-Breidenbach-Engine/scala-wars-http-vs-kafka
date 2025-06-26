package com.moneylion.scalawars.kafkaservice.config

import com.moneylion.scalawars.config.KafkaConsumerConfig
import com.moneylion.scalawars.config.KafkaProducerConfig
import com.moneylion.scalawars.config.KafkaTopicConfig
import com.moneylion.scalawars.config.PauseDuration
import com.moneylion.scalawars.config.RetryConfig

final case class AppConfig(
  kafkaConsumerConfig: KafkaConsumerConfig,
  kafkaProducerConfig: KafkaProducerConfig,
  kafkaTopicConfig: KafkaTopicConfig,
  retryConfig: RetryConfig,
  pauseDuration: PauseDuration
)
