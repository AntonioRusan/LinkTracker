package edu.java.models.jpa;

import edu.java.models.Chat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class ChatEntity {
    @Id private Long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"))
    private Set<LinkEntity> links = new HashSet<LinkEntity>();

    public ChatEntity(Long id) {
        this.id = id;
    }

    public Chat toChat() {
        return new Chat(this.id);
    }

    public void addLink(LinkEntity link) {
        this.links.add(link);
    }

    public void deleteLink(LinkEntity link) {
        this.links.remove(link);
    }
}
