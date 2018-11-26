package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2016/3/7.
 */
public class SearchRoomBean {
    private int position;
    private String roomName;
    private int roomId;

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomeName(String time) {
        this.roomName = time;
    }

    @Override
    public String toString() {
        return "SearchRoomBean{" +
                "position=" + position +
                ", roomName='" + roomName + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
