package com.vibhu.openai.service;

import com.vibhu.openai.entity.HelpDeskTicket;
import com.vibhu.openai.pojo.TicketRequest;
import com.vibhu.openai.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

    private final HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequest ticketRequest, String userName) {
        HelpDeskTicket helpDeskTicket = HelpDeskTicket.builder()
                .userName(userName)
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
