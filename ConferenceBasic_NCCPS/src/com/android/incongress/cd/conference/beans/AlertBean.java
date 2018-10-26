package com.android.incongress.cd.conference.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

//大会提醒
public class AlertBean implements Parcelable {
	public final static int TYPE_SESSTION = 0;
	public final static int TYPE_MEETING = 1;
	public final static int TYPE_DISABLE = 0;
	public final static int TYPE_ENABLE = 1;

    private int id;//提醒id
    private String date;//提醒时间
    private String repeatdistance;//提醒间隔
    private String repeattimes;//提醒次数
    private int enable;//提醒可用 0不可用 1为可用
    private String title;//提醒内容
    private int type;//提醒类型 0会议 1为发言
    private String relativeid;//提醒关联ID 关联sessionid或meetingid；
    private String start;
    private String end;
    private String room;
    private long time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRepeatdistance() {
		return repeatdistance;
	}
	public void setRepeatdistance(String repeatdistance) {
		this.repeatdistance = repeatdistance;
	}
	public String getRepeattimes() {
		return repeattimes;
	}
	public void setRepeattimes(String repeattimes) {
		this.repeattimes = repeattimes;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRelativeid() {
		return relativeid;
	}
	public void setRelativeid(String relativeid) {
		this.relativeid = relativeid;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "AlertBean [id=" + id + ", date=" + date + ", repeatdistance="
				+ repeatdistance + ", repeattimes=" + repeattimes + ", enable="
				+ enable + ", title=" + title + ", type=" + type
				+ ", relativeid=" + relativeid + ", start=" + start + ", end="
				+ end + ", room=" + room + ", time=" + time + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.date);
		dest.writeString(this.repeatdistance);
		dest.writeString(this.repeattimes);
		dest.writeInt(this.enable);
		dest.writeString(this.title);
		dest.writeInt(this.type);
		dest.writeString(this.relativeid);
		dest.writeString(this.start);
		dest.writeString(this.end);
		dest.writeString(this.room);
		dest.writeLong(this.time);
	}

	public AlertBean() {
	}

	protected AlertBean(Parcel in) {
		this.id = in.readInt();
		this.date = in.readString();
		this.repeatdistance = in.readString();
		this.repeattimes = in.readString();
		this.enable = in.readInt();
		this.title = in.readString();
		this.type = in.readInt();
		this.relativeid = in.readString();
		this.start = in.readString();
		this.end = in.readString();
		this.room = in.readString();
		this.time = in.readLong();
	}

	public static final Creator<AlertBean> CREATOR = new Creator<AlertBean>() {
		@Override
		public AlertBean createFromParcel(Parcel source) {
			return new AlertBean(source);
		}

		@Override
		public AlertBean[] newArray(int size) {
			return new AlertBean[size];
		}
	};
}
