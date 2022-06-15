import React, { useEffect, useState } from 'react';
import { Render } from 'zero-element/lib/config/formItemType';
import { getPageData } from 'zero-element/lib/Model';

const hideRemove = [
  'FINISHED', 'VERIFIED'
];

/**
 * 专门用来处理结案文件
 * @param {*} props 
 */
export default function EchoFile(props) {
  const { namespace, options, onChange, handle } = props;
  const newOptions = {
    max: 99,
    type: 'text',
    dragger: true,
    ...options,
  };
  const pageData = getPageData(namespace);
  const { detailData = {} } = pageData;
  const { status, reportFileName, reportFileUrl } = detailData || {};
  const [fileList, setFileList] = useState([]);
  const { onSaveOtherValue } = handle;

  useEffect(_ => {
    if (reportFileUrl) {
      setFileList([{
        name: reportFileName || reportFileUrl || '-',
        url: reportFileUrl,
      }]);
      onChange(reportFileUrl);
      onSaveOtherValue('fileName', reportFileName);
    } else {
      setFileList([]);
      onChange(undefined);
    }
  }, [reportFileUrl, reportFileName]);

  return fileList.length ? (
    <Render
      key="defaultValue"
      n="upload-file"
      {...props}
      options={newOptions}
      props={{
        showUploadList: {
          showRemoveIcon: hideRemove.includes(status) ? false : true,
        }
      }}
      value={fileList}
    />
  ) : (
      <Render
        n="upload-file"
        {...props}
        options={newOptions}
        value={[]}
      />
    )
}