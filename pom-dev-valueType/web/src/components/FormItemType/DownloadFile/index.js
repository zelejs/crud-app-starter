import React, { useState } from 'react';
import { Button } from 'antd';
import { message as msg } from 'antd';
import requestApi from '@/utils/promiseAjax';
import download from '@/utils/downloadFile';
import { get as getEndpoint } from 'zero-element/lib/utils/request/endpoint';

/**
 * 对接 C# api 适用
 */

export default function (props) {
  const { options = {}, formdata } = props;
  const { title, API, fileName, downloadMethod = 'get', message,  } = options;
  const [loading, setLoading] = useState(false);

  const [fName, setFileName] = useState('');

  function handleClick() {

    if (API) {
      setLoading(true);

      const list = [];
      Object.keys(formdata).forEach(key => {
        if(formdata[key]){
          setFileName(formdata[key].fileName);
          list.push(formdata[key]);
        }
      });
      
      // const apiUrl = `${getEndpoint()}${API}`;

      const queryData = {
        fileName: fName,
        ctdListString: JSON.stringify(list)
      }

      // console.log('queryData = ', queryData.ctdListString)

      download(API, queryData, {method:'post'})
      .then(resp => {
        if (resp && resp.code === 200) {
        }
      })
      .finally(_ => {
        setLoading(false);
      })

    }
  }

  return <Button
    onClick={handleClick}
    loading={loading}
  >
    {title}
  </Button>;
}