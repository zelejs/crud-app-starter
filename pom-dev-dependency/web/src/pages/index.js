import React from 'react';

import StandalonePage from '@/pages/Standalone'
import useTokenRequest from 'zero-element-boot/lib/components/hooks/useTokenRequest';

export default function index(props) {

  let api = '/dev/dependency/json';

  if (process.env.NODE_ENV === 'development') {
    api = `http://192.168.3.121:8080${api}`;
  }

  const [ data ] = useTokenRequest({api});

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
  dataX.push({items:newData})
  
  return (
        data.length > 0 ? (
          <StandalonePage {...props} data={dataX}/>
        ):<></>
  )
}