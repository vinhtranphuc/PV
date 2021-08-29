package com.so.jpa.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_post_images")
public class PostImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name="FK_tbPosts_tbPostImages"))
    private Post post;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "standard_image_path")
    private String standardImagePath;

    @Column(name = "small_image_path")
    private String smallImagePath;
}
