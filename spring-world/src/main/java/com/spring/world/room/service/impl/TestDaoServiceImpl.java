package com.spring.world.room.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.world.dao.RoleDao;
import com.spring.world.room.service.TestDaoService;

@Service
public class TestDaoServiceImpl implements TestDaoService {

	@Override
	public void testNoTransaction() {
		RoleDao roleDao = GlobalBeanFactory.getBeanByName(RoleDao.class);

		roleDao.insert1("yyyyyyyyyyyyy");
		roleDao.insert2(
				"ppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");
	}

	@Override
	@Transactional("test")
	public void testTransaction() {
		RoleDao roleDao = GlobalBeanFactory.getBeanByName(RoleDao.class);

		roleDao.insert1("ttttttttttttt");
		roleDao.insert2(
				"pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

	}

}
