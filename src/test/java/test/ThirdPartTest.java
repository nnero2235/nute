package test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import nnero.netty.im.Single;
import nnero.netty.im.server.db.DataBase;
import nnero.netty.im.server.entity.User;
import nnero.netty.im.server.guice.ServerHandlerInjector;
import org.hibernate.annotations.Target;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;


/**
 * Author: NNERO
 * Time : 下午2:25 19-2-13
 */
@Slf4j
public class ThirdPartTest {

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static String getAutoCreateAESKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        return parseByte2HexStr(b);
    }

    @Test
    public void logTest() throws Exception {
        Logger log = LoggerFactory.getLogger(ThirdPartTest.class);
        log.debug("\\==== debug ====");
        log.trace("==== trace ====");
        log.info("==== info ====");
        log.error("==== error ====");
        log.warn("==== warn ====");
        log.info(getAutoCreateAESKey());
    }

    @Test
    public void hibernateSave(){
        User user = new User();
        user.setUsername("aaa");
        user.setPassword("aaa123");
        user.setRegister_time(new Date());
        user.setActive_time(new Date());
        user.setState(1);
//        DataBase.getInstance().save(user);
    }

    @Test
    public void hibernateQuery(){
//        User u = DataBase.getInstance().getUser("nnero","nnero123");
//        log.info(u.toString());
    }

    @Test
    public void reflect(){
        Reflections reflections = new Reflections("nnero.netty.im.server");
        Set<Class<?>> singles = reflections.getTypesAnnotatedWith(Single.class);
        for(Class<?> clazz : singles){
            log.info(clazz.getCanonicalName());
        }
    }

    @Test
    public void guice(){
        DataBase dataBase = ServerHandlerInjector.getInstance(DataBase.class);
        if(dataBase != null){
            User user = new User();
            user.setUsername("bbb");
            user.setPassword("bbb123");
            user.setRegister_time(new Date());
            user.setActive_time(new Date());
            user.setState(1);
            dataBase.save(user);
        } else {
            log.info("dataBase is null");
        }
    }

    @Test
    public void redission(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.1.254:6379")
                .setDatabase(0)
                .setPassword("ddkj");
        RedissonClient redisson = Redisson.create(config);

        RMap<String,String> rMap = redisson.getMap("haha");
        for(String key : rMap.keySet()){
            log.info("key:"+key+" -> value:"+rMap.get(key));
        }
    }

    @Test
    public void lettuce(){
        RedisClient redisClient = RedisClient.create("redis://ddkj@192.168.1.254:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        syncCommands.set("hehe", "Hello, Redis!");

        connection.close();
        redisClient.shutdown();
    }
}
