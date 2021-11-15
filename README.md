# 神州云海IwaaSDK集成说明文档V1.0.3 #

## 一、概述 ##

本文档是在Android 5.1系统上，Android程序集成Iwaa客服机器人相关功能SDK的说明文档。  
Iwaa SDK分为两个SDK：IwaaSDK和IwaaAudioSDK。  
IwaaSDK通信相关，包括串口通信、webSocket通信、SocketServer服务；  
IwaaAudioSDK语音语义相关，包括语音识别、语音合成、语义。

## 二、集成说明 ##

Android开发工具为Android Studio,不建议使用Eclipse集成。
Iwaa SDK 适用Android SDK使用版本：4.4及以上。
Iwaa SDK是以aar文件打包，分为两个包语音语义SDK和机器人控制SDK。分别如下：

### （一）机器人控制SDK（IwaaSDK-release-2.1.aar） ###

机器人控制SDK包含了机器人的所有技能：运动控制、二代灯光控制、三代灯光控制、同步地图、模式切换、头部控制、身体转动、导航运动、手臂控制、机器人关机、机器人定时开机、打印功能、人脸识别开关、机器人校时功能、指纹信息增、删、查及其识别开关功能、广告机器人投射灯光开关和功放开关功能、其他扩展功能。

### （二）语音语义SDK(IwaaAudioSDK-release-2.0.aar) - 兼容四代客服机器人 ###

语音语义功能需要集成以下三个SDK  
1、IwaaAudioSDK-release-2.0.aar
集成了科大讯飞语音识别技术、语音合成技术、灵聚广义语义。

2、msc.jar（科大讯飞）
msc.jar和libmsc.so 为讯飞资源文件,需要在讯飞开放平台申请应用;如需使用,请更换为自己申请应用的msc.jar和libmsc.so。

