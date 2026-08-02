package com.vibhu.mcpserverstdio.service;


import com.vibhu.mcpserverstdio.entity.HelpDeskTicket;
import com.vibhu.mcpserverstdio.pojo.TicketRequest;
import com.vibhu.mcpserverstdio.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

    private final HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequest ticketRequest) {
        HelpDeskTicket helpDeskTicket = HelpDeskTicket.builder()
                .userName(ticketRequest.username())
                .issueDescription(ticketRequest.issueDescription())
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7)) // Assuming a default ETA of 7 days
                .build();
        return helpDeskTicketRepository.save(helpDeskTicket);
    }

    public List<HelpDeskTicket> findTicketsByUserName(String userName) {
        return helpDeskTicketRepository.findByUserName(userName);
    }
}
