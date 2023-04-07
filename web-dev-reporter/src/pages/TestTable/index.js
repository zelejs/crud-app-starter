import React, { useState, useEffect } from 'react';
import Table from '@/components/Table'
import promiseAjax from '@/components/promiseAjax'

export default function Index(props) {

  let api = '/api/testData/test'

  const [items, setItems] = useState('')

  useEffect(_ => {
    getApiUrl()
  }, [])

  function getApiUrl() {
    const queryData = {};
    promiseAjax(api, queryData).then(resp => {
      if (resp && resp.code === 200) {
        setItems(resp.data)
      console.log('data =',resp.data)
      } else {
        console.error("获取api path 数据失败")
      }
    });
  }

  const headers = [
    { "Host": '主机' },
    { "Time_": "时间" },
    { "BlockIO": "磁盘 I/O" },
    { "CPUPerc": "处理器" },
    { "Container": "容器" },
    { "ID": "容器 ID" },
    { "MemPerc": "内存使用率" },
    { "MemUsage": "内存使用量" },
    { "Name": "容器名称" },
    { "NetIO": "网络 I/O" },
    { "PIDs": "PID 号" }
  ]

  return (
    items && items.length > 0 ?
      <Table datas={items} headers={headers} /> :
      <></>

  )
}