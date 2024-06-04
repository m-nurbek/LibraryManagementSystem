package nurbek.librarymanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

@Data
@Builder
public class Book {
    private final Long id;
    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 20, message = "ISBN must be between 10 and 20 characters")
    private final String ISBN;
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be no longer than 100 characters")
    private final String title;
    @NotBlank(message = "Language is required")
    @Size(max = 2, message = "Language must be no longer than 2 characters")
    private final String language;
    @Min(value = 1, message = "Number of pages must be a number between 1 and 9999")
    @Max(value = 9999, message = "Number of pages must be a number between 1 and 9999")
    @Digits(integer = 4, fraction = 0, message = "Number of pages must be a number between 0 and 9999")
    private final Integer numberOfPages;
    private final BookStatus status;
    private final Date publishDate;
    private final Date archiveDate;
    @Min(value = 1, message = "Number of copies must be a number between 1 and 999")
    @Max(value = 999, message = "Number of copies must be a number between 1 and 999")
    @Digits(integer = 3, fraction = 0, message = "Number of copies must be a number between 0 and 999")
    private final Integer numberOfCopies;
    private final Author author;

    @JsonCreator
    public Book(@JsonProperty("id") Long id,
                @JsonProperty("isbn") String isbn,
                @JsonProperty("title") String title,
                @JsonProperty("language") String language,
                @JsonProperty("numberOfPages") Integer numberOfPages,
                @JsonProperty("status") BookStatus status,
                @JsonProperty("publishDate") Date publishDate,
                @JsonProperty("archiveDate") Date archiveDate,
                @JsonProperty("numberOfCopies") Integer numberOfCopies,
                @JsonProperty("author") Author author) {
        this.id = id;
        ISBN = isbn;
        this.title = title;
        this.language = language;
        this.numberOfPages = numberOfPages;
        this.status = status;
        this.publishDate = publishDate;
        this.archiveDate = archiveDate;
        this.numberOfCopies = numberOfCopies;
        this.author = author;
    }

    public static class Parser {
        private static final ObjectMapper mapper = new ObjectMapper();

        static {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        }

        public static String toJson(Book book) {
            try {
                final StringWriter writer = new StringWriter();
                mapper.writeValue(writer, book);
                return writer.toString();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static Book parseJson(String json) {
            try {
                return mapper.readValue(json, Book.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}