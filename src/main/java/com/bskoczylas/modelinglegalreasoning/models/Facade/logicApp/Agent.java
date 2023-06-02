package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import java.util.List;
import java.util.ArrayList;

public class Agent {
    private static int nextId = 1;
    private int id;
    private String name;
    private boolean isJudge = false;

    public Agent(String name) {
        this.id = nextId++;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isJudge() {
        return isJudge;
    }

    public void setJudge(boolean judge) {
        isJudge = judge;
    }
}