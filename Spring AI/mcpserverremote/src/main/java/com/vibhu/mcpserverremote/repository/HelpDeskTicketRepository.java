package com.vibhu.mcpserverremote.repository;


import com.vibhu.mcpserverremote.entity.HelpDeskTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket,Long> {
    List<HelpDeskTicket> findByUserName(String userName);
}
