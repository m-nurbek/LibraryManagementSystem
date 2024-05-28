package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.controller.LibraryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LibraryManagementSystemApplicationTests {

    @Autowired
    private LibraryController libraryController;

    @Test
    void contextLoads() {
        assertThat(libraryController).isNotNull();
    }
}