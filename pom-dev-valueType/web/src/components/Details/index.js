import React from 'react';
import { Spin, Button } from 'antd';
import ZEle from 'zero-element';
import global from 'zero-element/lib/config/global';
import useDetails from './hooks';
import { Render } from 'zero-element/lib/config/formItemType';
import { Render as LayoutRender } from 'zero-element/lib/config/layout';

import './index.less';
import _ from 'lodash';

export default function Details(props) {
  const {
    namespace,
    API,
    col = 2,
    fields = [],
    fieldsMap = {},
    goBack = false,
    data: proData,
    loading: proLoading,
  } = props;

  const [details, loading] = useDetails(namespace, API);
  const data = proData || details;

  return <Spin spinning={proLoading || loading}>
    {goBack && global.goBack ? (
      <>
        <Button onClick={global.goBack}>返回</Button>
      </>
    ) : null}
    <LayoutRender n="Grid" value={Array(col).fill(~~(24 / col))}>
      {fields.map((option, i) => {
        const { label } = option;

        return <div key={i} span={option.span} className="Details-item">
          {label ? (
            <div className="Details-labelTitle">
              {label} :
            </div>
          ) : null}
          {renderPlain(data, option, fieldsMap)}
        </div>
      })}
    </LayoutRender>
  </Spin>
}

function renderPlain(data, option, map) {
  const { field, options, ...rest } = option;
  const optionsObj = { ...options, ...rest };

  if (map && map[field]) {
    optionsObj.map = map[field].map;
  }

  return <Render n="plain"
    className="Details-valueContainer"
    options={optionsObj}
    value={_.get(data, field)}
    formdata={data}
  />
}

function RenderList({ namespace, columns, data = [] }) {
  return <ZEle
    namespace={namespace}
    config={{
      items: [
        {
          component: 'ReportList',
          config: {
            fields: columns.map(col => {
              return {
                ...col,
                key: col.field,
                dataIndex: col.field,
                title: col.label,
              }
            })
          }
        }
      ]
    }}
    data={data}
  />
}