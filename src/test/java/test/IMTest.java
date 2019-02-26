package test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.protocol.CodeC;
import nnero.netty.im.protocol.packet.Command;
import nnero.netty.im.protocol.packet.LoginRequest;
import nnero.netty.im.protocol.packet.Packet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: NNERO
 * Time : 下午5:35 19-2-19
 */
@Slf4j
public class IMTest {

    @Test
    public void codeCTest(){
        LoginRequest request = new LoginRequest();
        request.setUserId(1);
        request.setUsername("nnero");
        request.setPassword("123");
        CodeC codeC = new CodeC();
        ByteBuf buf = codeC.encode(ByteBufAllocator.DEFAULT.ioBuffer(),request);
        Packet packet = codeC.decode(buf);

        if(packet.getCommand().equals(Command.LOGIN_REQUEST.getCommand())){
            LoginRequest loginRequest = (LoginRequest) packet;
            Assert.assertEquals(loginRequest.getUserId(),1);
            Assert.assertEquals(loginRequest.getUsername(),"nnero");
            Assert.assertEquals(loginRequest.getPassword(),"123");
        } else {
            log.error("parse fail");
        }
    }
}
