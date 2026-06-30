package com.domenicopisano.mtgcollectionmanager.repository;

import com.domenicopisano.mtgcollectionmanager.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId, Pageable pageable);

    Page<Message> findBySenderIdOrderBySentAtDesc(Long senderId, Pageable pageable);

    List<Message> findByReceiverIdAndReadMessageFalse(Long receiverId);

    @Query("""
            select m
            from Message m
            where (m.sender.id = :userAId and m.receiver.id = :userBId)
               or (m.sender.id = :userBId and m.receiver.id = :userAId)
            order by m.sentAt asc
            """)
    List<Message> findConversation(
            @Param("userAId") Long userAId,
            @Param("userBId") Long userBId
    );
}