package com.Application.MyReminder.Service;

import com.Application.MyReminder.DAO.DAOInterface;
import com.Application.MyReminder.entities.Note;
import com.Application.MyReminder.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImpl implements ServiceDAO{

    private DAOInterface ServDao;

    @Autowired
    public ServiceImpl(DAOInterface userDao) {
        this.ServDao = userDao;
    }

    @Override
    @Transactional
    public void Save(User user) {

        ServDao.save(user);
    }

    @Override
    public User get(String name) {
        return ServDao.findUser(name);
    }

    @Override
    @Transactional
    public void saveNote(Note note) {
        ServDao.saveNote(note);
    }

    @Override
    public List<Note> FindNote(int id) {
        return ServDao.findNote(id);

    }

    @Override
    @Transactional
    public void DeleteAllNote(int id) {
        ServDao.deleteAllNote(id);
    }

    @Override
    public Note FindSingleNote(int UId, int NId) {
        return ServDao.findSingleNote(NId,UId);
    }

    @Override
    @Transactional
    public void UpdateItem(Note ReqNote, Note note) {
        ServDao.updateItem(ReqNote, note);
    }
}
