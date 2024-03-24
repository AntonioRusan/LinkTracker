package edu.java.models.jpa;

import edu.java.models.GitHubLink;
import edu.java.models.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "github_link")
public class GitHubLinkEntity {
    @Id @OneToOne @JoinColumn(name = "link_id") private LinkEntity link;
    @Column(name = "last_pull_request_date") private OffsetDateTime lastPullRequestDate;

    public GitHubLinkEntity(
        LinkEntity link,
        OffsetDateTime lastPullRequestDate
    ) {
        this.link = link;
        this.lastPullRequestDate = lastPullRequestDate;
    }
    public GitHubLink toGitHubLink() {
        return new GitHubLink(link.getId(), lastPullRequestDate);
    }
}
