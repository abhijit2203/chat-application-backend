package com.trifler.chat_application_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trifler.chat_application_backend.entity.ConversationMember;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long>{
	
	    List<ConversationMember> findByUser_Id(Long userId);

	    List<ConversationMember> findByConversation_Id(Long conversationId);

	    boolean existsByConversation_IdAndUser_Id(
	            Long conversationId, Long userId);

}
