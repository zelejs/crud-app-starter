import React, { useEffect, useState } from 'react';
import { Spin } from 'antd';
import { query } from '@/../zero-antd-dep/utils/request';
import { formatAPI } from 'zero-element/lib/utils/format';
import '../../index.less';

export default function ({ api, fieldsMap = {}, children }) {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const { status = {} } = fieldsMap;
  const { map = {} } = status;

  useEffect(_ => {
    setLoading(true);
    query(formatAPI(api, {}))
      .then(data => {
        setLogs(data)
      })
      .finally(_ => setLoading(false))
  }, [api])

  return <Spin spinning={loading}>
    <div className="list Details-statusList">
      {logs.map((log, i) => {
        return <div className="time" key={i}>
          <div className="label">{map[log.status] || log.status}</div>
          <div className="value"><pre>{log.note}</pre></div>
        </div>
      })}
    </div>
    {children}
  </Spin>
}