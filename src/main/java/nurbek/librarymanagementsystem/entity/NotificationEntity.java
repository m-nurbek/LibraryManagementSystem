package nurbek.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nurbek.librarymanagementsystem.dto.Notification;

import java.util.Date;

@Entity
@Table(name = "NOTIFICATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "notification_seq")
    @TableGenerator(
            name = "notification_seq",
            table = "id_gen_table",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val",
            initialValue = 1000,
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SUBJECT", nullable = false)
    private String subject;
    @Column(name = "CONTENT", nullable = false)
    private String content;
    @Column(name = "PUBLISH_DATE", nullable = false)
    private Date publishDate = new Date();

    @ManyToOne
    @JoinColumn(name = "RECIPIENT_ID", nullable = false)
    private AccountEntity account;

    public Notification toDto() {
        return new Notification(id, subject, content, publishDate, account.toDto());
    }

    public static NotificationEntity fromDto(Notification notification) {
        if (notification == null) {
            return null;
        }

        var entity = new NotificationEntity();
        entity.setId(notification.getId());
        entity.setSubject(notification.getSubject());
        entity.setContent(notification.getContent());
        entity.setPublishDate(notification.getPublishDate());
        entity.setAccount(AccountEntity.fromDto(notification.getAccount()));
        return entity;
    }
}