package com.vibhu.mcpclient.util;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.McpConnectionInfo;
import org.springframework.ai.mcp.McpToolFilter;
import org.springframework.stereotype.Component;

@Component
public class McpServerToolFilter implements McpToolFilter {

    private static final Logger logger = LoggerFactory.getLogger(McpServerToolFilter.class);

    /**
     * Can be configured via properties or environment variables to specify blocked servers.
     * @Value("${mcp.tool-filter.blocked-servers}") List<String> blockedServers
     * @param mcpConnectionInfo the first input argument
     * @param tool the second input argument
     * @return
     */
    @Override
    public boolean test(McpConnectionInfo mcpConnectionInfo, McpSchema.Tool tool) {
        assert mcpConnectionInfo.initializeResult() != null;
        String serverName = mcpConnectionInfo.initializeResult().serverInfo().name();
        String toolName = tool.name();
        logger.info("Evaluating tool '{}' for MCP Server '{}'", toolName, serverName);

        if(serverName.toLowerCase().contains("github")){
            logger.warn("Tool '{}' is not allowed for MCP Server '{}'. Skipping this tool.", toolName, serverName);
            return false;
        }

        //Can be done using toolname as well
        /*if(toolName.toLowerCase().startsWith("write_")){
            logger.warn("Tool '{}' is not allowed for MCP Server '{}'. Skipping this tool.", toolName, serverName);
            return false;
        }*/

        return true;
    }
}
