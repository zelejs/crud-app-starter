import React, { useState, useEffect } from 'react';
import DevLogsPage from '@/pages/dev'
import useTokenRequest from 'zero-element-boot/lib/components/hooks/useTokenRequest';
import { setEndpoint, setToken } from 'zero-element-boot/lib/components/config/common';
export default function index (props) {


  if (process.env.NODE_ENV == 'development') {
    setEndpoint('https://www.metagugu.net');
  }
  let api = '/dev/logs/json';


  const requestData = {
    // pattern: 'access.log',
    // filter: 'WARN',
    // N: "10"
  }
  const [data] = useTokenRequest({ api, requestData });

  const newData = []
  data.map((item, index) => {
    // if(item.indexOf('.jar') > -1){
    const newItem = {}
    newItem.id = index + 1;
    newItem.value = item;
    newData.push(newItem)
    // }
  })

  const dataX = []
  dataX.push({ items: newData })

  return (
    data.length > 0 ?
      (
        <DevLogsPage {...props} data={dataX} />
      ) : <></>
  )

}