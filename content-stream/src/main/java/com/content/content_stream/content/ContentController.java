package com.content.content_stream.content;

import com.content.content_stream.content.dto.ContentDetailResponse;
import com.content.content_stream.content.dto.ContentResponse;
import com.content.content_stream.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping
    public ApiResponse<List<ContentResponse>> getContents(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) ContentType contentType) {
        return ApiResponse.ok(contentService.getContents(genre, contentType));
    }

    @GetMapping("/{id}")
    public ApiResponse<ContentDetailResponse> getContent(@PathVariable Long id) {
        return ApiResponse.ok(contentService.getContent(id));
    }
}
