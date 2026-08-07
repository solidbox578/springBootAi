package com.vibhu.mcpclient.util;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpElicitation;
import org.springframework.stereotype.Component;
import java.util.Map;


@Component
public class HelpDeskElicitationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskElicitationProvider.class);

    @McpElicitation(clients = "vibhu")
    public McpSchema.ElicitResult handleElicitRequest(McpSchema.ElicitRequest elicitRequest) {
        LOGGER.info("Received MCP elicit request from server. Message: {}", elicitRequest.message());

        //Here user can provide the required information. For demonstration, we are hardcoding the response.
        Map<String, Object> userResponse = Map.of(
                "priority","HIGH",
                "contactPhone", "+911234124434"
        );

        LOGGER.info("Responding to elicitation with ACCEPT and data : {}", userResponse);
        return McpSchema.ElicitResult
                .builder(McpSchema.ElicitResult.Action.ACCEPT)
                .content(userResponse)
                .build();
    }

}
