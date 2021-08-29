package com.so.jpa.example.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.so.jpa.primary.entity.audit.UserDateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "tb_posts")
public class Post extends UserDateAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name="FK_tbCategories_tbPosts"))
    private Category category;

    @OneToMany(mappedBy = "tag")
    private Set<PostTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<PostAuthor> authors = new HashSet<>();

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "language")
    private String language;

    @Column(name = "images_layout")
    private Integer imagesLayout;

    @Column(name = "times_of_view", nullable = false)
    private Integer timesOfView;

    @Column(name = "like_cnt", nullable = false)
    private Integer likeCnt;

    @Column(name = "published_at")
    private Date publishedAt;

    @Column(name = "has_images_ontop")
    private Boolean hasImagesOntop;

    @Column(name = "content")
    private String content;

    @Column(name = "summary")
    private String summary;

    @Column(name = "title")
    private String title;

    public Post() {
    	super();
    }
}
