package com.lfl.shareapp.shareapp.event;

public class ShareEvent {
    public int updateView = 0;

    public ShareEvent(int updateView){
        this.updateView = updateView;
    }

    public int getMessage(){
        return this.updateView;
    }
}
