package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;

public interface Bot extends UpdatesListener {

    int process(List<Update> var1);

}
