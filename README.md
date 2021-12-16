# 企业可信数字化服务器（ETDS硬件服务）
## 1. 部署说明
### 前置条件/依赖
|  前置依赖   | 提供者  |
|  :----:  | :----:  |
| DTC核心组件  | 谭伟 |
| DTID核心组件  | 谭伟 |
| MQ服务组件  | 符杰、熊林 |
| ETDS的定制层服务  | 田仁山 |
| ETDS的TDaaS服务  | 柯博 |
| 授权中心服务  | 符杰 |
| openJDK11  | 开源 |
| MySQL8.0.27  | 开源 |

### 部署操作步骤
- 准备MySQL数据库的ETDS服务脚本、执行
- 执行准备ETDS服务的jar包、启动
- 初始化完成后默认内置管理员 账户:admin  密码:000000
- MySQL数据库脚本在resources/initsql目录下**init.sql**

### 激活ETDS的流程
- 通过初始化管理员admin登陆ETDS服务进入激活界面
- 通过TDaaS界面上获取到（由鉴权中心服务提前生成的）
- 最后在ETDS服务的激活界面输入**激活码**和**ETDS服务的后端地址**进行激活即可
- ⚠️ 激活码:由TDaaS界面提供 
- ⚠️ ETDS服务的**后端地址**:地址格式**【http:ip:port】**或**【http:域名:port】**
## 2. 接口文档

### 2.1 供ETDS的前端调用
### 登陆


**接口地址**:`/api/v1/account/login`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "account": "admin",
  "password": "000000"
}
```

**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|loginDTO|登陆/修改密码的DTO对象|body|true|登陆/修改密码的DTO对象|登陆/修改密码的DTO对象|
|&emsp;&emsp;account|账户||true|string||
|&emsp;&emsp;password|密码||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200000|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|string||
|message|提示信息|string||
|payload|返回数据json|json||


**响应示例**:
```javascript
{
  "code": "200000",
  "message": "操作成功!",
  "payload": {
    "active": true,
    "company": {
      "companyDtid": "dtid:dtca:3RF8yxtBVXCpS8DGdT1cYUEFKonr",
      "createTime": 1639121574,
      "companyName": "贵州省安顺市市场主体柯博测试企业1",
      "description": "产品测试6",
      "etdsCode": "c6pg4d9e4mv9tpv3ebj0",
      "state": 0,
      "etdsName": "kb-fj-xl-测试的etds——6",
      "etdsUrl": "http://192.168.1.44:18008"
    },
    "account": "admin",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhZG1pbiIsImV4cCI6MTY0MDIzMTczNSwiaWF0IjoxNjM5NjI2OTM1fQ.Y1KbQ4nL8GiYwh14MsB027KYikqEdEficn8YyXzCu_Q"
  }
}
```


## 刷新


**接口地址**:`/api/v1/account/refresh`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 修改密码


**接口地址**:`/api/v1/account/update-pwd`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "account": "",
  "oldPassword": "",
  "password": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|updatePasswordDTO|登陆/修改密码的DTO对象|body|true|登陆/修改密码的DTO对象_1|登陆/修改密码的DTO对象_1|
|&emsp;&emsp;account|账户||true|string||
|&emsp;&emsp;oldPassword|老密码||true|string||
|&emsp;&emsp;password|密码||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```
### 2.2 供ETDS的定制层调用
###
## 申请授权


