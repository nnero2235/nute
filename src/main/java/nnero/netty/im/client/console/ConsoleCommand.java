package nnero.netty.im.client.console;

import nnero.netty.im.client.guice.ClientHandlerInjector;

/**
 * Author: NNERO
 * Time : 下午3:10 19-2-25
 */
public enum ConsoleCommand {
    LOGIN("Login(L)","L",ClientHandlerInjector.getInstance(LoginConsoleCommand.class),true),
    MESSAGE("Message(M)","M",ClientHandlerInjector.getInstance(MessageConsoleCommand.class),false),
    LOGOUT("Logout(O)","O",ClientHandlerInjector.getInstance(LogoutConsoleCommand.class),true),
    QUIT("Quit(Q)","Q",ClientHandlerInjector.getInstance(QuitConsoleCommand.class),true),
    JOIN("Join(J)","J",ClientHandlerInjector.getInstance(JoinConsoleCommand.class),true),
    CREATE("Create(C)","C",ClientHandlerInjector.getInstance(CreateConsoleCommand.class),true);

    private IConsoleCommand command;

    private String mark;

    private String commandText;

    private boolean lock;

    ConsoleCommand(String commandText,String mark,IConsoleCommand command,boolean lock){
        this.commandText = commandText;
        this.mark = mark;
        this.command = command;
        this.lock = lock;
    }

    public IConsoleCommand getCommand() {
        return command;
    }

    public String getMark() {
        return mark;
    }

    public String getCommandText() {
        return commandText;
    }

    public boolean isLock() {
        return lock;
    }
}
