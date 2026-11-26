package com.trifler.chat_application_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trifler.chat_application_backend.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	 List<Message> findByConversation_IdOrderByCreatedAtAsc(
	            Long conversationId
	    );

}
