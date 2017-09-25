package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ipn.Message;

public class IPNProducer {
	private KafkaProducer<String, String> producer;
	private final String host;
	private final String id;


	public IPNProducer(String id, String host, String groupId) {
		this.id = id;
		this.host = host;
		// 
		Properties props = new Properties();
		props.put("client.id", this.id);
		props.put("producer.type", "async");
		props.put("bootstrap.servers", this.host);
		props.put("group.id", groupId);
		
		props.put("acks", "all");

		// If the request fails, the producer can automatically retry,
		props.put("retries", 0);

		// Specify buffer size in config
		props.put("batch.size", 16384);

		// Reduce the no of requests less than 0
		props.put("linger.ms", 1);

		// The buffer.memory controls the total amount of memory available to the
		// producer for buffering.
		props.put("buffer.memory", 33554432);

		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.producer = new KafkaProducer<>(props);
	}

	// send the created message
	public void sendAlert(String topic, String key, Message<?> message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(message);
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, json);
			producer.send(record, new Callback() {

				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					// exception only filled when an error occurs
					if (exception != null) {
						// in case of error decide what to do
					}

				}
			});
		} catch (JsonProcessingException e) {
			// TODO: decide what to do
		}
	}

	public void shutdown() {
		producer.close();
	}
}