**接口地址**:`/api/v1/custom/apply-auth`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "cc": [
    {
      "etds": "",
      "tdaas": ""
    }
  ],
  "desc": "",
  "expiration": 0,
  "serialNumber": "",
  "to": {
    "etds": "",
    "tdaas": ""
  }
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|applyAuth|申请授权的dto/定制层传给etds的|body|true|申请授权的dto/定制层传给etds的|申请授权的dto/定制层传给etds的|
|&emsp;&emsp;cc|数据供应方的list||true|array|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||
|&emsp;&emsp;desc|描述信息||true|string||
|&emsp;&emsp;expiration|过期时间||true|integer(int64)||
|&emsp;&emsp;serialNumber|序列号||true|string||
|&emsp;&emsp;to|授权方||true|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||boolean||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": true
}
```


## 申请数据


**接口地址**:`/api/v1/custom/apply-data`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "dtc": "",
  "fragment": "",
  "from": {
    "etds": "",
    "tdaas": ""
  },
  "httpMeta": {
    "body": "",
    "header": {},
    "method": "",
    "path": "",
    "query": ""
  },
  "serialNumber": "",
  "to": {
    "etds": "",
    "tdaas": ""
  }
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|applyData|DataRequestBizData 数据请求或者控制的DTC BizData|body|true|DataRequestBizData 数据请求或者控制的DTC BizData|DataRequestBizData 数据请求或者控制的DTC BizData|
|&emsp;&emsp;dtc|授权DTC的ID||true|string||
|&emsp;&emsp;fragment|Fragment.Wraper() 序列化的数据包数据包，这里的包一定是控制包，当HttpMeta为空时，才会认为需要处理数据的控制包控制功能包括：部分重传、全部重传、传输完毕确认||true|string||
|&emsp;&emsp;from|请求数据者||false|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||
|&emsp;&emsp;httpMeta|// HTTP 请求元数据，如果HttpMeta不为nil，那么它是一个请求，不处理控制包||true|HttpMeta|HttpMeta|
|&emsp;&emsp;&emsp;&emsp;body|请求数据||true|string||
|&emsp;&emsp;&emsp;&emsp;header|请求头||true|object||
|&emsp;&emsp;&emsp;&emsp;method|请求方法，只允许GET/POST||true|string||
|&emsp;&emsp;&emsp;&emsp;path|请求Path，要结合适配层定义好的应用层地址，一定是'/'开头||true|string||
|&emsp;&emsp;&emsp;&emsp;query|请求Query||true|string||
|&emsp;&emsp;serialNumber|请求序列号||true|string||
|&emsp;&emsp;to|向谁请求数据||false|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 接收数据


**接口地址**:`/api/v1/custom/receive-data`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "bizHeader": {
    "fragment": "",
    "header": {},
    "status": 0
  },
  "chunk": "",
  "dtc": {
    "code": 0,
    "desc": "",
    "dtc": ""
  },
  "from": {
    "etds": "",
    "tdaas": ""
  },
  "serialNumber": "",
  "to": {
    "etds": "",
    "tdaas": ""
  }
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|responseData|数据响应DTC的bizData|body|true|数据响应DTC的bizData/数据响应DTC的bizData|数据响应DTC的bizData/数据响应DTC的bizData|
|&emsp;&emsp;bizHeader|整个请求的响应头||true|BizHeader|BizHeader|
|&emsp;&emsp;&emsp;&emsp;fragment|// Fragment.Wraper() 序列化的数据包，// 这个包一定是数据包的头包，应为控制包没有HTTP请求||true|string||
|&emsp;&emsp;&emsp;&emsp;header|||false|object||
|&emsp;&emsp;&emsp;&emsp;status|||false|integer||
|&emsp;&emsp;chunk|响应的数据:这个数据包一定是Chunk，使用Chunk.Wrapper()获得||true|string||
|&emsp;&emsp;dtc|授权DTC的ID||true|AuthState|AuthState|
|&emsp;&emsp;&emsp;&emsp;code|授权状态码||true|integer||
|&emsp;&emsp;&emsp;&emsp;desc|授权状态描述||true|string||
|&emsp;&emsp;&emsp;&emsp;dtc|授权凭证的ID||true|string||
|&emsp;&emsp;from|响应数据者||true|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||
|&emsp;&emsp;serialNumber|请求序列号||true|string||
|&emsp;&emsp;to|向谁响应数据||true|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 接收数据的统计信息


**接口地址**:`/api/v1/custom/receive-statistics-data`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "authDtc": "",
  "authStatus": 0,
  "authStatusDesc": "",
  "chunkLength": 0,
  "chunkSize": 0,
  "firstResponse": 0,
  "from": {
    "etds": "",
    "tdaas": ""
  },
  "lastResponse": 0,
  "requestHttpMeta": {
    "body": "",
    "header": {},
    "method": "",
    "path": "",
    "query": ""
  },
  "requestedAt": 0,
  "responseHttpMeta": {
    "contentLength": 0,
    "header": {},
    "status": 0
  },
  "retries": 0,
  "serialNumber": "",
  "to": {
    "etds": "",
    "tdaas": ""
  }
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reportDTO|reportDTO|body|true|ReportDTO|ReportDTO|
|&emsp;&emsp;authDtc|||false|string||
|&emsp;&emsp;authStatus|||false|integer(int32)||
|&emsp;&emsp;authStatusDesc|||false|string||
|&emsp;&emsp;chunkLength|||false|integer(int32)||
|&emsp;&emsp;chunkSize|||false|integer(int64)||
|&emsp;&emsp;firstResponse|||false|integer(int64)||
|&emsp;&emsp;from|||false|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||
|&emsp;&emsp;lastResponse|||false|integer(int64)||
|&emsp;&emsp;requestHttpMeta|||false|HttpMeta|HttpMeta|
|&emsp;&emsp;&emsp;&emsp;body|请求数据||true|string||
|&emsp;&emsp;&emsp;&emsp;header|请求头||true|object||
|&emsp;&emsp;&emsp;&emsp;method|请求方法，只允许GET/POST||true|string||
|&emsp;&emsp;&emsp;&emsp;path|请求Path，要结合适配层定义好的应用层地址，一定是'/'开头||true|string||
|&emsp;&emsp;&emsp;&emsp;query|请求Query||true|string||
|&emsp;&emsp;requestedAt|||false|integer(int64)||
|&emsp;&emsp;responseHttpMeta|||false|ResponseHttpMeta|ResponseHttpMeta|
|&emsp;&emsp;&emsp;&emsp;contentLength|||false|integer||
|&emsp;&emsp;&emsp;&emsp;header|||false|object||
|&emsp;&emsp;&emsp;&emsp;status|||false|integer||
|&emsp;&emsp;retries|||false|integer(int32)||
|&emsp;&emsp;serialNumber|||false|string||
|&emsp;&emsp;to|||false|Address|Address|
|&emsp;&emsp;&emsp;&emsp;etds|etds的唯一码||true|string||
|&emsp;&emsp;&emsp;&emsp;tdaas|tdaas数字身份||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```
## 提供给定制层获取ETDS的唯一码和数字身份


