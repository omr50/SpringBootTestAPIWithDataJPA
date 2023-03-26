package com.RestApiWithJPA.RestApiWithJpa.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RestApiWithJPA.RestApiWithJpa.model.Tutorial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

//    Now we can use JpaRepository’s methods:
//    save(), findOne(), findById(), findAll(), count(), delete(), deleteById(), etc.
//    without implementing these methods.
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
}

