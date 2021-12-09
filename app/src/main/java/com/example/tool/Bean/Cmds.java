package com.example.tool.Bean;

import java.sql.Date;

public class Cmds {
    public int id;
    public int uid;
    public String host;
    public String command;
    public String cmdtime;

    @Override
    public String toString() {
        return "Cmds{" +
                "id=" + id +
                ", uid=" + uid +
                ", host='" + host + '\'' +
                ", command='" + command + '\'' +
                ", cmdtime=" + cmdtime +
                '}';
    }
}
