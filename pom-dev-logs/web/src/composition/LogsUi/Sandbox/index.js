import React, { useState, useEffect } from 'react';
import LogsUi from '@/composition/LogsUi'
import useTokenRequest from 'zero-element-boot/lib/components/hooks/useTokenRequest';

export default function index (props) {

  const api = `/dev/logs/json`
  const [data] = useTokenRequest({ api })

  const newData = []
  data.map(item=>{
    const newItem = {}
    newItem.id = index + 1;
    newItem.value = item;
    newData.push(newItem)
  })

  const dataX = []
  dataX.push({ items: newData })

  return (
    <>
      { dataX && dataX[0].items && dataX[0].items.length > 0 ? (
          <LogsUi data={dataX} />
      ):<></>}
    </>
  )

}