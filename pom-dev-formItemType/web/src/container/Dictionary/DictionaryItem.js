import React from 'react';
import { Typography } from 'antd';

import Tags from './Tags';
import TagAdd from './TagAdd';

const { Title } = Typography;

export default function DictionaryItem({
  title, data,
  onAdd,
  onDelete,
}) {
  return <div>
    <Title level={4}>{title}</Title>
    <Tags data={data}
      onDelete={onDelete}
    />
    <TagAdd onAdd={onAdd} />
    <br /><br />
  </div>
}