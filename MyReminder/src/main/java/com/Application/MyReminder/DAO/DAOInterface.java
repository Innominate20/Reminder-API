package com.Application.MyReminder.DAO;

import com.Application.MyReminder.entities.Note;
import com.Application.MyReminder.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DAOInterface {
    void save(User user);

    User findUser(String name);

    ResponseEntity<String> saveNote(Note note);

    List<Note> findNote(int id);

    void deleteAllNote(int id);

    Note findSingleNote(int NId, int UId);

    void updateItem(Note ReqData, Note note);
}


