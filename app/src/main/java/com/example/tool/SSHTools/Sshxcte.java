//package com.example.tool.SSHTools;
//import net.neoremind.sshxcute.core.ConnBean;
//import net.neoremind.sshxcute.core.IOptionName;
//import net.neoremind.sshxcute.core.Result;
//import net.neoremind.sshxcute.core.SSHExec;
//import net.neoremind.sshxcute.exception.TaskExecFailException;
//import net.neoremind.sshxcute.task.impl.ExecCommand;
//
//public class Sshxcte {
//    public static void main(String[] args) {
//        SSHExec sshExec = null;
//        try {
//            //远程登录主机
//            ConnBean connBean = new ConnBean("81.68.127.14", "ubuntu", "sb.123..");
//            sshExec = SSHExec.getInstance(connBean);
//            System.out.println( sshExec.connect());
//            //创建要执行的内容
//            ExecCommand command = new ExecCommand("w|cat");
//            Result result = sshExec.exec(command);
//            if (result.isSuccess){
//                System.out.println(result.sysout);
//            }else {
//                System.out.println(result.error_msg);
//            }
//        } catch (TaskExecFailException e) {
//            e.printStackTrace();
//        }finally {
//            sshExec.disconnect();
//        }
//    }
//}
//
