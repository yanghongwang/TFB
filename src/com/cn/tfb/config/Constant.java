package com.cn.tfb.config;

import com.cn.tfb.dao.DaoMaster;
import com.cn.tfb.dao.DaoSession;
import com.cn.tfb.event.EventBus;
import com.snappydb.DB;

public class Constant
{
	// 请求资源服务器地址
	public static String RUQUESTURL = "http://www.tfbpay.cn/tfb_test/sever/getapi.php";

	// 是否登录成功
	public static boolean isLogin = false;

	public static boolean isOnline = false;
	public static int localVersion = 1;
	public static int serverVersion = 1;
	// 缓存总收益
	public static int totalAmount = -1;
	// 缓存DaoMaster
	public static DaoMaster daoMaster;

	public static String DbName = "TFB_DB";
	// 请求动态码
	public static String REQTOKEN = "Req_Token";

	public static String DbFileName = "TFB_FILE_DB";
	// 缓存DaoSession
	public static DaoSession daoSession;
	// 数据库操作
	public static DB db;

	public static EventBus eventBus;
	// APP类型
	public static String APPTYPE = "1";

	// 手机型号
	public static String PhoneType;
	// 手机分辨率
	public static String PhoneResolution;
	// 客户渠道号
	public static String ChannelType;
	// 客户端版本号
	public static String Version;
	// 系统版本号
	public static String SystemVersion;
	// 设备id
	public static String DeviceId;
	// ip地址
	public static String IP;

}
