import React from 'react';
import { Switch, Icon } from 'antd';
import { Render } from 'zero-element/lib/config/formItemType';
import { Render as LayoutRender } from 'zero-element/lib/config/layout';
import useDetails from '../../hooks';
import _ from 'lodash';

import '../../index.less';

export default function PreviewFile(props) {
  const {
    namespace,
    API,
    col = 2,
    data: proData,
    loading: proLoading,
    label,
    span=12,
  } = props;

  console.log('preview data = ', props.data)

  const [details, loading] = useDetails(namespace, API);
  const data = proData || details;

  return (
    <LayoutRender n="Grid" value={Array(col).fill(~~(24 / col))}>
      <div key={'preview_file_index'} span={span} className="Details-item">
        {label ? (
          <div className="Details-labelTitle">
            {label} :
          </div>
        ) : null}
        {/* {renderPlain(data, props, fieldsMap)} */}
        <div>TODO 对接预览组件</div>
      </div>
    </LayoutRender>
  )

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