package nurbek.librarymanagementsystem.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.BookStatus;
import nurbek.librarymanagementsystem.entity.BookEntity;
import nurbek.librarymanagementsystem.repository.BookRepository;
import nurbek.librarymanagementsystem.repository.BookReservationRepository;
import nurbek.librarymanagementsystem.service.LibraryService;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@PersistJobDataAfterExecution
public class DeleteBookJob implements Job {
    private LibraryService libraryService;
    private BookRepository bookRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
//        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        // Delete books that are archived and have been archived for more than 30 days
        Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        Date date = calendar.getTime();

        List<BookEntity> booksToDelete = bookRepository.getBookEntitiesByStatusIgnoreCaseAndArchiveDateBefore(BookStatus.ARCHIVED.name(), date);
        booksToDelete.forEach(book -> {
            log.info("Deleting book: {}", book.getTitle());
            libraryService.deleteBook(book.getId());
        });
    }
}