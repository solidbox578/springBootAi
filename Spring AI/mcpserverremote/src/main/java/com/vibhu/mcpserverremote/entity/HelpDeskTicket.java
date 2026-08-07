package com.vibhu.mcpserverremote.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelpDeskTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;

    private String issueDescription;

    private String status;

    private String priority;

    private String contactPhone;

    private LocalDateTime createdAt;

    private LocalDateTime eta;

}
