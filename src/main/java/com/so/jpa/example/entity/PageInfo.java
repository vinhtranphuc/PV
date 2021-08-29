package com.so.jpa.example.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "tb_page_info")
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "restriction", nullable = false, columnDefinition = "varchar(255) default ''")
    private String restriction;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "facebook_actived")
    private Boolean facebookActived;

    @Column(name = "pinterest_url")
    private String pinterestUrl;

    @Column(name = "pinterest_actived")
    private Boolean pinterestActived;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "youtube_actived")
    private Boolean youtubeActived;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "instagram_actived")
    private Boolean instagramActived;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "twitter_actived")
    private Boolean twitterActived;

    @Column(name = "flickr_url")
    private String flickrUrl;

    @Column(name = "flickr_actived")
    private Boolean flickrActived;

    @Column(name = "about_us")
    private String aboutUs;

    @Column(name = "contact_us")
    private String contactUs;
}
