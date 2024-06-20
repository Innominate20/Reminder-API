package com.Application.MyReminder.Controller;


import com.Application.MyReminder.Security.JWT.JwtUtil;
import com.Application.MyReminder.Service.ServiceDAO;
import com.Application.MyReminder.entities.Note;
import com.Application.MyReminder.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.Application.MyReminder.Security.CustomUserDetailsService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Rest_Controller {

    private final AuthenticationManager authenticationManager;
    private final ServiceDAO serviceDAO;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;


    @Autowired
    public Rest_Controller(AuthenticationManager authenticationManager,CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil, ServiceDAO serviceDAO) {
        this.authenticationManager = authenticationManager;
        this.serviceDAO = serviceDAO;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("User not found!");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUserName());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/addNote")
    public void saveNote(@RequestBody Note note) {

            UserDetails temUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = serviceDAO.get(temUser.getUsername());
            note.setUser(user);
        serviceDAO.saveNote(note);
   }

    @PostMapping("/register")
    public void Register(@RequestBody User user){

         serviceDAO.Save(user);
    }

    @GetMapping("/NoteList")
    public List<Note> Notes(){
        UserDetails tmUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = serviceDAO.get(tmUser.getUsername());

        return serviceDAO.FindNote(user.getId());
    }

    @PostMapping("/deleteAllNotes")
    public void deleteNotes(){
        UserDetails tmUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = serviceDAO.get(tmUser.getUsername());

        serviceDAO.DeleteAllNote(user.getId());
    }

    @PostMapping("/updateNote")
    public ResponseEntity<String> update(@RequestBody Note note){
        UserDetails tmUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = serviceDAO.get(tmUser.getUsername());
        Note tmNote =  serviceDAO.FindSingleNote(user.getId(), note.getId());

        serviceDAO.UpdateItem(note, tmNote);
        return ResponseEntity.ok("Item Updated");
    }
}
