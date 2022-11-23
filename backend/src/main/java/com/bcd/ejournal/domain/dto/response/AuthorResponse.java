package com.bcd.ejournal.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Integer authorId;
    private String fullName;
    private String introduction;
    private String education;
    private String address;
    private String slug;
}
