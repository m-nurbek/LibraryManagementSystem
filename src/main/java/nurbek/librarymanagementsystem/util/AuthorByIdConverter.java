package nurbek.librarymanagementsystem.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nurbek.librarymanagementsystem.dto.Author;
import nurbek.librarymanagementsystem.service.AuthorService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuthorByIdConverter implements Converter<String, Author> {
    private final AuthorService authorService;

    @Override
    public Author convert(String source) {
        long id = Long.parseLong(source);
        log.info("Converting author id: {}", id);
        return authorService.getAuthorById(id).orElse(null);
    }
}