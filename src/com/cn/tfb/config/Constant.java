package com.cn.tfb.config;

import com.cn.tfb.dao.DaoMaster;
import com.cn.tfb.dao.DaoSession;
import com.cn.tfb.event.EventBus;
import com.snappydb.DB;

public class Constant
{
	// ������Դ��������ַ
	public static String RUQUESTURL = "http://app.ppmoney.com:8070/api/";

	// �Ƿ��¼�ɹ�
	public static boolean isLogin = false;

	public static boolean isOnline = false;
	public static int localVersion = 1;
	public static int serverVersion = 1;
	// ����������
	public static int totalAmount = -1;
	// ����DaoMaster
	public static DaoMaster daoMaster;
	
	public static String DbName = "TFB_DB";
	
	public static String DbFileName = "TFB_FILE_DB";
	// ����DaoSession
	public static DaoSession daoSession;
	//���ݿ����
	public static DB db;

	public static EventBus eventBus;
	
	// �ֻ��ͺ�
	public static String PhoneType;
	// �ֻ��ֱ���
	public static String PhoneResolution;
	// �ͻ�������
	public static String ChannelType;
	// �ͻ��˰汾��
	public static String Version;
	// ϵͳ�汾��
	public static String SystemVersion;
	// �豸id
	public static String DeviceId;
	// ip��ַ
	public static String IP;

}
