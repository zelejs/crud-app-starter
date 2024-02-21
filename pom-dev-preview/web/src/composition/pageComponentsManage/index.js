import React, { useState } from 'react';
import { HStack, VStack, Box, Button, Text } from '@chakra-ui/react';
import { history } from 'umi';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import Indicators from '@/composition/indicators';
import Selectors from '@/composition/selectors';

import { IndicatorIcon, SelectorIcon } from './svg'

const _menuList = [
  { id: 1, name: 'indicators', icon: <IndicatorIcon />, hoverIcon: <IndicatorIcon fill="#4BDD97" />, selected: true },
  { id: 2, name: 'selectors', icon: <SelectorIcon />, hoverIcon: <SelectorIcon fill="#4BDD97" />, selected: false },
]




export default function Index(props) {

  const _ComponentPagesMap = {
    indicators: <Indicators {...props}/>,
    selectors: <Selectors {...props}/>,
  }

  const [ menuList, setMenuList ] = useState(_menuList)
  const [ onHover, setOnHover] = useState(false);
  const [ currentHoverMenuId , setCurrentHoverMenuId ] = useState(0);
  const [ componentName , setComponentName ] = useState('indicators');

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
      if(item.id === menuItem.id){
        clickItem = item
        item.selected = true
        setComponentName(item.name)
      }else{
        item.selected = false
      }
      return item;
    })
    setCurrentHoverMenuId(0)
    setMenuList(menuList)
    if(clickItem){
      console.log('clickItem = ', clickItem)
    }
  }

  return (
    <HStack spacing='0'>
      <Box style={{ width: '100px', height: '100vh', background: '#1F2229', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
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
                      { item.selected ? item.hoverIcon : onHover && currentHoverMenuId == item.id ? item.hoverIcon : item.icon}
                    </Box>
                  )
                }
                <Box>
                  <Text color={item.selected ? "#4BDD97" : onHover && currentHoverMenuId == item.id ? "#4BDD97" : "#fff"}>{item.name}</Text>
                </Box>
              </VStack>
            </Box>

          ))}
        </VStack>
      </Box>

      <Box style={{ width: '100%', height: '100vh', background: '#15171C' }}>
        { componentName ? _ComponentPagesMap[componentName] : <></>}
      </Box>

    </HStack>
  )

}