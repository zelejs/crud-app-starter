import React from 'react';
import useSelectedKeys from '../../utils/useSelectedKeys';
import renderMenu from '../../utils/renderMenu';

import SubMenu from './SubMenu';
import MenuItem from './MenuItem';
import './index.less';

const cMap = {
  SubMenu,
  MenuItem,
};

export default function TopNav({ navType, path, menuData, onClick }) {
  const selectedKeys = useSelectedKeys(path);

  return <div className="TopNav">
    {renderMenu({
      menuData,
      divider: false,
      selectedKeys,
      component: cMap,
    })
    }
  </div>
}