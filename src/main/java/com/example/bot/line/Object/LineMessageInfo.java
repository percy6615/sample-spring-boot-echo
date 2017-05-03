package com.example.bot.line.Object;

public class LineMessageInfo {
	private String channeltoken;
	private String senderid;
	private String epochsecond;
	private String replytoken;
	private String text;
	private String messagetype;


	public String getChanneltoken() {
		return channeltoken;
	}

	public void setChanneltoken(String channeltoken) {
		this.channeltoken = channeltoken;
	}

	public String getSenderid() {
		return senderid;
	}

	public void setSenderid(String senderid) {
		this.senderid = senderid;
	}

	public String getEpochsecond() {
		return epochsecond;
	}

	public void setEpochsecond(String epochsecond) {
		this.epochsecond = epochsecond;
	}

	public String getReplytoken() {
		return replytoken;
	}

	public void setReplytoken(String replytoken) {
		this.replytoken = replytoken;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}
}
