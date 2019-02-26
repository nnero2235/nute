package nnero.netty.im.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: NNERO
 * Time : 下午4:46 19-2-21
 */
@Data
@Entity
@Table(name = "chat_group")
public class Group {

    @Id
    @GeneratedValue(generator = "increment")
    private int group_id;

    @Column(name = "group_name",length = 20,nullable = false)
    private String group_name;

    @Column(name = "max_persons",length = 11,nullable = false)
    private int max_persons;

    @Column(name = "create_date",nullable = false)
    private Date create_date;
}
