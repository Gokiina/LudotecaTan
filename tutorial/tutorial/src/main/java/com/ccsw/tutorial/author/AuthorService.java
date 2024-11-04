package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.common.criteria.AuthorSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {

    Author get(Long id);

    Page<Author> findPage(AuthorSearchDto dto);

    void save(Long id, AuthorDto dto);

    void delete(Long id) throws Exception;

    List<Author> findAll();

}