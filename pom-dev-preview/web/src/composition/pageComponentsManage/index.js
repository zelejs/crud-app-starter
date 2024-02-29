import React, { useState } from 'react';
import { HStack, VStack, Box, Button, Text, Switch } from '@chakra-ui/react';
import { history } from 'umi';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import Indicators from '@/composition/indicators';
import Selectors from '@/composition/selectors';
import Carts from '@/composition/carts';
import Presenters from '@/composition/presenters';
import Layouts from '@/composition/Layouts';

import { IndicatorIcon, SelectorIcon, CartIcon, PresenterIcon } from './svg'

const _menuList = [
  { id: 1, name: 'indicators', icon: <IndicatorIcon />, hoverIcon: <IndicatorIcon fill="#4BDD97" />, selected: false },
  { id: 2, name: 'selectors', icon: <SelectorIcon />, hoverIcon: <SelectorIcon fill="#4BDD97" />, selected: false },
  { id: 3, name: 'carts', icon: <CartIcon />, hoverIcon: <CartIcon fill="#4BDD97" />, selected: false },
  { id: 4, name: 'presenters', icon: <PresenterIcon />, hoverIcon: <PresenterIcon fill="#4BDD97" />, selected: true },
  // { id: 5, name: 'layouts', icon: <PresenterIcon />, hoverIcon: <PresenterIcon fill="#4BDD97" />, selected: false },
]

const _showAddBtns = [ 'carts', 'presenters']


export default function Index(props) {

  const _ComponentPagesMap = {
    indicators: Indicators ,
    selectors: Selectors,
    carts: Carts,
    presenters: Presenters,
    layouts: Layouts
  }

  const [menuList, setMenuList] = useState(_menuList)
  const [onHover, setOnHover] = useState(false);
  const [currentHoverMenuId, setCurrentHoverMenuId] = useState(0);
  const [componentName, setComponentName] = useState('presenters');
  const [isSwitch, setIsSwitch] = useState(false)

  const toggleHoverEntered = (menuId) => {
    setOnHover(true)
    setCurrentHoverMenuId(menuId)
  }
  const toggleHoverLeaved = () => {
    setOnHover(false)
    setCurrentHoverMenuId(0)
  }

  const onMenuClick = (menuItem) => {
    let clickItem = ''
    menuList.map((item, index) => {
      if (item.id === menuItem.id) {
        clickItem = item
        item.selected = true
        setComponentName(item.name)
      } else {
        item.selected = false
      }
      return item;
    })
    //重置状态
    setCurrentHoverMenuId(0)
    setMenuList(menuList)
    setIsSwitch(false)
    if (clickItem) {
      console.log('menu item click = ', clickItem)
    }
  }

  //左侧组件列表
  const matchComponentPage = () => {
    const C = _ComponentPagesMap[componentName]
    return <C {...props} isSwitch={isSwitch} />
  }

  return (
    <HStack spacing='0' overflowY={'hidden'}>
      <Box style={{ width: '100px', height: window.innerHeight, background: '#1F2229', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <VStack spacing={5}>
          {menuList.map((item, index) => (
            <Box key={`menu_${index}`} cursor={'pointer'}
              onMouseEnter={() => toggleHoverEntered(item.id)}
              onMouseLeave={() => toggleHoverLeaved(item.id)}
              onClick={() => onMenuClick(item)}
            >
              <VStack spacing={0}>
                {
                  item.icon && (
                    <Box key={`menu_${index}`}>
                      {item.selected ? item.hoverIcon : onHover && currentHoverMenuId == item.id ? item.hoverIcon : item.icon}
                    </Box>
                  )
                }
                <Box>
                  <Text color={item.selected ? "#4BDD97" : onHover && currentHoverMenuId == item.id ? "#4BDD97" : "#fff"}>{item.name}</Text>
                </Box>
              </VStack>
            </Box>

          ))}
          {
            _showAddBtns.includes(componentName) ? (
              <VStack spacing={1}>
                <Switch colorScheme={"green"} isFocusable size='md' isChecked={isSwitch} onChange={(e) => setIsSwitch(e.target.checked)} />
                <Box>
                  <Text p={0} m={0} color={'#fff'}>新增</Text>
                </Box>
              </VStack>
            ) : <></>
          }


        </VStack>
      </Box>

      <Box style={{ width: '100%', height: window.innerHeight, background: '#15171C' }}>
        {componentName ? matchComponentPage()  : <></>}
      </Box>

    </HStack>
  )

}