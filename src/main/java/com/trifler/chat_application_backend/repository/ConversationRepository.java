package com.trifler.chat_application_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trifler.chat_application_backend.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>{

}