3、AIEngine.aar（广义语义）
灵聚广义语义官方SDK。AppKey与该SDK没有绑定，如果需要使用,请到[灵聚](https://open.lingju.ai/)官网申请应用，在初始化时候传入自己申请的AppKey。

### （三）集成步骤说明 ###

1. 把aar文件 拷贝到 自己Android工程的app/libs目录下；  

2. 在app/build.gradle中添加如下	 

```
repositories {
	flatDir {
		dirs 'libs'
	}
}
dependencies {
	//机器人控制需要添加的aar
	implementation(name: 'IwaaSDK-release-2.1, ext: 'aar')；
	implementation 'com.alibaba:fastjson:1.1.54.android';
	//语音语义需要添加的aar
	implementation(name: 'IwaaAudioSDK-release-2.0, ext: 'aar')；
	implementation(name: 'AIEngine.aar', ext: 'aar')；
}
```

或者简洁版

```
dependencies {
	implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
	implementation 'com.alibaba:fastjson:1.1.68.android'
}
```

3. 在AndroidManifest.xml中添加权限，如有则忽略 

```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

# 三、功能说明 #

## (一) 机器人控制功能模块 ##

### 1、运动控制(RobotOperationMotion) ###

 运动相关API全部封装在RobotOperationMotion类，使用的时候可以通过工厂方法RobotOperationFactory.createRobotOperation(RobotOperationMotion.class)或者直接new 出对象，在调用相应的方法。	
（1）前进  
	goAhead()或者goAhead(RobotCallback robotCallback)  

（2）后退  
	goBack()或者goBack(RobotCallback robotCallback)  

（3）左转  
	turnLeft()或者turnLeft(RobotCallback robotCallback)  

（4）右转  
	turnRight()或者turnRight(RobotCallback robotCallback)  

（5）停止  
	stop()或者stop(RobotCallback robotCallback)  

（6）直接控制  
	直接用sendMotionMessage(float v, float w, RobotCallback robotCallback)方法。  
	v - 前进后退方向的值（-80 -- 80）,负值为后退，0为停止，正值为前进，绝对值越大前进后退速率越大;  
	w - 左转右转方向值（-80 -- 80）,负值为右转，0为停止，正值为左转，绝对值越大旋转速率越大;

### ~~2、二代灯光控制（RobotOperationLight）（已弃用）~~ ###

机器人灯光控制，控制机器人的眼睛、耳朵和身体上的灯光控制。  
（1）眼睛灯光  
	开启：eyeLightOn(int period, int lightValue)  或者  eyeLightOn(int period, int lightValue, RobotCallback robotCallback)；  
	关闭：eyeLightOff()或者eyeLightOff(RobotCallback robotCallback)  

（2）耳朵灯光  
	开启：earLightOn(int period, int lightValue)或者earLightOn(int period, int lightValue, RobotCallback robotCallback)  
	关闭：earLightOff()或者earLightOff(RobotCallback robotCallback)  

（3）身体灯光  
	开启：bodyLightOn(int period, int lightValue)或者bodyLightOn(int period, int lightValue, RobotCallback robotCallback)  
	关闭：bodyLightOff()或者bodyLightOff(RobotCallback robotCallback)  

（4）所有灯光  
	开启：allLightOn(int period, int lightValue)  
	关闭：allLightOff()

### 3、三代灯光控制（RobotOperationLight2） ###

第三代机器人的灯光控制，控制机器人上嘴唇和下嘴唇灯光。  
（1）上嘴唇灯光  

	开启：upperLipLightOn(int period, int lightValue)或者upperLipLightOn(int period, int lightValue, RobotCallback robotCallback)  
	关闭：upperLipLightOff()或者upperLipLightOff(RobotCallback robotCallback)  

（2）下嘴唇灯光  
	开启：lowerLipLightOn(int period, int lightValue)或者lowerLipLightOn(int period, int lightValue, RobotCallback robotCallback)  
	关闭：lowerLipLightOff(int period, int lightValue)或者lowerLipLightOn(int period, int lightValue, RobotCallback robotCallback)

```java
    /**
     * 打开上嘴唇灯光
     *
     * @param period        闪烁频率周期  0.0--10.0 单位：0.1秒，特殊值：101表示不闪烁。
     * @param lightValue    灯光亮度，0-100 百分比亮度
     */
    public void upperLipLightOn(int period, int lightValue)
        
    /**
     * 打开上嘴唇灯光
     *
     * @param period        闪烁频率周期  0.0--10.0 单位：0.1秒，特殊值：101表示不闪烁。
     * @param lightValue    灯光亮度，0-100 百分比亮度
     * @param robotCallback 回调函数
     */
    public void upperLipLightOn(int period, int lightValue, RobotCallback robotCallback) 
```

### 4、地图操作（RobotOperationMap） ###

（1）获取默认地图  
	setMapListener(MapListener mapListener) 和 getDefaultMapInfo()

（2）设置默认地图	  
	setDefaultMap(int mapId, @NonNull final DefaultMapListener defaultMapListener)  

（3）根据地图Name获取地图数据  
	getMapByName(String name, @NonNull final OnMapListener onMapListener)  

（4）获取地图数据  
	getAllMapInfos(@NonNull final MapInfoListener mapInfoListener)

（5）设置充电桩  
	setChargePoint(int mapID, double x, double y, double angle, ChargePointListener listener)  

（6）添加地图方案	  
	addMapPlan(@NonNull String planName, @NonNull String remark, AddMapPlanListener listener)  

（7）删除地图方案  
	deleteMapPlan(@NonNull int planId, DeleteMapPlanListener listener)  

（8）配置基站  
	navigationMode()或者navigationMode(RobotCallback robotCallback)

（9）建图（创建、取消和结束建图）  
	createMap(@NonNull int planId, String mapName, int operation, int x, int y, int offsetX, int offsetY, int angle, CreateMapListener listener)

（10）替换地图  
	replaceMap(@NonNull String mapFileName, String mapData, ReplaceMapListener listener)


（11）设置电子围栏  
	setElectronicFence(@NonNull int mapID, List<MapInfosResponse.MapPlanListBean.MapListBean.ElectricFenceLinesBean> electricFenceLines, List<MapInfosResponse.MapPlanListBean.MapListBean.ElectricFenceRectsBean> electricFenceRects, ElectronicFenceSettingListener listene)

###  5、模式切换（RobotOperationMode） ###

机器人模式切换，包含四种模式：手动(服务)、自由、充电、导航  
（1）手动模式  
	manualMode()或者manualMode(RobotCallback robotCallback)  

（2）自由模式	  
	freeMode()或者freeMode(RobotCallback robotCallback)  

（3）充电模式  
	chargeMode()或者chargeMode(RobotCallback robotCallback)  

（4）导航模式  
	navigationMode()或者navigationMode(RobotCallback robotCallback)

### 6、头部控制（RobotOperationHead） ###

控制机器人头部运动的类。包含水平方向转动和垂直方向转动。其中三代艾娃客服机器人无垂直方向的转动。  
（1）普通转动  
	参入指定的参数，灵活的转动头部。sendHeadMessage(int horizontalAngle, int horizontalSpeed,int verticalAngle,int verticcalSpeed,RobotCallback robotCallback)  

（2）水平转动  
	horizontalRotateTo(int horizontalAngle, RobotCallback robotCallback)或者horizontalRotateTo(int horizontalAngle)  

（3）垂直转定  
	verticalRotateTo(int horizontalAngle, RobotCallback robotCallback) 或者verticalRotateTo(int horizontalAngle)  

（4）头部回正
	reset(RobotCallback robotCallback)或者reset();

### 7、身体转动（RobotOperationRotate） ###

控制机器人身体转动的类。  
（1）普通转动  
	指定机器朝某一方向，指定速度，旋转指定角度。sendBodyRotate(int direction,int angle,int speed,RobotCallback robotCallback);或者sendBodyRotate(int direction,int angle,int speed);  

（2）左转  
	机器人以默认速度左转90度，turnLeft()或者turnLeft(RobotCallback robotCallback);  

（3）右转  
	机器人以默认速度右转90度turnRight()或者turnRight(RobotCallback robotCallback);  

（4）转身  
	机器人以默认速度右转180度turnBody()或者turnBody(RobotCallback robotCallback);  

（5）转圈  
	机器人以默认速度右转360度turnAround()或者turnAround(RobotCallback robotCallback);

### 8、导航运动（RobotOperationNavi） ###

机器人导航运动的类，导航到指定位置。  
（1）普通导航  
	sendNaviMessage(float x, float y, int angle, float speed, int navigationType, final RobotCallback robotCallback)或者sendNaviMessage(float x, float y, int angle, float speed, int navigationType)  

（2）遇到障碍避障  
	机器人导航到某地，中间遇到障碍物避障后继续导航naviObstacleAvoid(float x,float y,int ange,float speed)  

（3）遇到障碍停止  
	机器人导航到某地，中间遇到障碍物后停止导航naviObstacleStop(float x,float y,int ange,float speed)

### 9、关机（RobotOperationShutdown） ###

控制机器人系统的断电关机。  
（1）默认关机  
	sendShutdown() 或者sendShutdown(RobotCallback robotCallback)默认关机，延迟10s。  

（2）延迟关机  
	sendShutdown(int delay) 或者sendShutdown(int delay, final RobotCallback robotCallback)延迟多少秒关机。最少10s。

### 10、手臂控制（RobotOperationArm） ###

控制机器人手臂的运动，第三代开始才添加了机械臂。  
（1）普通转动  
	sendArmMessage(int cmd,int speed,int angle,RobotCallback robotCallback)  

（2）左臂转动  
	leftArm(int speed,int angle) 或者leftArm(int speed,int angle,RobotCallback robotCallback)  

（3）右臂转动  
	rightArm(int speed,int angle) 或者rightArm(int speed,int angle,RobotCallback robotCallback)  

（4）手臂复位  
	reset()

### 11、打印控制（RobotOperationPrint） ###

调用机器人自带的打印机功能。发送文本内容打印。sendPrint(String message,RobotCallback robotCallback) 或者sendPrint(String message)

### 12、人脸识别开关（FaceRecognitionSwitch） ###

控制人脸识别的开关，打开后工控机会推送人脸识别数据，注册FaceResultListener后会获取到人脸识别的结果。  
（1）打开  
	open(String ip)或者open(String ip,RobotCallback robotCallback)；  

（2）关闭  
	close() 或者 close(RobotCallback robotCallback)；

### 13、定时开机（RobotOperationBoot） ###

工控机的定时开机功能。  

（1）打开  
	open(int hour,int min,int second)或者open(int hour,int min,int second,RobotCallback robotCallback)；  

（2）关闭  
	close() 或者 close(RobotCallback robotCallback)；

### 14、校时（RobotOperationTime） ###

给机器人工控机校时 时间格式：yyyy-MM-dd HH:mm:ss。calibration() 或者calibration(RobotCallback robotCallback) 

### 15、指纹功能（RobotOperateFingerprint） ###

包含有指纹信息增、删、查及其识别开关功能。  
（1）创建指纹信息  
	create(String name, String id) 或者create(String name, String id, RobotCallback robotCallback)  

（2）删除指纹信息  
	delete(String id) 或者delete(String id, RobotCallback robotCallback)  

（3）查询所有指纹信息  
	queryAll(RobotOperateFingerprint.QueryFingerprintListener queryFingerprintListener)  

（4）指纹识别开关  
	recognizeSwitch(boolean isOpen)或者recognizeSwitch(boolean isOpen, RobotCallback robotCallback)

### ~~16、广告机器人（RobotOperateAd)~~ ###

包含有指纹信息增、删、查及其识别开关功能。  
（1）打开广告投射灯开关  
	openProjector() 或者openProjector(RobotCallback robotCallback)  

（2）关闭广告投射灯开关  
	closeProjector() 或者closeProjector(RobotCallback robotCallback)  

（3）打开功放开关  
	openPowerAmplifier() 或者openPowerAmplifier(RobotCallback robotCallback)  

（4）关闭功放开关  
	closePowerAmplifier()或者closePowerAmplifier(RobotCallback robotCallback)  

### 17、其他扩展操作（RobotOperationExtend） ###

临时扩展协议，发送json格式文件给工控机协议。
sendExtendMessage(String json) 或者
sendExtendMessage(String json，RobotCallback robotCallback)

## (二) 机器人数据上报 ##

### 1、机器人的状态信息（RobotStatusListener） ###

 包含传感器信息，运动状态信息和导航状态信息的推送。注册RobotStatusListener监听器，获取机器人状态信息结果。  
（1）传感器信息  
包含电池信息、UWB信息、雷达信息、激光信息已经轮子的速度、急停按钮等信息；onSensorStatusResponse(SensorStatusResponse sensorStatusResponse)  

（2）运动状态信息  
包含当前模式、头部角度、灯光、机器人当前坐标等信息；onRobotMotionStatusResponse(MotionStatusResponse motionStatusResponse)  

 （3）导航信息  
机器人起始坐标位置、路线规划信息；onRobotMotionStatusResponse(MotionStatusResponse motionStatusResponse)

### 2、身份证信息（RobotIDReadListener） ###

身份证阅读器读取到身份证之后，推送的身份证信息。
onReadIDResponse(IDCardResponse idResponse) 

### 3、银行卡或者条形码信息（RobotOtherCardListener） ###

银行卡阅读器读取到银行卡，推送的银行卡信息。条形码阅读器读取到的条形码，推送过来的条形码信息。onReadOtherCard(int type, String number)

### 4、人脸识别结果信息（FaceResultListener） ###

人脸识别信息的推送。onFaceResult(FaceResultResponse faceResultResponse)

### 5、指纹录入监听器（FingerprintInputListener） ###

指纹创建时候，需要做指纹录入，录入时候有录入信息推送。onFingerprintInput(FingerprintInputResponse fingerprintInputResponse)

### 6、指纹识别监听器（FingerprintRecognizeListener） ###

指纹识别时候，推送识别信息。onFingerprintRecognize(FingerprintResponse.FingerInfo fingerInfo)

### 7、其他扩展推送信息（ExtUploadListener） ###

onExtUpload(String json) 

## (三) 六麦克风阵列模块 ##

### 1、六麦简介与唤醒 ###

六麦即六麦克风阵列，是科大讯飞的圆形拾音模块，0度、60度、120度、180度、240度、300度方位设置一个麦克风，形成麦克风阵列。本公司艾娃机器人正前方默认是0度方向。每次唤醒，六麦会经过串口数据过来，其中包含当前声源的角度值， 比如：WAKE UP!angle:90 表示当前声源是在90度方向。

### 2、API接口使用步骤（SerialPortTools） ###

（1）串口初始化  
init(String devPath, int baudRate, boolean autoRecover)  
SerialPortTools.init("/dev/ttyS3", 115200, true);//串口设置地址（/dev/ttyS3），串口通讯波特率（115200）。  

（2）设置相应的监听器  
根据自己的需求实现SerialPortDataListener, SerialPortStateListener这两个接口并注册，如下  
SerialPortTools.addDataListener(this);//串口的数据监听器  
SerialPortTools.addStateListener(this);//串口的状态监听器  

（3）获取串口数据  
实现SerialPortStateListener接口 onSerialPortDataReceived(byte[] data)会返回data数据，可以解析获取五麦发送来的角度值。  
（4）发送数据到串口  
SerialPortTools.send(byte[] data)  

（5）发送数据指令到串口
SerialPortTools.sendCmd(String cmd)  
发送数据增强拾音方向，0,60,120,180,240,300度分别是：BEAM 0  BEAM 1  BEAM 2 BEAM 3,BEAM 4,BEAM 5
增强0度方向例子：SerialPortTools.sendCmd("BEAM 0");  

（6）反注册监听器和关闭串口  
SerialPortTools.removeStateListener(this);  
SerialPortTools.removeDataListener(this);  
SerialPortTools.close();

## (四) 语音语义功能模块 ##

语音功能，包括语音识别和语音合成。语音功能全部由AudioEngineProxy单例处理。

### 1、AudioEngineProxy类 ###

（1）绑定和解绑代理服务  
绑定服务的实现语音功能的第一步，即bindAudioEngineService(Context context)。  
或者bindAudioEngineService(Context context,AudioEngineProxy.builder builder)。  
builder.buildIflytekAppId("xf_appid");//传入科大讯飞AppId  
builder.buildLingjuAppKey("lingju_appid");//传入灵聚语义AppKey  
默认会使用本公司申请的AppId和AppKey，也有可以自己重新申请。如果替换语音相关的SDK则需要删除IflytechSDK-release-1.0.aar文件，以免冲突。另外需要对应科大讯飞appid的jar包和so文件集成到程序中，具体步骤请参考科大讯飞官方集成说明文档。
不需要语音服务之后需要解绑，即：unbindAudioEngineService  

（2）语音识别  
开始识别：startRecognize()  
设置识别监听器：setRecognizeLister(RecognizeListener recognizeLister)  
结束识别：stopRecognize()  

（3）语义结果回调  
设置语义监听器。setResponseListener(ResponseListener responseListener)  

（4）语音合成  
合成一段文：startSpeakAbsolute(String text)  
设置语音合成监听器： setSynthesizerListener(SynthesizerListener synthesizerListener)  
结束合成：stopSpeakingAbsolte()  

（5）设置连续识别超时时间  
setRecognizeTimeout(int timeout),默认时间2min。  

（6）重置语音识别开始的时间  
resetRecognizeBeginTime()，调用此方法后连续识别的超时时间会重新计时。  

（7）设置语音识别时候的音量门限值  
setVolumeThreshold(int volumeThreshold)，（0-30）范围。表示在识别时候需要最大音量达到该值才会有语音识别结果返回。  

（8）设置识别的参数  
setRecognizerParam(String key,String value)，具体的参数请参考科大讯飞官方文档和Demo。  

~~（9）设置机器人属性  
saveRobotProperty(Robot robot)~~  

（10）设置机器人地理位置信息  
saveRobotLocation(Location location)  

~~（11）添加可以识别的词语  
addUserWords(String[] words)~~  

（12）Media播放音频文件相关  
播放SD卡目录下的音频文件：playSdCard(String path)  

播放assets目录下的音频文件：playAsset(String name)  

异步准备在线音乐列表：prepareOnlineMusic(List<SongEntity> list)  

播放或暂停：playPause()  

停止播放：stopMedia()  

跳到指定进度：seekTo(int progress)  

开始播放：playMedia()  

暂停播放：pauseMedia()  

获取音频文件时间长度:getDuration()  

获去当前播放的进度：getCurrentPosition()  

是否正在播放：isPlaying()  

~~(13)设置语义类型  
setSemanticsMode(int mode)默认包含两种语义：0：科大讯飞语义；1：灵聚语义。~~  

### 2、语音识别监听器RecognizeListener ###

开始识别： onRecoginzeBegin();  

识别时候音量的变化(0-30)：onRecoginzeVolumeChanged(int vol);  

识别结束：void onRecoginzeEnd();  

识别结果的返回：boolean onRecoginzeResult(String result);  

根据返回的boolean 是否需要语义返回。true表示需要语义返回。  

识别出错：void onRecognizeError(int e);  

识别超时（默认120秒）：void onRecognizeTimeout();  

### 3、语音合成监听器SynthesizerListener ###

合成初始化：onSynthesizerInited(int errorCode);  

合成完成：onSynthersizeCompleted(int errorCode);  

开始说话：onSynthersizeSpeakBegin();  

合成出错：onSynthersizeError(int errorCode);  

合成说话时候的进度（0-100）onSpeakProgress(int p);  

### 4、语义返回监听器ResponseListener ###

语义返回：onRobotResponse(String text)  

音乐返回：onPlayResponse(List<SongEntity> songEntity)  

### 5、两个广播 ###

（1）playAsset播放完成后的广播：AudioEngineProxy.ACTION_ASSET_COMPLETION  

（2）playList 准备完成后的广播：AudioEngineProxy.ACTION_MUSIC_PREPARED
