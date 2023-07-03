package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value;

public class Value {
        private static int nextId = 1;
        private int id;
        private String name;

        public Value(String name) {
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



}
