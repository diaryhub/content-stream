package com.content.content_stream.watchhistory;

import com.content.content_stream.global.common.ApiResponse;
import com.content.content_stream.watchhistory.dto.WatchRequest;
import com.content.content_stream.watchhistory.dto.WatchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watch-history")
@RequiredArgsConstructor
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @PostMapping
    public ApiResponse<Void> record(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WatchRequest request) {
        watchHistoryService.record(userDetails.getUsername(), request);
        return ApiResponse.ok(null);
    }

    @GetMapping
    public ApiResponse<List<WatchResponse>> getMyHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(watchHistoryService.getMyHistory(userDetails.getUsername()));
    }

    @GetMapping("/{contentId}")
    public ApiResponse<List<WatchResponse>> getContentHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long contentId) {
        return ApiResponse.ok(watchHistoryService.getContentHistory(userDetails.getUsername(), contentId));
    }
}
