package com.Application.MyReminder.Service;

import com.Application.MyReminder.entities.Note;
import com.Application.MyReminder.entities.User;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface ServiceDAO {

    void Save(User user);

    User get(String name);

    void saveNote(Note note);

    List<Note> FindNote(int id);
   void DeleteAllNote(int id);

   Note FindSingleNote(int UId, int NId);
   void UpdateItem(Note ReqNote, Note note);
}
