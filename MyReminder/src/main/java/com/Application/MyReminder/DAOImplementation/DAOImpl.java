package com.Application.MyReminder.DAOImplementation;

import com.Application.MyReminder.DAO.DAOInterface;
import com.Application.MyReminder.HandleException.UserAlreadyExistsExep;
import com.Application.MyReminder.Service.ServiceDAO;
import com.Application.MyReminder.entities.Note;
import com.Application.MyReminder.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.ErrorResponseException;

import java.util.List;

@Repository
public class DAOImpl implements DAOInterface {
    private EntityManager entityManager;

    @Autowired
    public DAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void save(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encoded = bCryptPasswordEncoder.encode(user.getPassword());

        //Check if the user already exists
        User temuser = findUser(user.getUserName());
        if(temuser != null){

            throw new UserAlreadyExistsExep("User already exists!");

        }
        user.setPassword(encoded);
        entityManager.persist(user);
    }

    @Override
    public User findUser(String name) {
        TypedQuery<User> query = entityManager.createQuery("from User where  userName=:name",User.class);
        query.setParameter("name",name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ResponseEntity<String> saveNote(Note note){
        entityManager.persist(note);
        return ResponseEntity.ok("Item saved!");
    }

    @Override
    public List<Note> findNote(int id) {
        TypedQuery<Note> query = entityManager.createQuery("SELECT n FROM Note n WHERE n.user.id = :id", Note.class);
        query.setParameter("id",id );

        return query.getResultList();
    }

    @Override
    public void deleteAllNote(int id) {
        Query query = entityManager.createQuery("DELETE FROM Note n where n.user.id = :id");

        query.setParameter("id", id);
        query.executeUpdate();

    }

    @Override
    public Note findSingleNote(int NId, int UId) {
        TypedQuery<Note> query = entityManager.createQuery("SELECT N FROM Note N WHERE N.user.id = :UId AND N.id = :NId", Note.class);
        query.setParameter("NId", NId);
        query.setParameter( "UId", UId);

        return query.getSingleResult();
    }

    @Override
    public void updateItem(Note ReqData, Note note) {
        if (ReqData.getTitle() != null){
            note.setTitle(ReqData.getTitle());
        }
        if(ReqData.getContent() !=null){
            note.setContent(ReqData.getContent());
        }
        if(ReqData.getDateTime()!=null){
            note.setDateTime(ReqData.getDateTime());
        }
    }

}

