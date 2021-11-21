package com.pet.comes.repository;

import com.pet.comes.model.Entity.Comment;
import com.pet.comes.model.IdClass.CommentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentID> {

}
