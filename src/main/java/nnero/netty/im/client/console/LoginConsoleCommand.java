package nnero.netty.im.client.console;

import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.LoginRequest;
import nnero.netty.im.protocol.packet.Packet;

/**
 * Author: NNERO
 * Time : 下午3:12 19-2-25
 */
@Single
public class LoginConsoleCommand implements IConsoleCommand {

    @Override
    public Packet doBusiness(ConsoleInput input) {
        System.out.println("Enter username:");
        String username = input.getInput();
        System.out.println("Enter password:");
        String password = input.getInput();

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }
}
