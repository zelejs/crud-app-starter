import React, { useState, useEffect, useMemo, useRef } from 'react';
import { Table } from 'antd';
import { useWillMount } from 'zero-element/lib/utils/hooks/lifeCycle';
import { query } from 'zero-element-antd/lib/utils/request';
import CheckboxGroup from './CheckboxGroup';
import CheckboxClass from './CheckboxClass';
import CheckboxItems from './CheckboxItems';


export default function (props) {
  const { value } = props;
  const dataSource = useRef([]);
  const [listData, setListData] = useState([]);
  const [selected, setSelected] = useState({});
  const [selectedClass, setSelectedClass] = useState([]);

  const fListData = useMemo(_ => {
    const layoutList = getRowSpan(listData);

    return listData.map((item, i) => {
      item.rowSpan = layoutList[i];
      item.selected = selected;
      item.onAllChecked = handleSelectedClass.bind(null, i);
      if (layoutList[i]) {
        item.selectedClass = selectedClass.slice(i, layoutList[i] + i);
      }
      return item;
    })
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [listData, selected, selectedClass]);

  const columns = [
    {
      title: '权限组',
      dataIndex: 'group',
      width: 120,
      render(value, row) {
        const { rowSpan } = row;
        const obj = {
          props: {
            rowSpan,
          },
          children: <CheckboxGroup
            value={value}
            data={row}
            onChange={handleSelectGroupAllChild}
          />,
        };

        return obj;
      }
    },
    {
      title: '权限类',
      dataIndex: 'name',
      width: 120,
      render(value, row) {
        return <CheckboxClass
          value={value}
          data={row}
          onChange={handleSelectAllChild}
        />
      }
    },
    {
      title: '权限名',
      colSpan: 2,
      dataIndex: 'perms',
      render(value, row) {
        return <CheckboxItems
          value={value}
          data={row}
          onChange={handleSelect}
        />
      }
    },
  ];

  useWillMount(queryData);
  useEffect(_ => {
    if (value) {
      const obj = {};
      value.forEach(key => {
        obj[key] = true;
      })
      setSelected(obj);
    }
  }, [value])

  function queryData() {
    query('/api/adm/perms/group_by')
      .then(({ items }) => {
        if(items && Array.isArray(items)){
          const itemsChildren = items[0].items;
          if(itemsChildren && Array.isArray(itemsChildren)){
            dataSource.current = itemsChildren;
            setListData(formatData(dataSource.current));
          }
        }
      })
  }
  function handleSelect(checked, data) {
    const perms = getAllPerms(data);
    if (checked) {
      handleAppendPerms(perms);
    } else {
      handleRemovePerms(perms)
    }
  }
  function handleSelectAllChild(checked, data) {
    const perms = getAllPerms(data);
    if (checked) {
      handleAppendPerms(perms);
    } else {
      handleRemovePerms(perms)
    }
  }
  function handleSelectGroupAllChild(checked, key) {
    const find = dataSource.current.find(item => item.id === key);
    const perms = getGroupAllPerms(find);

    if (checked) {
      handleAppendPerms(perms);
    } else {
      handleRemovePerms(perms)
    }
  }
  function handleAppendPerms(perms) {
    const obj = {};
    perms.forEach(key => {
      obj[key] = true;
    });
    handleChange(obj);
  }
  function handleRemovePerms(perms) {
    const obj = {};
    perms.forEach(key => {
      obj[key] = false;
    });
    handleChange(obj);
  }
  function handleSelectedClass(index, data) {
    selectedClass[index] = data;
    setSelectedClass([...selectedClass]);
  }
  function handleChange(obj) {
    const perms = {
      ...selected,
      ...obj,
    };
    const rst = [];
    Object.keys(perms).forEach(key => {
      if (perms[key]) {
        rst.push(key);
      }
    });
    props.onChange(rst);
  }

  return <div>
    <Table
      bordered
      rowKey="key"
      columns={columns}
      pagination={false}
      dataSource={fListData}
    />
  </div>
}

function getRowSpan(data) {
  const rst = [];
  const unique = {};
  let preGNIndex;

  data.forEach((item, index) => {
    const { group } = item;
    if (group) {
      if (unique[group]) {
        rst.push(0);
        rst[preGNIndex] = rst[preGNIndex] + 1;
      } else {
        unique[group] = true;
        preGNIndex = index;

        rst.push(1);
      }
    } else {
      rst.push(1);
    }
  })
  return rst;
}

function formatData(data) {
  const stack = [...data];
  const recorder = {};
  const rst = [];

  while (stack.length) {
    const item = stack.shift();
    const { id, pid, groupId, items } = item;
    if (items) {
      stack.push(...items);
    }
    const obj = {
      key: id,
      name: item.name,
    };
    recorder[id] = obj;
    if (pid) {
      if (!recorder[pid]) {
        // 一级
        // obj.perms = [];
        // obj.group = item.name;
      } else {
        // 二级
        obj.group = recorder[pid].name;
        obj.groupId = recorder[pid].key;
        // recorder[pid].name = item.name;
        obj.perms = [];
        rst.push(obj);
      }
    } else if (groupId) {
      // 三级
      recorder[groupId].perms.push({
        id,
        name: item.name,
      });
    }
  }
  return rst;
}

function getAllPerms(data) {
  const stack = [data];
  const rst = [];

  while (stack.length) {
    const item = stack.shift();
    if (item.id) {
      rst.push(item.id);
    }

    if (item.perms) {
      stack.push(...item.perms);
    }
  }
  return rst;
}
function getGroupAllPerms(data) {
  const stack = [data];
  const rst = [];

  while (stack.length) {
    const item = stack.shift();
    if (item.groupId) {
      rst.push(item.id);
    }

    if (item.items) {
      stack.push(...item.items);
    }
  }
  return rst;
}