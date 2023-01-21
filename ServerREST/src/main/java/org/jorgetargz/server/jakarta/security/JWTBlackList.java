package org.jorgetargz.server.jakarta.security;

import java.util.ArrayList;
import java.util.List;

public class JWTBlackList {

    private static JWTBlackList instance;
    private final List<String> blackList;

    private JWTBlackList() {
        blackList = new ArrayList<>();
    }

    public static JWTBlackList getInstance() {
        if (instance == null) {
            instance = new JWTBlackList();
        }
        return instance;
    }

    public void addToken(String token) {
        blackList.add(token);
    }

    public boolean isTokenInBlackList(String token) {
        return blackList.contains(token);
    }
}
