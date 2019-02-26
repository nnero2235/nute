package nnero.netty.im.client.console;

import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.JoinGroupRequest;
import nnero.netty.im.protocol.packet.Packet;

/**
 * Author: NNERO
 * Time : 下午3:22 19-2-25
 */
@Single
public class JoinConsoleCommand implements IConsoleCommand {
    @Override
    public Packet doBusiness(ConsoleInput input) {
        System.out.println("Enter groupId:");
        int id = Integer.parseInt(input.getInput());

        JoinGroupRequest request = new JoinGroupRequest();
        request.setGroupId(id);
        return request;
    }
}
