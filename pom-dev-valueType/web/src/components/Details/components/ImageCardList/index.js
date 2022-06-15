import React from 'react';
import ZEle from 'zero-element';
import ItemPanel from './ItemPanel';
import './index.less';

export default function ImageCardList({ data, field, map, namespace, api, operation, format }) {
  const config = {
    items: [
      {
        component: 'ItemList', config: {
          API: {
            listAPI: api,
          },
          fields: []
        }
      }
    ]
  };
  const records = (data && field) ? data[field] : undefined;

  return <div className="c-ImageCardList">
    <ZEle
      namespace={namespace}
      config={config}
      operation={operation}
      format={format}
      map={map}
      Item={ItemPanel}
      data={records}
      pagination={!Boolean(records)}
    />
  </div>
}