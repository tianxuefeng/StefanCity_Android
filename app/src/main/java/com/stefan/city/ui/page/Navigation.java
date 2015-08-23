package com.stefan.city.ui.page;

import java.io.Serializable;

/**
 * TabPage导航实体
 * @author 辰梦
 *
 */
public class Navigation implements Serializable {
	
	public final static int TYPE_0 = 0;		// �?��
	public final static int TYPE_1 = 1;		// 周榜
	public final static int TYPE_2 = 2;		// 月榜
	private int type;
	private String url;
	private String title;
	private Integer iconId;

	public Navigation(int type, String url, String title) {
		this(type, url, title, null);
	}
	
	public Navigation(int type, String url, String title, Integer iconId) {
		this.type = type;
		this.url = url;
		this.title = title;
		this.iconId = iconId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getIconId() {
		return iconId;
	}

	public void setIconId(Integer iconId) {
		this.iconId = iconId;
	}

	@Override
	public String toString() {
		return "Navigation [type=" + type + ", url=" + url + ", title=" + title
				+ ", iconId=" + iconId + "]";
	}

}
