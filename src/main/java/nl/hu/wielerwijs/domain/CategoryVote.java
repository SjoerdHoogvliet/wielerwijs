package nl.hu.wielerwijs.domain;

import nl.hu.security.domain.User;

public class CategoryVote {
    private RiderCategoryEnum category;
    private User user;
    private Renner renner;

    public CategoryVote(RiderCategoryEnum category, User user, Renner renner) {
        this.category = category;
        this.user = user;
        this.renner = renner;
    }

    public RiderCategoryEnum getCategory() {
        return category;
    }

    public User getUser() {
        return user;
    }

    public Renner getRenner() {
        return renner;
    }
}