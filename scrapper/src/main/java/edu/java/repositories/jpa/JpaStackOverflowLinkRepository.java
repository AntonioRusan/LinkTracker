package edu.java.repositories.jpa;

import edu.java.models.jpa.StackOverflowLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStackOverflowLinkRepository extends JpaRepository<StackOverflowLinkEntity, Long> {
}
