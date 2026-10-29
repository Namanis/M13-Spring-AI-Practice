package com.accenture.springai_bootcamp_demo.client;

import com.accenture.springai_bootcamp_demo.config.OpenRouterProperties;
import com.accenture.springai_bootcamp_demo.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Thin client over the OpenRouter chat completions API, backed by Spring AI's
 * {@link ChatClient}. Keeps the public surface intentionally small: callers
 * hand over the conversation history and receive the assistant's reply text.
 */
@Slf4j
@Component
public class OpenRouterClient {
    //TODO 4: define and initialize a ChatClient
    private final ChatClient chatClient;

    public OpenRouterClient(ChatClient.Builder chatClientBuilder, OpenRouterProperties openRouterProperties) {
        this.chatClient = chatClientBuilder
                .defaultSystem(openRouterProperties.systemPrompt())
                .build();
    }

    public String complete(List<ChatMessage> history) {
        String reply = call(history);

        return extractContent(reply);
    }

    private String call(List<ChatMessage> history) {
        try {
            return chatClient.prompt()
                    .messages(toSpringMessages(history))
                    .call()
                    .content();
            //TODO 5: make a call through ChatClient with the conversation history (including latest user message)
        } catch (OpenRouterException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("OpenRouter request failed", ex);
            throw new OpenRouterException("Failed to reach OpenRouter: " + ex.getMessage(), ex);
        }
    }

    //TODO 6: use helper methods/mappers/utility classes to construct the payload for a cleaner solution
    private List<Message> toSpringMessages(List<ChatMessage> history) {
        List<Message> messages = new ArrayList<>();
        for (ChatMessage message : history) {
            messages.add(toSpringMessages(message));
        }

        return messages;
    }

    private Message toSpringMessages(ChatMessage message) {
        String content = message.getContent();
        return switch (message.getRole()) {
            case USER -> new UserMessage(content);
            case ASSISTANT ->  new AssistantMessage(content);
            case SYSTEM ->  new SystemMessage(content);
        };
    }


    private String extractContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new OpenRouterException("OpenRouter returned an empty response");
        }
        return content.trim();
    }
}
