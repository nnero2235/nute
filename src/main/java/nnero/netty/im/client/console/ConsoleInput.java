package nnero.netty.im.client.console;

import com.google.common.base.Strings;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.client.ClientAttrs;
import nnero.netty.im.protocol.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: NNERO
 * Time : 下午2:52 19-2-25
 */
@Single
@Slf4j
public class ConsoleInput implements Runnable {

    private ReentrantLock inputLock;

    private Condition inputCondition;

    private Channel nettyChannel;

    private Thread inputThread;

    private Scanner scanner;

    private Map<String,ConsoleCommand> consoleCommandMap;

    private String tip;

    private volatile boolean shutdown;

    public ConsoleInput(){
        inputLock = new ReentrantLock();
        inputCondition = inputLock.newCondition();
        scanner = new Scanner(System.in);
        consoleCommandMap = new HashMap<>();

        ConsoleCommand[] commands = ConsoleCommand.values();
        StringBuilder sb = new StringBuilder("Enter Command:");
        for(ConsoleCommand c : commands){
            consoleCommandMap.put(c.getMark(),c);
            sb.append("[").append(c.getCommandText()).append("] ");
        }
        tip = sb.toString();
    }

    private void checkChannel(){
        if(nettyChannel == null){
            throw new RuntimeException("channel is not bind!");
        }
        if(!nettyChannel.isActive()){
            throw new RuntimeException("connection closed.");
        }
    }

    public void lockWaiting() {
        try {
            inputLock.lock();
            inputCondition.await();
        } catch (Exception e){
            log.error("",e);
        } finally {
            inputLock.unlock();
        }
    }

    public void unlockWaiting(){
        try {
            inputLock.lock();
            inputCondition.signalAll();
        } catch (Exception e){
            log.error("",e);
        } finally {
            inputLock.unlock();
        }
    }

    public void shutdown(){
        if(inputThread != null){
            shutdown = true;
        }
    }

    public String getInput(){
        return scanner.nextLine().trim();
    }

    public <T> T getChannelAttr(AttributeKey<T> key){
        checkChannel();
        return nettyChannel.attr(key).get();
    }

    public void bindChannel(Channel channel){
        if(nettyChannel != null){
            throw new RuntimeException("already bind channel.unBind first!");
        }
        this.nettyChannel = channel;
    }

    public void unbindChannel(){
        if(nettyChannel != null){
            nettyChannel = null;
        }
    }

    public void startInputThread(){
        inputThread = new Thread(this);
        inputThread.start();
    }


    @Override
    public void run() {
        while (!shutdown) {
            System.out.println(tip);
            String c = getInput();
            if (Strings.isNullOrEmpty(c)) {
                System.out.println("Command can't be empty.");
                continue;
            }
            ConsoleCommand command = consoleCommandMap.get(c);
            if(command == null){
                System.out.println("Command do not be found!");
                continue;
            }
            checkChannel();
            Packet packet = command.getCommand().doBusiness(this);
            nettyChannel.writeAndFlush(packet);
            if(command.isLock()){
                lockWaiting();
            }
        }
        log.info("Console exit!");
    }
}
