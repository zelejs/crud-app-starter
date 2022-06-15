import React, { useState } from 'react';
import { Button } from 'antd';
import { message as msg } from 'antd';
import requestApi from '@/utils/promiseAjax';
// import download from '@/utils/downloadFile';
import { get as getEndpoint } from 'zero-element/lib/utils/request/endpoint';

import { download } from 'zero-element/lib/utils/request';

/**
 * 对接 列表项 下载 适用
 */

export default function (props) {
  const { options = {}, formdata } = props;
  const { title, fileName, downloadMethod = 'get', message, } = options;
  const [loading, setLoading] = useState(false);

  const [fName, setFileName] = useState('');

  function handleClick() {

    setLoading(true);

    // const apiUrl = `${getEndpoint()}${API}`;
    const api = formdata.url;
    const queryData = {}

    console.log('api = ', api)

    // download(api, queryData)
    //   .then(resp => {
    //     if (resp && resp.code === 200) {
    //     }
    //   })
    //   .finally(_ => {
    //     setLoading(false);
    //   })

    download(api, {}).then(_ => {
      if (message) {
        msg.success(message);
      }
    })
    .finally(_ => {
      setLoading(false);
    })

  }

  return <Button
    onClick={handleClick}
    loading={loading}
  >
    {title}
  </Button>;
}