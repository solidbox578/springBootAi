package com.vibhu.mcpserverremote.tool;


import com.vibhu.mcpserverremote.entity.HelpDeskTicket;
import com.vibhu.mcpserverremote.pojo.TicketRequest;
import com.vibhu.mcpserverremote.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {
    private final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService helpDeskTicketService;

    //@Tool(name = "createTicket", description = "Create a new help desk support ticket", returnDirect = true) Here LLM will direct give the tool output
    @McpTool(name = "createTicket", description = "Create a new help desk support ticket")
    public String createHelpDeskTicket(@McpToolParam(description = "Details to create a support ticket")
                                       TicketRequest ticketRequest) {
        LOGGER.debug("Creating a new help desk ticket with detail: {}", ticketRequest);
        HelpDeskTicket createdTicket = helpDeskTicketService.createTicket(ticketRequest);
        return "Help desk ticket created successfully with ID: " + createdTicket.getId() + " for user: " + createdTicket.getUserName();
    }

    @McpTool(description = "Get the status of help desk support tickets for the given username")
    public List<HelpDeskTicket> getTicketStatus(@ToolParam(description =
            "Username to fetch the status of the help desk tickets") String username, McpSyncRequestContext ctx) throws InterruptedException {
        LOGGER.debug("Getting help desk ticket for user: {}", username);
        ctx.info("Fetching help desk tickets for user: "+ username);
        List<HelpDeskTicket> ticketsByUserName = helpDeskTicketService.findTicketsByUserName(username);
        ctx.info("Found "+ticketsByUserName.size()+" tickets for user: "+username);

        for(int i=0; i<10;i++){
            Thread.sleep(1000);
            int percentage = (i*100)/10;
            ctx.progress(spec -> spec.percentage(percentage).message("Processing ticket status for user: "+username));
        }

        return ticketsByUserName;
    }

}
