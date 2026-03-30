package com.agshin.extapp.config;

import com.agshin.extapp.listener.UserEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // ─────────────────────────────────────────────────────────
    // BEAN 2: MessageListenerAdapter for user events
    //
    // This adapter wraps your UserEventLister POJO.
    // The second argument "handleMessage" is the METHOD NAME
    // Spring will call on that POJO when a message arrives.
    //
    // Spring uses reflection to call:
    //   userEventListener.handleMessage(String message, String channel)
    //
    // Why do we need this instead of calling our listener directly?
    // Because the raw Redis API delivers a Message object (bytes).
    // The adapter handles the deserialization from bytes to String
    // before handing it to your method. It's a translation layer.
    // ─────────────────────────────────────────────────────────
    @Bean
    public MessageListenerAdapter userListenerAdapter(UserEventListener listener) {
        // message that was published to redis will be passed to UserEventListener's handleMessage method
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "handleMessage");

        // Tell the adapter to deserialize bytes as UTF-8 strings.
        // Without this it uses Java serialization which produces garbage.
        adapter.setSerializer(new StringRedisSerializer());

        return adapter;
    }


    // ─────────────────────────────────────────────────────────
    // BEAN 3: RedisMessageListenerContainer
    //
    // This is the heart of the subscriber side. It:
    //   1. Opens a DEDICATED Redis connection (not from your pool)
    //   2. Calls SUBSCRIBE / PSUBSCRIBE on every topic you register
    //   3. Runs a background thread that blocks waiting for messages
    //   4. When a message arrives, routes it to the right adapter
    //
    // Think of it like Laravel's queue:work — it's a long-running
    // background process that just listens and dispatches.
    //
    // You register topics here by pairing:
    //   adapter (which POJO to call) + topic (which channel to watch)
    // ─────────────────────────────────────────────────────────

    @Bean
    public RedisMessageListenerContainer listenerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter userListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        // Give it the connection factory — it will create its own
        // dedicated connection from this. NOT the same connection
        // your StringRedisTemplate uses.
        container.setConnectionFactory(connectionFactory);

        // binding adapter to a specific channel
        // ChannelTopic wraps a channel name string (as opposed to PatternTopic for wildcards)
        container.addMessageListener(
                userListenerAdapter,
                new ChannelTopic("user.registered")
        );

        // One adapter can listen to multiple channels
        // container.addMessageListener(
        //     orderListenerAdapter,
        //     List.of(
        //         new ChannelTopic("orders:events"),
        //         new ChannelTopic("orders:returns")
        //     )
        // );

        return container;
    }
}
