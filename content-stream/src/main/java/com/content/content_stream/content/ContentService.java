package com.content.content_stream.content;

import com.content.content_stream.content.dto.ContentDetailResponse;
import com.content.content_stream.content.dto.ContentResponse;
import com.content.content_stream.global.exception.CustomException;
import com.content.content_stream.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;

    public List<ContentResponse> getContents(Genre genre, ContentType contentType) {
        List<Content> contents;
        if (genre != null && contentType != null) {
            contents = contentRepository.findByGenreAndContentType(genre, contentType);
        } else if (genre != null) {
            contents = contentRepository.findByGenre(genre);
        } else if (contentType != null) {
            contents = contentRepository.findByContentType(contentType);
        } else {
            contents = contentRepository.findAll();
        }
        return contents.stream().map(ContentResponse::from).toList();
    }

    public ContentDetailResponse getContent(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        return ContentDetailResponse.from(content);
    }
}
