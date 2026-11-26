package com.trifler.chat_application_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "conversation_id", nullable = false)
	    private Conversation conversation;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "sender_id", nullable = false)
	    private User sender;

	    @Column(nullable = false, columnDefinition = "TEXT")
	    private String content;

	    @Column(name = "created_at", nullable = false, updatable = false)
	    private LocalDateTime createdAt;

	    @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	    }

}
