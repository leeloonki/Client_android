package com.example.tool.Bean;

public class Host {
    public int id;
    public int uid;
    public String host;
    public int port;
    public String username;
    public String password;

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", uid=" + uid +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
