package com.github.TrendingRepos;

/**
 * Created by BlackJack on 2017-12-31.
 */

/* The Model Of this Application */
public class Repository {
    String name;
    String description;
    String repoOwner;
    int numberStars;
    String avatar_url;

    public Repository(String name, String description,int numberStars, String repoOwner, String avatar_url) {
        this.name = name;
        this.description = description;
        this.repoOwner = repoOwner;
        this.numberStars = numberStars;
        this.avatar_url = avatar_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public void setNumberStars(int numberStars) {
        this.numberStars = numberStars;
    }

    public void setPhoto(String photo) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRepoOwner() {
        return repoOwner;
    }

    public int getNumberStars() {
        return numberStars;
    }

    public String getPhoto() {
        return avatar_url;
    }
}
