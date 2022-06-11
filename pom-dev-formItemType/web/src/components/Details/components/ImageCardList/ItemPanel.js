import React, { useMemo } from 'react';
import Panel from '@/components/Panel';
import Flex from '@/../zero-antd-dep/layout/Flex';
import Item from './Item';
import { formatAPI } from 'zero-element/lib/utils/format';
import { formatTableFields } from 'zero-element-antd/lib//container/List/utils/format';
import { useModel } from 'zero-element/lib/Model';

const { FlexItem } = Flex;

export default function (props) {
  const { data = {}, map = {}, namespace, operation, format, handle, index, } = props;

  const model = useModel(namespace);
  const operationRender = useMemo(_ => {
    if (Array.isArray(operation)) {
      return formatTableFields([], operation, handle, {
        namespace,
        model,
      }).columns[0].render;
    }
    return _ => null;

  }, [operation]);


  const beforeData = {
    title: formatAPI(map.title, { namespace, data }),
    subTitle: formatAPI(map.subTitle, { namespace, data }),
    image: formatAPI(map.image, { namespace, data }),
    imageTitle: formatAPI(map.imageTitle, { namespace, data }),
  };

  return <Panel
    collapseIcon={false}
    title={
      <Flex>
        <FlexItem>{beforeData.title}</FlexItem>
        <FlexItem flex={1} className="padding-left weight-400">{beforeData.subTitle}</FlexItem>
      </Flex>
    }>
    <Item data={beforeData} indexData={{
      text: '',
      record: data,
      index,
      type: '',
    }}
      format={format}
      operation={operationRender('', index, data)}
    />
  </Panel>
}