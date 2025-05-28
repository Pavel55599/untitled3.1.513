package org.example.pavelkrykov.spring.rest;


import org.springframework.web.client.RestTemplate;

public class App {
    public static void main(String[] args) {

        Communication communication = new Communication();
        communication.performOperations();
    }
}



