package com.vibhu.openai.Tools;

import com.vibhu.openai.entity.HelpDeskTicket;
import com.vibhu.openai.pojo.TicketRequest;
import com.vibhu.openai.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {
    private final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService helpDeskTicketService;

    //@Tool(name = "createTicket", description = "Create a new help desk support ticket", returnDirect = true) Here LLM will direct give the tool output
    @Tool(name = "createTicket", description = "Create a new help desk support ticket")
    public String createHelpDeskTicket(@ToolParam(description = "Details to create a support ticket")
                                           TicketRequest ticketRequest, ToolContext toolContext) {
        String userName = toolContext.getContext().get("username").toString();
        LOGGER.debug("Creating a new help desk ticket for user: {}", userName);
        HelpDeskTicket createdTicket = helpDeskTicketService.createTicket(ticketRequest, userName);
        return "Help desk ticket created successfully with ID: " + createdTicket.getId() + " for user: " + userName;
    }

    @Tool(description = "Get the status of help desk support tickets for the given username")
    public List<HelpDeskTicket> getTicketStatus(ToolContext toolContext) {
        String userName = toolContext.getContext().get("username").toString();
        LOGGER.debug("Getting help desk ticket for user: {}", userName);
        return helpDeskTicketService.findTicketsByUserName(userName);
    }

}
