package com.cn.tfb.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Properties;

import com.cn.tfb.util.ReflectUtil;

import android.content.Context;

/**
 * @author hzx 2014年4月21日
 * @version V1.0
 */
public class PropertiesConfig implements IConfig
{

	/** assets中配置信息文件 */
	private String assetsPath = "/assets/tacdonfig.properties";
	/** 软件Files文件夹中配置信息文件 */
	private String filesPath = "tacdonfig.properties";
	private static PropertiesConfig mPropertiesConfig;
	private static final String LOADFLAG = "assetsload";
	private Context mContext;
	private Properties mProperties;

	private PropertiesConfig(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 获得系统资源类
	 * 
	 * @param context
	 * @return
	 */
	public static PropertiesConfig getPropertiesConfig(Context context)
	{
		if (mPropertiesConfig == null)
		{
			mPropertiesConfig = new PropertiesConfig(context);
		}
		return mPropertiesConfig;
	}

	@Override
	public void loadConfig()
	{
		Properties props = new Properties();
		InputStream in = PropertiesConfig.class.getResourceAsStream(assetsPath);
		try
		{
			if (in != null)
			{
				props.load(in);
				Enumeration<?> e = props.propertyNames();
				if (e.hasMoreElements())
				{
					while (e.hasMoreElements())
					{
						String s = (String) e.nextElement();
						props.setProperty(s, props.getProperty(s));
					}
				}
			}
			setBoolean(LOADFLAG, true);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public Boolean isLoadConfig()
	{
		return getBoolean(LOADFLAG, false);
	}

	public void setConfig(String key, String value)
	{

		if (value != null)
		{
			Properties props = getProperties();
			props.setProperty(key, value);
			setProperties(props);
		}

	}

	public String getAssetsPath()
	{
		return assetsPath;
	}

	public void setAssetsPath(String assetsPath)
	{
		this.assetsPath = assetsPath;
	}

	public String getFilesPath()
	{
		return filesPath;
	}

	public void setFilesPath(String filesPath)
	{
		this.filesPath = filesPath;
	}

	/**
	 * 返回配置关于配置实体
	 * 
	 * @return 返回配置实体
	 */
	private Properties getProperties()
	{
		if (mProperties == null)
		{
			mProperties = getPro();
		}
		return mProperties;
	}

	private Properties getPro()
	{
		Properties props = new Properties();
		try
		{
			InputStream in = mContext.openFileInput(filesPath);
			props.load(in);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return props;
	}

	private void setProperties(Properties p)
	{
		OutputStream out;
		try
		{
			out = mContext.openFileOutput(filesPath, Context.MODE_PRIVATE);
			p.store(out, null);
			out.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close()
	{

	}

	@Override
	public boolean isClosed()
	{
		return false;
	}

	@Override
	public void setString(String key, String value)
	{
		setConfig(key, value);
	}

	@Override
	public void setInt(String key, int value)
	{
		setString(key, String.valueOf(value));
	}

	@Override
	public void setBoolean(String key, Boolean value)
	{
		setString(key, String.valueOf(value));
	}

	@Override
	public void setByte(String key, byte[] value)
	{
		setString(key, String.valueOf(value));
	}

	@Override
	public void setShort(String key, short value)
	{
		setString(key, String.valueOf(value));
	}

	@Override
	public void setLong(String key, long value)
	{
		setString(key, String.valueOf(value));
	}

	@Override
	public void setFloat(String key, float value)
	{
		setString(key, String.valueOf(value));
	}

	@Override
	public void setDouble(String key, double value)
	{
		setString(key, String.valueOf(value));
	}

	public String getConfig(String key, String defaultValue)
	{
		return getProperties().getProperty(key, defaultValue);
	}

	@Override
	public String getString(String key, String defaultValue)
	{
		return getConfig(key, defaultValue);
	}

	@Override
	public int getInt(String key, int defaultValue)
	{
		try
		{
			return Integer.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;

	}

	@Override
	public boolean getBoolean(String key, Boolean defaultValue)
	{
		try
		{
			return Boolean.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public byte[] getByte(String key, byte[] defaultValue)
	{
		try
		{
			return getString(key, "").getBytes();
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public short getShort(String key, Short defaultValue)
	{
		try
		{
			return Short.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public long getLong(String key, Long defaultValue)
	{
		try
		{
			return Long.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public float getFloat(String key, Float defaultValue)
	{
		try
		{
			return Float.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public double getDouble(String key, Double defaultValue)
	{
		try
		{
			return Double.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	@Override
	public String getString(int resID, String defaultValue)
	{
		return getString(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public int getInt(int resID, int defaultValue)
	{
		return getInt(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public boolean getBoolean(int resID, Boolean defaultValue)
	{
		return getBoolean(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public byte[] getByte(int resID, byte[] defaultValue)
	{
		return getByte(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public short getShort(int resID, Short defaultValue)
	{
		return getShort(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public long getLong(int resID, Long defaultValue)
	{
		return getLong(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public float getFloat(int resID, Float defaultValue)
	{
		return getFloat(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public double getDouble(int resID, Double defaultValue)
	{
		return getDouble(this.mContext.getString(resID), defaultValue);
	}

	@Override
	public void setString(int resID, String value)
	{
		setString(this.mContext.getString(resID), value);
	}

	@Override
	public void setInt(int resID, int value)
	{
		setInt(this.mContext.getString(resID), value);
	}

	@Override
	public void setBoolean(int resID, Boolean value)
	{
		setBoolean(this.mContext.getString(resID), value);
	}

	@Override
	public void setByte(int resID, byte[] value)
	{
		setByte(this.mContext.getString(resID), value);
	}

	@Override
	public void setShort(int resID, short value)
	{
		setShort(this.mContext.getString(resID), value);
	}

	@Override
	public void setLong(int resID, long value)
	{
		setLong(this.mContext.getString(resID), value);
	}

	@Override
	public void setFloat(int resID, float value)
	{
		setFloat(this.mContext.getString(resID), value);
	}

	@Override
	public void setDouble(int resID, double value)
	{
		setDouble(this.mContext.getString(resID), value);
	}

	@Override
	public void setConfig(Object entity)
	{
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{

			if (!ReflectUtil.isTransient(field))
			{
				if (ReflectUtil.isBaseDateType(field))
				{
					String columnName = ReflectUtil.getFieldName(field);
					field.setAccessible(true);
					setValue(field, columnName, entity);
				}
			}
		}
	}

	private void setValue(Field field, String columnName, Object entity)
	{
		try
		{
			Class<?> clazz = field.getType();
			if (clazz.equals(String.class))
			{
				setString(columnName, (String) field.get(entity));
			}
			else if (clazz.equals(Integer.class) || clazz.equals(int.class))
			{
				setInt(columnName, (Integer) field.get(entity));
			}
			else if (clazz.equals(Float.class) || clazz.equals(float.class))
			{
				setFloat(columnName, (Float) field.get(entity));
			}
			else if (clazz.equals(Double.class) || clazz.equals(double.class))
			{
				setDouble(columnName, (Double) field.get(entity));
			}
			else if (clazz.equals(Short.class) || clazz.equals(Short.class))
			{
				setShort(columnName, (Short) field.get(entity));
			}
			else if (clazz.equals(Long.class) || clazz.equals(long.class))
			{
				setLong(columnName, (Long) field.get(entity));
			}
			else if (clazz.equals(Boolean.class))
			{
				setBoolean(columnName, (Boolean) field.get(entity));
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public <T> T getConfig(Class<T> clazz)
	{
		Field[] fields = clazz.getDeclaredFields();
		T entity = null;
		try
		{
			entity = clazz.newInstance();
			for (Field field : fields)
			{
				field.setAccessible(true);
				if (!ReflectUtil.isTransient(field))
				{
					if (ReflectUtil.isBaseDateType(field))
					{

						String columnName = ReflectUtil.getFieldName(field);
						field.setAccessible(true);
						getValue(field, columnName, entity);
					}
				}

			}
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return entity;
	}

	private <T> void getValue(Field field, String columnName, T entity)
	{
		try
		{
			Class<?> clazz = field.getType();
			if (clazz.equals(String.class))
			{
				field.set(entity, getString(columnName, ""));
			}
			else if (clazz.equals(Integer.class) || clazz.equals(int.class))
			{
				field.set(entity, getInt(columnName, 0));
			}
			else if (clazz.equals(Float.class) || clazz.equals(float.class))
			{
				field.set(entity, getFloat(columnName, 0f));
			}
			else if (clazz.equals(Double.class) || clazz.equals(double.class))
			{
				field.set(entity, getDouble(columnName, 0.0));
			}
			else if (clazz.equals(Short.class) || clazz.equals(Short.class))
			{
				field.set(entity, getShort(columnName, (short) 0));
			}
			else if (clazz.equals(Long.class) || clazz.equals(long.class))
			{
				field.set(entity, getLong(columnName, 0l));
			}
			else if (clazz.equals(Byte.class) || clazz.equals(byte.class))
			{
				field.set(entity, getByte(columnName, new byte[8]));
			}
			else if (clazz.equals(Boolean.class))
			{
				field.set(entity, getBoolean(columnName, false));
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void remove(String key)
	{
		Properties props = getProperties();
		props.remove(key);
		setProperties(props);
	}

	@Override
	public void remove(String... keys)
	{
		Properties props = getProperties();
		for (String key : keys)
		{
			props.remove(key);
		}
		setProperties(props);
	}

	@Override
	public void clear()
	{
		Properties props = getProperties();
		props.clear();
		setProperties(props);
	}

	@Override
	public void open()
	{

	}
}
