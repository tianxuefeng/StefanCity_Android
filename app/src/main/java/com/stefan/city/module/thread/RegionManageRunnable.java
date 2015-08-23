package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.service.RegionService;

import android.os.Handler;

public class RegionManageRunnable extends REBaseRunnable {
	
	public static final int STATUS_DEL = 1;
	
	public static final int STATUS_UPDATE = 2;
	
	public static final int STATUS_ADD = 3;
	
	private int operateStatus;
	
	private RegionManEntity entity;
	
	private RegionService regionService;
	
	public RegionManageRunnable(Handler handler, RegionManEntity entity, int operateStatus) {
		super(handler);
		this.regionService = new RegionService();
		this.operateStatus = operateStatus;
		this.entity = entity;
	}

	@Override
	public void run() {
		int what = -1;
		if (entity != null && REHttpUtility.isCheckConOK()) {
			switch(operateStatus) {
			case STATUS_ADD:
				what = regionService.insert(entity);
				break;
				
			case STATUS_DEL:
				what = regionService.delete(entity.getId());
				break;
				
			case STATUS_UPDATE:
				what = regionService.update(entity);
				break;
			}
		}
		sendMessage(what, operateStatus);
	}
}
