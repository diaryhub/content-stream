package com.content.content_stream.content;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByGenre(Genre genre);

    List<Content> findByContentType(ContentType contentType);

    List<Content> findByGenreAndContentType(Genre genre, ContentType contentType);
}
