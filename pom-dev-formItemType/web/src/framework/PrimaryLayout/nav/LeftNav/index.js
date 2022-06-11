import React, { useEffect } from 'react';
import { Layout } from 'antd';
import useSelectedKeys from '../../utils/useSelectedKeys';
import renderMenu from '../../utils/renderMenu';
import { history } from 'umi';

import SubMenu from './SubMenu';
import MenuItem from './MenuItem';
import './index.less';

const { Sider } = Layout;

const cMap = {
  SubMenu,
  MenuItem,
};

export default function LeftNav({ path, navType, menuData }) {
  const selectedKeys = useSelectedKeys(path);

  useEffect(_ => {
    if (menuData && menuData.length && navType === 'both' && selectedKeys.length) {
      if (menuData[0].path !== path && path.split('/').length <= 2) {
        history.push({
          pathname: menuData[0].path
        })
      }
    }
  }, [path])

  return <Sider
    width={menuData && menuData.length ? 200 : 0}
    className="LeftSider"
    style={{
      background: '#fff',
      overflow: 'hidden auto',
    }}>
    <div className="LeftNav">
      {renderMenu({
        menuData,
        divider: false,
        selectedKeys,
        component: cMap,
      })
      }
    </div>
  </Sider>
}