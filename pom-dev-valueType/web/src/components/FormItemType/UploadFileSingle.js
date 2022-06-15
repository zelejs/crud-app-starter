import React, { useState, useEffect } from 'react';
import { Upload, Button, message } from 'antd';
import { get } from 'zero-element/lib/utils/request/endpoint';
import { getToken } from 'zero-element/lib/utils/request/token';
import { formatAPI } from 'zero-element/lib/utils/format';
import { LoadingOutlined, PlusOutlined, UploadOutlined } from '@ant-design/icons';

const { Dragger } = Upload;

const initFileList = [];
export default function UploadFile(props) {
  const { value, options, namespace, handle, props: restProps } = props;

  const {
    title = '点击上传',
    API = '/api/fs/uploadfile',
    max = 9,
    fileNameField = 'fileName',
    type = 'json',
    dragger = false,
    acceptType=''
  } = options;
  const { onSaveOtherValue } = handle;
  const [fileList, setFileList] = useState(initFileList);
  const [loading, setLoading] = useState(false);
  const fAPI = formatAPI(API, {
    namespace,
  });
  
  useEffect(_ => {
    if (fileList === initFileList) {
      setFileList(format(value, props.formdata));
    }
  }, [fileList, value]);

  const uploadButton = (
    <Button>
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      {title}
    </Button>
  );

  function handleChange(info) {
    const { file, fileList } = info;
    setFileList(fileList.length > 0 ? [file]:[]);

    if (info.file.status === 'uploading' && fileList.length > 0) {
      setLoading(true);
    }
    if (info.file.status === 'done' ||
      info.file.status === 'error' ||
      info.file.status === 'removed') {
      setLoading(false);

      if (info.file.status === 'error') {
        message.error(`文件 ${info.file.name} 上传失败`);
      }

      const doneFileList = fileList.filter(file => file.status === 'done');
      const saveFileList = doneFileList.map(file => ({
        name: getValue(file, 'originalFileName', 'name'),
        url: getValue(file, 'url'),
        fileName: getValue(file, 'originalFileName', 'name'),
        fileUrl: getValue(file, 'url'),
      }));

      if (type === 'json') {
        props.onChange(saveFileList);
      } else {
        props.onChange(saveFileList.map(i => i.url).join(';'));
      }

      if (max === 1) {
        if (saveFileList[0]) {
          onSaveOtherValue(fileNameField, saveFileList[0].name);
        } else {
          onSaveOtherValue(fileNameField, '');
        }
      }
    }
  }
  
  const uploadProps = {
    accept: acceptType,
    name: 'file',
    action: /^http(s)*:\/\//.test(API) ? fAPI : `${get()}${fAPI}`,
    fileList: fileList,
    showUploadList: true,
    headers: {
      authorization: `Bearer ${getToken()}`,
    },
    onChange: handleChange
  }

  const limit = fileList.length >= max;

  return <div className="clearfix">
    {dragger ?
      (
        <Dragger
          {...uploadProps}
          {...restProps}
          className={limit ? 'ZEleA-UploadFile-hidden' : ''}
        >
          <p className="ant-upload-drag-icon">
            <UploadOutlined />
          </p>
          <p className="ant-upload-text">点击或拖拽文件以上传</p>
        </Dragger>
      ) : (
        <Upload
          {...uploadProps}
          {...restProps}
        >
          {limit ? '' : uploadButton}
        </Upload>
      )}
  </div>
}
function format(value, formatData) {
  let rst = [];
  try {

    if (typeof (value) === 'string') {
      const showData = [
        {
          "name": `${get()}${value}`,
          "url": `${get()}${value}`,
        }
      ]
      rst = showData;
    } else if (Array.isArray(value)) {
      rst = value;
    } else if(JSON.stringify(formatData) != '{}'){
        
      const showData = [
        {
          "name": formatData.fileName,
          "url": `${get()}${formatData.url}`,
        }
      ]
      rst = showData;
    }
      
  } catch (e) {
    rst.push({
      name: '文件',
      url: value,
    });
  }
  rst.length > 0 && rst.map((item, index) => {
    rst[index] = {
      ...item,
      name: item.name || item.fileName || item.url,
      url: item.url || item.fileUrl || undefined,
      uid: String(-index),
      status: 'done',
    }
  });
  return rst;
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