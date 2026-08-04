package com.vibhu.mcpclient.util;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpProgress;
import org.springframework.stereotype.Component;

@Component
public class HelpDeskToolProgressListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskToolProgressListener.class);

    @McpProgress(clients = "vibhu")
    public void onToolProgress(McpSchema.ProgressNotification progressNotification) {
        LOGGER.info("Received update - {}% complete received for Request ID {}: Message: {}",
                progressNotification.progress(),
                progressNotification.progressToken(),
                progressNotification.message());
    }

}
