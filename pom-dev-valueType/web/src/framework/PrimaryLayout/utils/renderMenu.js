import React from 'react';
import { Link } from 'umi';
import { Avatar } from 'antd';

/**
 * 渲染导航菜单数据
 *
 * @export
 * @param {Array} menuData
 * @param {boolean} divider 是否要把没有 path 的子项渲染成 分割线
 * @returns React.element
 */

export default function renderMenu({
  menuData, divider, navType,
  collaps, selectedKeys,
  onClick,
  component,
}) {
  const stack = [menuData];
  const rst = [];
  const { SubMenu, MenuItem } = component;
  // console.log(selectedKeys,"SELECT")
  while (stack.length) {
    const menu = stack.shift();

    if (!menu) {
      break;
    };
    if (menu.invisible) {
      continue;
    }

    if (Array.isArray(menu)) {
      stack.push(...menu);
    } else {
      const { icon, name, path, items } = menu;

      if (Array.isArray(items)) {
        rst.push(<SubMenu key={path}
          collaps={collaps}
          path={path}
          selected={selectedKeys.includes(path)}
          title={
            renderItem(icon, name, collaps)
          }
        >
          {renderMenu({ menuData: items, selectedKeys, component })}
        </SubMenu>);
      } else {
        if (path) {
          let topNavItem;
          if (typeof onClick === 'function') {
            topNavItem = <MenuItem
              key={path}
              collaps={collaps}
              selected={selectedKeys.includes(path)}
              onClick={onClick.bind(null, path)}
            >
              {renderItem(icon, name, collaps)}
            </MenuItem>
          } else {
            topNavItem = <MenuItem key={path}
              collaps={collaps}
              selected={selectedKeys.includes(path)}
            >
              <Link to={path}>
                {renderItem(icon, name, collaps)}
              </Link >
            </MenuItem>
          }
          rst.push(topNavItem);
        } else {
          divider && rst.push(<div key={menu.key || name}><hr /></div>);
        }
      }
    }
  }

  return rst;
}

function renderItem(icon, name, collaps) {
  return <div className="nav-title">
    <div className="nav-icon">
      {icon ? (<div
        dangerouslySetInnerHTML={{
          __html: icon
        }}></div>)
        :
        collaps ? <Avatar size={26}>{name.slice(0, 1)}</Avatar> : null
      }
    </div>
    <span className="nav-name">{name}</span>
    
  </div>
}