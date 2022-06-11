import React, { useState } from 'react';
import { Layout } from 'antd';
import useSelectedKeys from '../../utils/useSelectedKeys';
import renderMenu from '../../utils/renderMenu';

import SubMenu from './SubMenu';
import MenuItem from './MenuItem';

import {
  LeftOutlined,
} from '@ant-design/icons';
import './index.less';

const { Sider } = Layout;

const cMap = {
  SubMenu,
  MenuItem,
};

export default function LeftNav({ path, menuData }) {
  const selectedKeys = useSelectedKeys(path);
  const [collaps, setCollaps] = useState(false);

  return <Sider width={collaps ? 56 : 200}
    className="LeftNavCollaps"
  >
    <>
      <LeftOutlined rotate={collaps ? 180 : 0}
        className="collapsButton"
        onClick={_ => setCollaps(!collaps)} />
      <>
        {renderMenu({
          menuData,
          divider: true,
          collaps,
          selectedKeys,
          component: cMap,
        })
        }
      </>
    </>
  </Sider>
}