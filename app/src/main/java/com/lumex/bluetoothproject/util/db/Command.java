package com.lumex.bluetoothproject.util.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 阿泰Charles on 2016/12/08.
 */
@Entity
public class Command {
    @Id
    private Long id;
    private int commandType;
    private String commandCaption;
    private String commandContent;
    public String getCommandContent() {
        return this.commandContent;
    }
    public void setCommandContent(String commandContent) {
        this.commandContent = commandContent;
    }
    public String getCommandCaption() {
        return this.commandCaption;
    }
    public void setCommandCaption(String commandCaption) {
        this.commandCaption = commandCaption;
    }
    public int getCommandType() {
        return this.commandType;
    }
    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 439351006)
    public Command(Long id, int commandType, String commandCaption,
            String commandContent) {
        this.id = id;
        this.commandType = commandType;
        this.commandCaption = commandCaption;
        this.commandContent = commandContent;
    }
    @Generated(hash = 1834133387)
    public Command() {
    }
}
