package nnero.netty.im.client.console;

import nnero.netty.im.Single;
import nnero.netty.im.protocol.packet.CreateGroupRequest;
import nnero.netty.im.protocol.packet.Packet;

/**
 * Author: NNERO
 * Time : 下午3:23 19-2-25
 */
@Single
public class CreateConsoleCommand implements IConsoleCommand {
    @Override
    public Packet doBusiness(ConsoleInput input) {
        System.out.println("Enter groupName:");
        String group = input.getInput();
        System.out.println("Enter maxPerson:");
        int max = Integer.parseInt(input.getInput());

        CreateGroupRequest request = new CreateGroupRequest();
        request.setGroupName(group);
        request.setMaxPersons(max);
        return request;
    }
}