**接口地址**:`/api/v1/custom/find-etds-info`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```
### 2.3 供鉴权中心调用


## 供给鉴权中心同步ETDS信息到ETDS中


**接口地址**:`/api/v1/etds/sync-etds-info`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|etdsInfo|etdsInfo|body|true|||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```
### 2.3 供TDaaS服务调用的


## 提供给tdaas确认激活


**接口地址**:`/api/v1/tdaas/confirm-activate-etds`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "etdsCode": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|confirmActivateEtdsDTO|confirmActivateEtdsDTO|body|true|ConfirmActivateEtdsDTO|ConfirmActivateEtdsDTO|
|&emsp;&emsp;etdsCode|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result«Etds对象»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||Etds对象|Etds对象|
|&emsp;&emsp;activationCode|激活码|string||
|&emsp;&emsp;appKey|应用key|string||
|&emsp;&emsp;appSecret|密钥|string||
|&emsp;&emsp;companyDtid|企业dtid|string||
|&emsp;&emsp;companyName|公司名称|string||
|&emsp;&emsp;createTime|创建时间|integer(int64)||
|&emsp;&emsp;description|描述|string||
|&emsp;&emsp;etdsCode|Etds唯一标识码|string||
|&emsp;&emsp;etdsName|etds名字|string||
|&emsp;&emsp;etdsUrl|etds url（ip+port）|string||
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;license|license|string||
|&emsp;&emsp;state|状态[0 : 正常  1:暂停]|integer(int32)||
|&emsp;&emsp;updateTime|更新时间|integer(int64)||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {
		"activationCode": "",
		"appKey": "",
		"appSecret": "",
		"companyDtid": "",
		"companyName": "",
		"createTime": 0,
		"description": "",
		"etdsCode": "",
		"etdsName": "",
		"etdsUrl": "",
		"id": 0,
		"license": "",
		"state": 0,
		"updateTime": 0
	}
}
```


## 提供给tdaas的心跳检测接口


**接口地址**:`/api/v1/tdaas/etds-heartbeat`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result«HeartbeatResponse»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||HeartbeatResponse|HeartbeatResponse|
|&emsp;&emsp;etdsCode||string||
|&emsp;&emsp;status|tdaas管控etds的状态[0:正常  1:暂停]|string||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {
		"etdsCode": "",
		"status": ""
	}
}
```


## 恢复ETDS


**接口地址**:`/api/v1/tdaas/recover`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "companyDtid": "",
  "etdsCode": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|operateETDSDTO|操作etds的dto|body|true|操作etds的dto|操作etds的dto|
|&emsp;&emsp;companyDtid|公司数字身份||true|string||
|&emsp;&emsp;etdsCode|etds的唯一code||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 恢复ETDS


**接口地址**:`/api/v1/tdaas/recover`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "companyDtid": "",
  "etdsCode": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|operateETDSDTO|操作etds的dto|body|true|操作etds的dto|操作etds的dto|
|&emsp;&emsp;companyDtid|公司数字身份||true|string||
|&emsp;&emsp;etdsCode|etds的唯一code||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 提供给tdaas恢复某个授权凭证的接口


**接口地址**:`/api/v1/tdaas/recover-auth-dtc`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "claimId": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authDtcDTO|authDtcDTO|body|true|AuthDtcDTO|AuthDtcDTO|
|&emsp;&emsp;claimId|凭证id||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 提供给tdaas暂停某个授权凭证的接口


**接口地址**:`/api/v1/tdaas/stop-auth-dtc`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "claimId": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|authDtcDTO|authDtcDTO|body|true|AuthDtcDTO|AuthDtcDTO|
|&emsp;&emsp;claimId|凭证id||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```


## 提供给tdaas同步pubKey和priKey到etds中的接口


**接口地址**:`/api/v1/tdaas/sync-private-key`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "companyDtid": "",
  "privateKey": "",
  "publicKey": "",
  "safeCode": ""
}
```


**请求参数**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|syncPrivateKeyDTO|syncPrivateKeyDTO|body|true|SyncPrivateKeyDTO|SyncPrivateKeyDTO|
|&emsp;&emsp;companyDtid|公司数字身份||true|string||
|&emsp;&emsp;privateKey|私钥||true|string||
|&emsp;&emsp;publicKey|公钥||false|string||
|&emsp;&emsp;safeCode|安全码||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||string||
|message||string||
|payload||object||


**响应示例**:
```javascript
{
	"code": "",
	"message": "",
	"payload": {}
}
```
### ETDS和定制层的交互设计图
![avatar](./images/定制层和ETDS之间数据的流转.png)
### ETDS和TDaaS的交互设计图
![avatar](./images/TDaaS和ETDS之间的凭证交互流程.png)

