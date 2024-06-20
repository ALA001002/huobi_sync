# 工程简介

# 延伸阅读
## 1. 接口：/bgSlip/save 保存或者新增数据
```json
{
    "id":"1",//记录id，传入表示修改不传表示新增
    "symbol":"btcusdt", //交易对
    "addValue":50,  //增额
    "startTime":"1614153900000", //开始时间戳
    "intervalTime":5, //持续时间
    "openFlag":true //是否开启 默认为true 
}
```
## 2. 接口：/bgSlip/updateStatus 更新配置状态
```json
{
  "id":"1",//修改对象id
  "openFlag": true //是否开启
}
```
## 3. 接口地址：/bgSlip/page 列表接口
#### 请求
```json
{
    "pageNo":1, //页码
    "pageSize":1, //页长
    "symbol": "btcusdt" //查询交易对 支持模糊查询
}
```
#### 响应
```json
{
    "content": [
        {
            "id": 5,
            "startTime": 1617024900000,
            "endTime": 1617025200000,
            "openFlag": true,
            "addValue": 500.000000000000000000,
            "symbol": "btcusdt",
            "delFlag": false,
            "createTime": "2021-03-29T13:45:52.000+00:00",
            "updateTime": "2021-03-29T13:45:52.000+00:00",
            "addAmount": null,
            "intervalTime": 5
        }
    ],
    "pageable": {
        "sort": {
            "unsorted": false,
            "sorted": true,
            "empty": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 1,
        "paged": true,
        "unpaged": false
    },
    "last": false,
    "totalPages": 5, //总页数
    "totalElements": 5, //总条数
    "first": true,
    "size": 1,
    "number": 0,
    "sort": {
        "unsorted": false,
        "sorted": true,
        "empty": false
    },
    "numberOfElements": 1, 
    "empty": false
}
```
