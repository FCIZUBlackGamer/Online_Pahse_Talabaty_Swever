package com.talabaty.swever.admin.Managment.Privilages.ControlPrivilege;

public class PrivilegeItem {
    String id, name;
    boolean read, write, edit, delete;

    public PrivilegeItem(String id, String name, boolean read, boolean write, boolean edit, boolean delete) {
        this.id = id;
        this.name = name;
        this.read = read;
        this.write = write;
        this.edit = edit;
        this.delete = delete;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    public boolean getRead(){
        return read;
    }public boolean getWrite(){
        return write;
    }public boolean getEdit(){
        return edit;
    }public boolean getDelete(){
        return delete;
    }
}
