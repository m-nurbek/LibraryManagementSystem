package nurbek.librarymanagementsystem.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "library.books")
@Component
@Data
public class ConfigurationProperty {
    private int pageSize = 10;
}