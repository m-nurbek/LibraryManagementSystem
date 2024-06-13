package nurbek.librarymanagementsystem.repository;

import nurbek.librarymanagementsystem.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query(value = "SELECT * FROM notification n WHERE n.id = :recipientId", nativeQuery = true)
    List<NotificationEntity> findByRecipientId(@Param("recipientId") Long recipientId);
}