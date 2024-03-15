import React, { useState, useEffect } from 'react';
import { VStack, Box, HStack, Button, Text } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';
require('./index.less');


const _menuList = [
    { id: 1, name: '预览', selected: false },
    { id: 2, name: '编辑', selected: false },
    // { id: 3, name: '绑定属性', selected: false },
    // { id: 4, name: '确定', selected: false },
]

export default function Index(props) {

    const { itemClick, resetData } = props;

    const [menuList, setMenuList] = useState(_menuList)
    const [currentHoverMenuId, setCurrentHoverMenuId] = useState(0);

    useEffect(() => {
        menuList.map((item, index) => {
            return item.selected = false
        })
        setMenuList(menuList)
    },[resetData])

    const toggleHoverEntered = (menuId) => {
        setCurrentHoverMenuId(menuId)
      }
      const toggleHoverLeaved = () => {
        setCurrentHoverMenuId(0)
      }

      const onMenuClick = (menuItem) => {
        let clickItem = ''
        menuList.map((item, index) => {
          if (item.id === menuItem.id) {
            clickItem = item
            item.selected = true
          } else {
            item.selected = false
          }
          return item;
        })
        //重置状态
        setCurrentHoverMenuId(0)
        setMenuList(menuList)
        if (clickItem.selected) {
          // console.log('menu item click = ', clickItem)
          itemClick(menuItem.id)
        }
      }

    return (
            <HStack spacing={'3'}>
                {menuList.map((item, index) => (
                    <Box key={`menu_${index}`} cursor={'pointer'}  className='menuItem' style={{background:item.selected ? "#3eb97e" : "#fff"}}
                        onMouseEnter={() => toggleHoverEntered(item.id)}
                        onMouseLeave={() => toggleHoverLeaved(item.id)}
                        onClick={() => onMenuClick(item)}
                    >
                        <Text style={{margin:0}}  color={item.selected ? "#fff" : "#000"}>{item.name}</Text>
                    </Box>

                ))}
            </HStack>
    )

}