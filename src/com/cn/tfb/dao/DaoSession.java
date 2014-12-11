package com.cn.tfb.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import com.cn.tfb.dao.internal.DaoConfig;
import com.cn.tfb.dao.scope.IdentityScopeType;

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession
{

	public DaoSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap)
	{
		super(db);

	}

	public void clear()
	{
	}

}