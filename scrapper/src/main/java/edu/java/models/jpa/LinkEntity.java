package edu.java.models.jpa;

import edu.java.models.Link;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "url", unique = true) private String url;
    @Column(name = "last_check_time") private OffsetDateTime lastCheckTime;
    @Column(name = "updated_at") private OffsetDateTime updatedAt;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"))
    private Set<ChatEntity> chats = new HashSet<ChatEntity>();

    public LinkEntity(
        String url,
        OffsetDateTime updatedAt,
        OffsetDateTime lastCheckedTime
    ) {
        this.url = url;
        this.lastCheckTime = lastCheckedTime;
        this.updatedAt = updatedAt;
    }

    public Link toLink() {
        return new Link(id, url, lastCheckTime, updatedAt);
    }

    public void addChat(ChatEntity chat) {
        this.chats.add(chat);
    }

    public void deleteChat(ChatEntity chat) {
        this.chats.remove(chat);
    }

}
