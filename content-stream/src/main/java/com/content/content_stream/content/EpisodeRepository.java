package com.content.content_stream.content;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findByContentOrderByEpisodeNumberAsc(Content content);
}
