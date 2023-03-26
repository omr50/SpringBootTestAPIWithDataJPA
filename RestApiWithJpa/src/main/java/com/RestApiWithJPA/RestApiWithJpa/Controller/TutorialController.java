package com.RestApiWithJPA.RestApiWithJpa.Controller;

import com.RestApiWithJPA.RestApiWithJpa.Repository.TutorialRepository;
import com.RestApiWithJPA.RestApiWithJpa.model.Tutorial;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//The @CrossOrigin annotation in Spring Boot is used to enable Cross-Origin Resource Sharing (CORS)
//for a particular controller or controller method. CORS is a mechanism that allows resources on a
//web page to be requested from another domain outside the domain from which the resource originated.
//Origins is set to "http://localhost:8081", which means that requests coming from "http://localhost:8081"
//are allowed to access the API.
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {
    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
        List<Tutorial> tutorials = new ArrayList<Tutorial>();
        try {
            if (title == null)
                // finds all tutorials and for each of them we are going
                // to add them to the tutorials list.
                tutorialRepository.findAll().forEach(tutorials::add);
            else
                // if they specified a title in the query string
                tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable long id){
        System.out.println("id ran");
//         Optional<> class is a container object that may or may not contain
//         a non null value. Its commonly used in Java and Spring Boot to
//         represent the possibility of null values. Good way of preventing
//         null pointer exception errors in your code. The isPresent() method
//         allows us to check if the object inside of it exists. If it does
//         we can use .get() to get it. Optional is often used in cases where
//         we are fetching data from the database. We see that the findById
//         method has a return type of optional.
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if (tutorial.isPresent())
            return new ResponseEntity<>(tutorial.get(), HttpStatus.OK);
        // otherwise return not found status code
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial){
        System.out.println("Posted a new tutorial");
        try{
            Tutorial _tutorial = tutorialRepository
                    .save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial){
        System.out.println("Ran Put");
        try{
            Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
            if (tutorialData.isPresent()){
                Tutorial _tutorial = tutorialData.get();
                _tutorial.setDescription(tutorial.getDescription());
                _tutorial.setPublished(tutorial.isPublished());
                _tutorial.setTitle(tutorial.getTitle());
                return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> deleteTutorial(@PathVariable("id") long id){
        try{
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<Tutorial> deleteAllTutorials(){
        try {
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished(){
        try {
            List<Tutorial> publishedTutorials = tutorialRepository.findByPublished(true);
            if (publishedTutorials.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(publishedTutorials, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
