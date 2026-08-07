package com.vibhu.mcpserverremote.tool;


import com.vibhu.mcpserverremote.entity.HelpDeskTicket;
import com.vibhu.mcpserverremote.pojo.TicketContactInfo;
import com.vibhu.mcpserverremote.pojo.TicketRequest;
import com.vibhu.mcpserverremote.service.HelpDeskTicketService;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.ai.mcp.annotation.context.StructuredElicitResult;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {
    private final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

    private static  final String DEFAULT_PRIORITY = "MEDIUM";
    private static  final String DEFAULT_CONTACT_PHONE = "+N/A";

    private final HelpDeskTicketService helpDeskTicketService;

    /**
     * returnDirect = true
     * means that the LLM will directly return the output of this tool without any additional formatting or processing.
     * This is useful when you want the LLM to provide a concise response based on the tool's output.
     * @McpTool(name = "createTicket", description = "Create a new help desk support ticket", returnDirect = true)
     */

    /**
     *
     * @param ticketRequest
     * @return
     */
    @McpTool(name = "createTicket", description = "Create a new help desk support ticket")
    public String createHelpDeskTicket(@McpToolParam(description = "Details to create a support ticket")
                                       TicketRequest ticketRequest, McpSyncRequestContext ctx) {
        LOGGER.debug("Creating a new help desk ticket with detail: {}", ticketRequest);
        String priority = DEFAULT_PRIORITY;
        String contactPhone = DEFAULT_CONTACT_PHONE;

        if(ctx.elicitEnabled()){
            ctx.info("Asking you for a few extra details before opening the tickets..");
            LOGGER.info("Requesting user for additional details for ticket creation from the MCP client via elicitation..");

            StructuredElicitResult<TicketContactInfo> elicitResult = ctx.elicit(
                    spec -> spec.message("Before we open your support ticket, please choose a priority : " +
                            "(LOW, MEDIUM, HIGH OR URGENT) and share a contact number so our team can reach you."),
                    TicketContactInfo.class);

            LOGGER.info("Elicitation finished with action: {}", elicitResult.action());

            if(elicitResult.action() == McpSchema.ElicitResult.Action.ACCEPT && elicitResult.structuredContent() != null){
                TicketContactInfo contactInfo = elicitResult.structuredContent();
                if(contactInfo.priority() != null && !contactInfo.priority().isBlank()) {
                    priority = contactInfo.priority();
                }
                if(contactInfo.contactPhone() != null && !contactInfo.contactPhone().isBlank()) {
                    contactPhone = contactInfo.contactPhone();
                }
                LOGGER.info("User provided additional details for ticket creation: Priority - {}, Contact Phone - {}", priority, contactPhone);
            } else {
                ctx.warn("No Extra details were provided for elicitation. Using default priority '"+priority+"' for ticket creation.");
                LOGGER.warn("User did not provide additional details for ticket creation. Using default values.");
            }
        } else {
            ctx.warn("Connected MCP client does not support elicitation. Using default priority '"+DEFAULT_PRIORITY+"'.");
        }
        HelpDeskTicket createdTicket = helpDeskTicketService.createTicket(ticketRequest, priority, contactPhone);
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

    /**
     * EXAMPLE OF SAMPLING:  MCP server sending request back to client to get data from LLM present there.
     * This is a good example of how the MCP server can leverage the LLM capabilities of the client to generate a summary of the tickets.
     * @param username
     * @param ctx
     * @return
     */

    @McpTool(name ="summerizeTickets", description = "Generate a friendly, natural-language summary of all the support tickets that belong to a given username")
    public String summerizeTickets(@McpToolParam(description = "Username to summerize the help desk tickets for") String username,
                            McpSyncRequestContext ctx) {
        LOGGER.debug("Generating summary for help desk tickets for user: {}", username);
        List<HelpDeskTicket> ticketsByUserName = helpDeskTicketService.findTicketsByUserName(username);

        if(ticketsByUserName.isEmpty()){
            return "No Support tickets found for user: "+username;
        }

        if(!ctx.sampleEnabled()){
            LOGGER.warn("Connected MCP Client does not support Sampling. Returning the Raw ticket date instead");
            return ticketsByUserName.toString();
        }

        String ticketData = ticketsByUserName.stream()
                .map(ticket -> "Ticket ID: " + ticket.getId() + ", Status: " + ticket.getStatus() + ", " +
                        "Issue description: " + ticket.getIssueDescription() + "ETA: " + ticket.getEta())
                .collect(java.util.stream.Collectors.joining("\n"));

        String systemPrompt = """
                            You are a friendly help desk assistant. Using ONLY the ticket data provided by the user, write a short,
                            warm summary for the customer about the status of their support tickets.
                            Mention how many tickets they have in total, group them by status(OPEN, IN_PROGRESS, CLOSED)
                            and reassure them about the ones that are still being worked on. Keep it under 120 words and do not invent any information
                            that is not present in the ticket data.
                           """;
        LOGGER.info("Requesting LLM completion from the MCP Client via sampling....");
        ctx.info("Asking your AI assistant to summerize "+ ticketsByUserName.size()+" tickets for user: "+username);
        McpSchema.CreateMessageResult result = ctx.sample(spec -> spec
                .systemPrompt(systemPrompt)
                .message("Here are the support tickets for "+username+" :\n "+ticketData));

        String summary =((McpSchema.TextContent) result.content()).text();

        LOGGER.info("Sampling Response Received. \n {}  \n Model Used by client : {}", summary, result.model());
        return summary;
    }

}
