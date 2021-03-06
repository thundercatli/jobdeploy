package com.devhc.jobdeploy.ssh;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.List;

public abstract class DeployDriver {

    private int timeout = 60;
    private boolean sudo;
    private Ansi.Color color = Ansi.Color.DEFAULT;

    public DeployDriver(){
    }

    public void execCommand(String command, String dir) {
        execCommand("cd " + dir + ";" + command);
    }

    public abstract void execCommand(String command);

    public abstract void put(String sourceFile, String target) throws IOException;

    public abstract List<Pair<String,Long>> ls(String dir) throws IOException;

    public void symlink(String dir, String from, String to) {
        String cmd = "cd " + dir + ";ln -snf " + from + " " + to;
        execCommand(cmd);
    }

    public void changePermission(String file, String chmod, String chown,
                                 boolean recursion) {
        if (StringUtils.isNotEmpty(chmod)) {
            if (recursion) {
                execCommand("chmod -R " + chmod + " " + file);
            } else {
                execCommand("chmod " + chmod + " " + file);
            }
        }
        if (StringUtils.isNotEmpty(chown)) {
            if (recursion) {
                execCommand("chown -R " + chown + " " + file);
            } else {
                execCommand("chown " + chown + " " + file);
            }
        }

    }

    public void changePermission(String file, String chmod, String chown) {
        changePermission(file, chmod, chown, false);
    }

    public void mkdir(String dir, String chmod, String chown) {
        if (".".equals(dir)) {
            return;
        }
        String initRemoteDir = "mkdir -p " + dir;
        execCommand(initRemoteDir);
        changePermission(dir, chmod, chown);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isSudo() {
        return sudo;
    }

    public void setSudo(boolean sudo) {
        this.sudo = sudo;
    }

    public Ansi.Color getColor() {
        return color;
    }

    public void setColor(Ansi.Color color) {
        this.color = color;
    }
}
