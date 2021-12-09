package com.example.tool.SSHTools;

public class Test {
    public static void main( String[] args ) {
        String cmd = "ls -1h";
        Shell shell = new Shell("81.68.127.14", "ubuntu", "sb.123..");
        String execLog = shell.execCommand(cmd);
        System.out.println(execLog);
    }
}
