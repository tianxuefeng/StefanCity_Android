package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.service.RegionService;

import android.os.Handler;

public class RegionAddRunnable extends REBaseRunnable {
	
	private RegionService regionService;
	
	private RegionManEntity entity;

	public RegionAddRunnable(Handler handler, RegionManEntity entity) {
		super(handler);
		regionService = new RegionService();
		this.entity = entity;
	}

	@Override
	public void run() {
		int what = 0;
		if (entity != null && REHttpUtility.isCheckConOK()) {
			what = regionService.insert(entity);
		}
		sendMessage(what, entity.getName());
	}

}
