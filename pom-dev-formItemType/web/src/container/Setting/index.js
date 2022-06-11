import React, { useState, useRef, useEffect } from 'react';
import { useDidMount } from 'zero-element/lib/utils/hooks/lifeCycle';
import Flex from '@/../zero-antd-dep/layout/Flex';
import { Render } from 'zero-element/lib/config/layout';
import { Card } from 'antd';
import NavMenu from './NavMenu';
import SettingItem from './SettingItem';
import { query, update } from 'zero-element/lib/utils/request';
import { formatAPI } from 'zero-element/lib/utils/format';

const { FlexItem } = Flex;

export default function Setting(props) {
  const [editID, setEditID] = useState(-1);
  const editValue = useRef(undefined);
  const [group, setGroup] = useState([]);
  // 当前选中的配置项
  const [extraData, setExtraData] = useState({});
  const [groupData, setGroupData] = useState({});
  const [pagination, setPagination] = useState(null);

  const { namespace, config } = props;
  const {
    layout = 'Empty', layoutConfig = {},
    API = {},
  } = config;

  useDidMount(_ => {
    if (API.listAPI) {
      queryGroup();
    }
  });
  useEffect(_ => {
    if (extraData.id) {
      queryGroupData()
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [extraData])

  function queryGroup() {
    const fApi = formatAPI(API.listAPI, {
      namespace,
    })
    query(fApi)
      .then(({ status, data }) => {
        if (status && data.code === 200) {
          let rst;
          if (data.data.pages !== undefined) {
            const { records, ...rest } = data.data;
            setPagination(rest);
            rst = records;
          } else {
            rst = findValidValue(data.data);
          }

          setGroup(rst);
          if (rst.length) {
            setExtraData(rst[0]);
          }
        }
      })
  }
  function queryGroupData() {
    const fApi = formatAPI(API.getAPI, {
      namespace,
      data: {
        id: extraData.id,
        name: extraData.name,
        title: extraData.title,
      }
    })
    query(fApi)
      .then(({ status, data }) => {
        if (status && data.code === 200) {
          setGroupData(data.data);
        }
      })
  }
  function handleClick(data) {
    setExtraData(data);
  }
  function handlePagination(page, pageSize) {
    console.log(123, page, pageSize);
  }


  function handleEdit(id, value) {
    editValue.current = value;
    setEditID(id);
  }
  function handleEditCancel() {
    setEditID(-1);
    editValue.current = undefined;
  }
  function handleSaveChange(e) {
    const value = e.target.value;
    editValue.current = value;
  }
  function handleSubmit(id, value) {
    setEditID(-1);
    if (value) {
      handelSaveData(id, value);
    } else {
      handelSaveData(id, editValue.current);
    }
    editValue.current = undefined;
  }

  function handelSaveData(id, value) {
    const fApi = formatAPI(API.updateAPI, {
      namespace,
      data: {
        id: id,
      }
    })
    update(fApi, {
      value,
    })
      .then(queryGroupData)
  }

  return <Render n={layout} {...layoutConfig}>
    <Flex align="flex-start" >
      <FlexItem>
        <NavMenu
          selectId={String((extraData.id))}
          data={group}
          onClick={handleClick}
          pagination={pagination}
          onPagination={handlePagination}
        />
      </FlexItem>
      <FlexItem flex={1} className="ZEleA-margin-left">
        <Card>
          <SettingItem
            data={groupData}
            editID={editID}
            onEdit={handleEdit}
            onEditCancel={handleEditCancel}
            onSaveChange={handleSaveChange}
            onSubmit={handleSubmit}
          />
        </Card>
      </FlexItem>
    </Flex>
  </Render>
}

function findValidValue(obj) {
  const stack = [obj];
  let rst = [];

  while (stack.length) {
    const item = stack.shift();
    if (item.length) {
      rst = item;
      break;
    }
    if (item.children) {
      stack.push(item.children);
    }
  }
  return rst;
}