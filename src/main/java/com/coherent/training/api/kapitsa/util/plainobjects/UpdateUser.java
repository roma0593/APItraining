package com.coherent.training.api.kapitsa.util.plainobjects;

public class UpdateUser {
    User userNewValues;
    User userToChange;

    public UpdateUser() {
    }

    public UpdateUser(User userNewValues, User userToChange) {
        this.userNewValues = userNewValues;
        this.userToChange = userToChange;
    }

    public User getUserNewValues() {
        return userNewValues;
    }

    public User getUserToChange() {
        return userToChange;
    }
}
