package com.vibhu.openai.config;

import com.vibhu.openai.config.advisor.TokenUsageAuditAdvisor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.ai.chat.client.ChatClientBuilderCustomizer;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientBuilderCustomizerConfig {


    /**
     * Now These two Advisor would be available globally for all ChatClient instances. So, we don't need to add them in each ChatClient bean definition.
     * @return
     */

    @Bean
    public ChatClientBuilderCustomizer loggerCustomizer() {
        return chatClientBuilder -> chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor());
    }

    @Bean
    @ConditionalOnProperty(name="audit.token-usage.enabled", havingValue = "true")
    public ChatClientBuilderCustomizer tokenUsageAuditCustomizer() {
        return chatClientBuilder -> chatClientBuilder.defaultAdvisors(new TokenUsageAuditAdvisor());
    }

}
