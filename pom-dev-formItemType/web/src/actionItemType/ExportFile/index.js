import React, { useState } from 'react';
import { Button } from 'antd';
import { message as msg } from 'antd';
import download from '@/utils/downloadFile';

export default (props) => {

  const { title, options, className, location } = props;
  const { API, query = {}, method='get' } = options;

  const [loading, setLoading] = useState(false);

  function handleClick() {

    if (API) {

      setLoading(true);
      
      // const apiUrl = `${getEndpoint()}${API}`;

      download(API, {...query}, {method: method})
      .then(resp => {
        if (resp && resp.code === 200) {
        }
      })
      .finally(_ => {
        setLoading(false);
      })

    }else{
      msg.error("export API is not null")
    }
  }

  return (
    <Button onClick={handleClick} className={className} loading={loading}>
      {title}
    </Button>
  )
    
}