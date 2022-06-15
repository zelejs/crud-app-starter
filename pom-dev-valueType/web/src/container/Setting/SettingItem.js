import React from 'react';
import { Typography, Table } from 'antd';

import Input from './Input';
import Operation from './Operation';

const { Title } = Typography;

export default function ({
  data,
  editID,
  onEdit, onEditCancel, onSaveChange, onSubmit
}) {
  const { id, title, name, items = [] } = data;
  const columns = [
    {
      dataIndex: 'title', title: '名称',
      render: (text, record) => text || record.name
    },
    { dataIndex: 'description', title: '描述' },
    {
      dataIndex: 'value', title: '值',
      render: (text, record, index) => <Input
        value={text}
        data={record}
        index={index}
        editID={editID}
        onSaveChange={onSaveChange}
      />
    },
    {
      dataIndex: 'operation', title: '操作',
      render: (text, record, index) => <Operation
        value={text}
        data={record}
        index={index}
        editID={editID}
        onEdit={onEdit}
        onEditCancel={onEditCancel}
        onSubmit={onSubmit}
      />
    },
  ];

  return id ? (
    <div>
      <Title level={4}>{title || name}</Title>
      <Table
        rowKey="id"
        bordered
        columns={columns}
        dataSource={items}
        pagination={false}
      />
    </div>
  ) : '点击左侧选择配置'
}