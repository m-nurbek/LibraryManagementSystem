package nurbek.librarymanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
public class Notification {
    private Long id;
    @NotBlank(message = "Subject is required")
    @Size(min = 1, max = 200, message = "Subject must be no longer than 100 characters")
    private String subject;
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Content must be no longer than 2000 characters")
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate = new Date();

    @NotBlank(message = "Recipient is required")
    private Account account;

    @JsonCreator
    public Notification(
            @JsonProperty("id") Long id,
            @JsonProperty("subject") String subject,
            @JsonProperty("content") String content,
            @JsonProperty("publishDate") Date publishDate,
            @JsonProperty("recipient") Account account) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.publishDate = publishDate;
        this.account = account;
    }

    public static class Parser {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(Notification notification) {
            try {
                final StringWriter writer = new StringWriter();
                mapper.writeValue(writer, notification);
                return writer.toString();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static Notification parseJson(String json) {
            try {
                return mapper.readValue(json, Notification.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}