package ru.kc4kt4.reactive.crud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @Id
    private String id;
    private String firstName;
    private String surname;
    @Indexed(unique = true)
    private String email;
}
