import React from 'react';
import {
  // Button,
  Menu,
  // Pagination
} from 'antd';
import NavMenuItem from './NavMenuItem';

const { Item } = Menu;

export default function NavMenu({ selectId, data, onClick, onPagination }) {
  if (!data.length) return null;

  function handleClick({ key }) {
    const find = data.find(i => String(i.id) === key);

    onClick(find);
  }

  return <>
    {/* <Button
      icon="plus"
      type="dashed"
      block
    >
      新增
    </Button> */}
    <Menu
      mode="inline"
      onClick={handleClick}
      selectedKeys={[selectId]}
      style={{
        width:  186,
      }}
    >
      {data.map(item => {
        return <Item key={item.id}>
          <NavMenuItem
            data={item}
          />
        </Item>
      })}
    </Menu>
    {/* <div>
      <Pagination
        simple
        onChange={onPagination}
      />
    </div> */}
  </>
}