package nurbek.librarymanagementsystem;

import nurbek.librarymanagementsystem.entity.AccountEntity;
import nurbek.librarymanagementsystem.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldFindAccountsByRoleAndKeyword() {
        // given
        String keyword = "Claudette";
        String role = "USER";
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<AccountEntity> accountEntityPage = accountRepository.getAccountsByRoleAndKeyword(keyword, role, pageRequest);

        // then
        assertThat(accountEntityPage.getContent()).isNotEmpty();
        assertThat(accountEntityPage.getContent().size()).isGreaterThanOrEqualTo(1);
    }
}