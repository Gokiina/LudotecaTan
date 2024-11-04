package com.ccsw.tutorial.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePage<T> extends PageImpl<T> {

    private static final long serialVersionUID = 1L;

    // Constructor que acepta List<T>, Pageable y long
    public ResponsePage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    // Otros constructores...
    public ResponsePage(List<T> content) {
        super(content);
    }

    public ResponsePage() {
        super(new ArrayList<>());
    }
}
