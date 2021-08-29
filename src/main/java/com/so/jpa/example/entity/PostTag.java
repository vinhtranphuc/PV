package com.so.jpa.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_posts_tags")
public class PostTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false,foreignKey = @ForeignKey(name="FK_tbPosts_tbPostsTags"))
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(name="FK_tbTags_tbPostsTags"))
    private Tag tag;
}
