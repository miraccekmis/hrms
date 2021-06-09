package com.hrms.core.adapters.concretes;

import org.springframework.stereotype.Service;

import com.hrms.core.adapters.abstracts.UserCheckService;

import fakeMernis.FakeMernis;

@Service
public class FakeMernisManager implements UserCheckService {

	FakeMernis fakeMernis = new FakeMernis();

	@Override
	public boolean CheckUser(String nationalityId, int birthYear) {
		return fakeMernis.TCKimlikNoDogrula(nationalityId, birthYear);

	}

}