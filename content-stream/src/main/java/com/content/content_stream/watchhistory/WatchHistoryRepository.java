package com.content.content_stream.watchhistory;

import com.content.content_stream.content.Content;
import com.content.content_stream.content.Episode;
import com.content.content_stream.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    List<WatchHistory> findByUserOrderByUpdatedAtDesc(User user);

    List<WatchHistory> findByUserAndContent(User user, Content content);

    Optional<WatchHistory> findByUserAndContentAndEpisodeIsNull(User user, Content content);

    Optional<WatchHistory> findByUserAndEpisode(User user, Episode episode);
}
