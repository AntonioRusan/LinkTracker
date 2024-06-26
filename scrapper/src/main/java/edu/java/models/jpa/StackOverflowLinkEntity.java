package edu.java.models.jpa;

import edu.java.models.StackOverflowLink;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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
@Table(name = "stackoverflow_link")
public class StackOverflowLinkEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @OneToOne @JoinColumn(name = "link_id", referencedColumnName = "id") @MapsId private LinkEntity link;
    @Column(name = "last_answer_date") private OffsetDateTime lastAnswerDate;

    public StackOverflowLinkEntity(
        LinkEntity link,
        OffsetDateTime lastAnswerDate
    ) {
        this.link = link;
        this.lastAnswerDate = lastAnswerDate;
    }

    public StackOverflowLink toStackOverflowLink() {
        return new StackOverflowLink(link.getId(), lastAnswerDate);
    }
}
