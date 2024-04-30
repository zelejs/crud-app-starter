import React, { useState, useEffect } from 'react';
import { HStack, VStack, Box, Button, Text, Switch } from '@chakra-ui/react';

import Indicators from '@/composition/indicators';
import Selectors from '@/composition/selectors';
import Carts from '@/composition/carts';
import Presenters from '@/composition/presenters';
import Layouts from '@/composition/Layouts';
import AutoLayouts from '@/composition/autoLayouts';
import PaletteManage from '@/composition/paletteManage';
import Categorys from '@/composition/categorys';

import { IndicatorIcon, SelectorIcon, CartIcon, 
  PresenterIcon, AutoLayoutIcon, PaletteIcon, CategoryIcon } from './svg'

require('./index.less');

const _menuList = [
  // { id: 1, name: 'auto', icon: <AutoLayoutIcon />, hoverIcon: <AutoLayoutIcon fill="#4BDD97" />, selected: false },
  { id: 2, name: 'indicators', icon: <IndicatorIcon />, hoverIcon: <IndicatorIcon fill="#4BDD97" />, selected: true },
  { id: 3, name: 'selectors', icon: <SelectorIcon />, hoverIcon: <SelectorIcon fill="#4BDD97" />, selected: false },
  { id: 4, name: 'carts', icon: <CartIcon />, hoverIcon: <CartIcon fill="#4BDD97" />, selected: false },
  { id: 5, name: 'presenters', icon: <PresenterIcon />, hoverIcon: <PresenterIcon fill="#4BDD97" />, selected: false },
  // { id: 6, name: 'layouts', icon: <PresenterIcon />, hoverIcon: <PresenterIcon fill="#4BDD97" />, selected: false },
  { id: 7, name: 'palette', icon: <PaletteIcon />, hoverIcon: <PaletteIcon fill="#4BDD97" />, selected: false },
  { id: 8, name: 'categorys', icon: <CategoryIcon />, hoverIcon: <CategoryIcon fill="#4BDD97" />, selected: false },
]

const _showAddBtns = [ 'carts', 'presenters']


export default function Index(props) {

  const _ComponentPagesMap = {
    indicators: Indicators ,
    selectors: Selectors,
    carts: Carts,
    presenters: Presenters,
    layouts: Layouts,
    auto: AutoLayouts
  }

  const [menuList, setMenuList] = useState(_menuList)
  const [onHover, setOnHover] = useState(false);
  const [currentHoverMenuId, setCurrentHoverMenuId] = useState(0);
  const [componentName, setComponentName] = useState('indicators');
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
      // console.log('menu item click = ', clickItem)
    }
  }

  //左侧组件列表
  const matchComponentPage = () => {
    const C = _ComponentPagesMap[componentName]
    return <C {...props} isSwitch={isSwitch} />
  }

  return (
    <HStack spacing='0' overflowY={'hidden'}>
      <Box style={{ width: '100px', height: '100vh', background: '#1F2229', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <VStack spacing={5} className='menu-container'>
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
        {componentName ==='palette' ? (
          <Box w={"100%"} h={"100%"} bg={"#ffffff"} padding={'10px'}>
            <PaletteManage/>
          </Box>
        ) : componentName === 'categorys' ? (
          <Box w={"100%"} h={"100%"} bg={"#EDECF1"}>
            <Categorys/>
          </Box>
        ) : componentName ? matchComponentPage() : <></>}
      </Box>

    </HStack>
  )

}