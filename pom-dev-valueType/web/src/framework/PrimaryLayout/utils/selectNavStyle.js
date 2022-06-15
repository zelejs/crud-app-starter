// import React from 'react';
import TopNav from '../nav/TopNav';
import LeftNav from '../nav/LeftNav';
import LeftNavCollaps from '../nav/LeftNavCollaps';
import { getModel } from 'zero-element/lib/Model';

const navMap = {
  'top': (menuData) => ([
    TopNav, menuData,
    () => null, []
  ]),
  'left': (menuData) => ([
    () => null, [],
    LeftNav, menuData,
  ]),
  'LeftNavCollaps': (menuData) => ([
    () => null, [],
    LeftNavCollaps, menuData,
  ]),
};

function selectNavStyle(type, menuData, path, switchLeftNav) {
  const permMenu = filterMenu(menuData);
  if (navMap[type]) {
    return navMap[type](permMenu);
  }

  let fPath;
  if (switchLeftNav) {
    fPath = switchLeftNav;
  } else {
    // 提取 父 path
    fPath = path.replace(/(\/\w+)[\/\w-]*/, '$1');
  }
  // 定位到 父path 的数据
  const leftNavData =
    permMenu.find(menu => menu.path === fPath)
    || { items: [] };

  return [
    TopNav, permMenu.map(menu => {
      const { items, ...rest } = menu;
      return rest;
    }),
    LeftNav, leftNavData.items
  ];
}

function filterMenu(menuData) {
  const globalModel = getModel('global');

  const stack = [menuData];
  const rst = [];

  while (stack.length) {
    const shift = stack.shift();
    if (!shift) {
      break;
    };
    if (Array.isArray(shift)) {
      rst.push(...shift);
    } else {
      const menu = { ...shift };

      if (menu.permissions) {
        const perm = globalModel.getPerm();
        if (!checkPerm(menu.permissions, perm)) {
          continue;
        }
      }

      if (Array.isArray(menu.items)) {
        menu.items = filterMenu(menu.items);
      }
      rst.push(menu);
    }

  }
  return rst;
}

function checkPerm(key, perm) {
  if (typeof key === 'string') {
    return perm[key];
  }
  if (Array.isArray(key)) {
    return key.some(k => perm[k]);
  }
}

export default selectNavStyle;