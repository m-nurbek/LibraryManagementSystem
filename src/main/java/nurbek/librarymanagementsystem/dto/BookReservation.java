package nurbek.librarymanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

@Data
@Builder
public class BookReservation {
    private final Long id;

    @NotBlank(message = "Reservation date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final Date reservationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final Date dueDate;
    private final ReservationStatus status;

    private final Book book;
    private final Account account;

    @JsonCreator
    public BookReservation(
            @JsonProperty("id") Long id,
            @JsonProperty("reservationDate") Date reservationDate,
            @JsonProperty("dueDate") Date dueDate,
            @JsonProperty("status") ReservationStatus status,
            @JsonProperty("book") Book book,
            @JsonProperty("account") Account account
    ) {
        this.id = id;
        this.reservationDate = reservationDate;
        this.dueDate = dueDate;
        this.status = status;
        this.book = book;
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

        public static String toJson(BookReservation reservation) {
            try {
                final StringWriter writer = new StringWriter();
                mapper.writeValue(writer, reservation);
                return writer.toString();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }

        public static BookReservation parseJson(String json) {
            try {
                return mapper.readValue(json, BookReservation.class);
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}