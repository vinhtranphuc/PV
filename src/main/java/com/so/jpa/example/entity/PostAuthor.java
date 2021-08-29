package com.so.jpa.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_posts_authors")
public class PostAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name="FK_tbUsers_tbPostsAurhors"))
    private User author;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name="FK_tbPosts_tbPostsAurhors"))
    private Post post;
}
