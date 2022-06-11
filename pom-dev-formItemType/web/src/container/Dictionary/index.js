import React, { useState, useEffect } from 'react';
import useBaseList from 'zero-element/lib/helper/list/useBaseList';
import { useDidMount, useWillUnmount } from 'zero-element/lib/utils/hooks/lifeCycle';
import { Card } from 'antd';
import { Render } from 'zero-element/lib/config/layout';

import Flex from '@/../zero-antd-dep/layout/Flex';

import Tree from 'zero-element-antd/lib/components/Tree';

import DictionaryItem from './DictionaryItem';
import Modal from './Modal';
import { post } from 'zero-element/lib/utils/request';

const { FlexItem } = Flex;

export default function Dictionary(props) {

  const { namespace, config } = props;
  const {
    layout = 'Empty', layoutConfig = {},
    API = {},
    tree = {},
  } = config;

  const { API: treeAPI, searchField = 'search' } = tree;

  const [extraData, setExtraData] = useState({});
  const [visible, setVisible] = useState(false);
  const [groupId, setGroupId] = useState(-1);

  const listProps = useBaseList({
    namespace,
    modelPath: 'listData',
    extraData,
  }, config);

  const { data, handle } = listProps;
  const { onGetList, onClearList } = handle;

  useDidMount(_ => {
    if (API.listAPI) {
      if (API.listAPI.indexOf('<') === -1) {
        onGetList({});
      }
    }
  });
  useEffect(_ => {
    if (extraData.id) {
      onGetList({});
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [extraData]);
  useWillUnmount(onClearList);

  function handleSelect(data) {
    const { children } = data;
    if (children && !children.length) {
      setExtraData(data);
    }
  }

  function handleModalVisible() {
    setVisible(!visible);
    setGroupId(-1);
    onGetList({});
  }
  function handleOpenAddModal(id) {
    setVisible(true);
    setGroupId(id);
  }
  function handleDelete(id) {
    const api = `/api/config/field/options/${groupId}/bulk/delete`;
    post(api, {
      ids: [id]
    })
      .then(_ => {
        onGetList({});
      })
  }

  return <Render n={layout} {...layoutConfig}>
    <Flex align="flex-start">
      {treeAPI ? (<FlexItem>
        <Tree
          API={treeAPI}
          defaultExpandParent
          searchField={searchField}
          namespace={namespace}
          onChange={handleSelect}
        />
      </FlexItem>) : null}
      {extraData.id ? (
        <FlexItem className="ZEleA-margin-left" flex={1}>
          <Card size="small"
          >
            {data && data.map(item => {
              return <DictionaryItem
                key={item.id}
                title={item.title || item.name}
                data={item.items}
                onAdd={handleOpenAddModal.bind(null, item.field)}
                onDelete={handleDelete}
              />;
            })}
          </Card>
        </FlexItem>
      ) : null}
    </Flex>
    <Modal
      visible={visible}
      namespace={namespace}
      onClose={handleModalVisible}
      extraData={{
        id: groupId,
      }}
    />
  </Render>
}