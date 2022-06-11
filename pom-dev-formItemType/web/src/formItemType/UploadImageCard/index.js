import React, { useState } from 'react';
import { Image, Button, Tooltip, Upload } from 'antd';
import { get } from 'zero-element/lib/utils/request/endpoint';
import { getToken } from 'zero-element/lib/utils/request/token';

export default function (props) {
  const { value, options, handle, onChange } = props;
  const { title = '上传', API = '/api/fs/uploadfile', width = 200, height = width, tips } = options;
  const [loading, setLoading] = useState(false);
  const [visible, setVisible] = useState(false);

  function handleChange(info) {
    const { fileList } = info;
    if (info.file.status === 'uploading' && fileList.length > 0) {
      setLoading(true);
    }
    if (info.file.status === 'done') {
      onChange(getValue(info.file, 'url'));

      setLoading(false);
      setVisible(false);
    }
    if (info.file.status === 'error') {
      setLoading(false);
      onChange(undefined);
    }
  }

  function handleVisibleChange(bool) {
    setVisible(bool);
  }

  return <div style={{ width, textAlign: 'center' }}>
    <div >
      <Image
        width={width}
        height={height}
        style={{ background: '#f7f7f7' }}
        src={value}
      />
    </div>
    <br />
    <Tooltip
      title={tips}
      visible={visible}
      onVisibleChange={handleVisibleChange}
    >
      <Upload
        name="file"
        disabled={loading}
        action={/^http(s)*:\/\//.test(API) ? API : `${get()}${API}`}
        headers={{
          authorization: `Bearer ${getToken()}`,
        }}
        onChange={handleChange}
        showUploadList={false}
      >
        <Button
          type="primary"
          loading={loading}
        >
          {title}
        </Button>
      </Upload>
    </Tooltip>
    <br /><br />
  </div>
}

function getValue(obj, key, spareKey) {
  if (!obj) return undefined;
  if (obj.response) {
    if (obj.response.data && obj.response.data[key]) {
      return obj.response.data[key];
    }
    if (obj.response.data && obj.response.data[spareKey]) {
      return obj.response.data[spareKey];
    }
  }
  if (obj[key]) {
    return obj[key];
  }
  if (obj[spareKey]) {
    return obj[spareKey];
  }
  return undefined;
}