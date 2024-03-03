package edu.java.bot.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private List<String> linkList;

    public User(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.linkList = new ArrayList<>();
    }

    public boolean addLink(String link) {
        if (linkList.contains(link)) {
            return false;
        } else {
            linkList.add(link);
            return true;
        }
    }

    public boolean removeLink(String link) {
        if (linkList.contains(link)) {
            linkList.remove(link);
            return true;
        } else {
            return false;
        }
    }

    public List<String> getLinkList() {
        return linkList;
    }
}
