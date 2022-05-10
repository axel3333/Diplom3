package ru.netology.javacore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.out;

public class TodoServer {

    private final int port;
    private final Todos todos;

    public TodoServer(int port, Todos todos) {
        this.port = port;
        this.todos = todos;
    }

    public void start() throws IOException {
        out.println("Starting server at " + port + "...");

        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try (Socket connection = serverSocket.accept();
                 PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String name = in.readLine();
                ServerOperation input = gson.fromJson(name, ServerOperation.class);
                if (input.getType().equals("ADD")) {
                    todos.addTask(input.getTask());
                } else if (input.getType().equals("REMOVE")) {
                    todos.removeTask(input.getTask());
                }
                out.println(todos.getAllTasks());
            } catch (IOException e) {
                out.println(e.getMessage());
            }
        }
    }
}

