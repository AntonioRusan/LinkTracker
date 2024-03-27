package edu.java.repositories.jpa;

import edu.java.models.jpa.GitHubLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGitHubLinkRepository extends JpaRepository<GitHubLinkEntity, Long> {
}
