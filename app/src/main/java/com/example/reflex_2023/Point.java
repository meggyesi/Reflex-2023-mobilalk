package com.example.reflex_2023;

public class Point {
    private String uid, when, point, username;
    private UserDAO userDAO;

    public Point() {
        userDAO = new UserDAO();
    }

    public Point(String uid, String when, String point, String username) {
        userDAO = new UserDAO();

        this.uid = uid;
        this.when = when;
        this.point = point;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    public String getPoint() {
        return point;
    }


    @Override
    public String toString() {
        return getUsername() + ": " + getPoint();
    }
}
