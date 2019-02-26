package nnero.netty.im.server.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: NNERO
 * Time : 上午10:39 19-2-20
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "increment")
    private int uid;

    @Column(name = "username",nullable = false,length = 50)
    private String username;

    @Column(name = "password",nullable = false,length = 50)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "register_time",nullable = false)
    private Date register_time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "active_time",nullable = false)
    private Date active_time;

    @Column(name = "state",nullable = false)
    private int state;
}
