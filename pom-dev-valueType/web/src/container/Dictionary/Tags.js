import React from 'react';
import { Tag, Icon } from 'antd';

import styles from './index.less';

export default function ({
  data = [],
  onDelete
}) {
  return data.map(tag => {
    return <span key={tag.id} className={styles.tagContainer}>
      <Tag
        color="#108ee9"
        className={styles.tag}
      >
        {tag.name}
      </Tag>
      <Icon type="delete"
        className={styles.delete}
        onClick={onDelete.bind(null, tag.id)}
      />
    </span>;
  })
}